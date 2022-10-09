package GameLogic;

import MapLogic.BasicPopulate;
import MapLogic.GenerateBasicMap;
import MapLogic.Map;
import MapLogic.PopulateMap;
import UI.MapUI;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Objects;

public class Game {
    private Player player;
    private final Stage stage;
    private final MapUI mapUI;
    private Map map;
    private final ArrayList<Task<Void>> buffer = new ArrayList<>();
    private static final long TURN_WAIT = 250;
    private static final long FAST_TURN_WAIT = 50;

    private ArrayList<Enemy> enemies;
    public Game(Stage stage) {
        this.stage = stage;
        loadMap();

        this.mapUI = new MapUI(this);
        updateMap();
    }

    /**
     * moves the player to the specified position, and advances a single turn
     * @param x x-position player moves to
     * @param y y-position player moves to
     */
    public void move(int x, int y) {
        // Player move
        player.move(map, x, y);
        updateMap();

        // Dungeon move
        for (Enemy enemy : enemies) {
            if (enemy.getPosition().equals(map.getPlayerLoc())) {
                gameOver();
            }

            if (map.onVisionTile()) {
                enemy.lookSq(map);
            } else {
                enemy.look(map);
            }
            enemy.move(map);

            if (enemy.getPosition().equals(map.getPlayerLoc())) {
                gameOver();
            }
        }
        updateMap();

        if (map.getSpace(player.getLoc()) == Map.EXIT) win();
    }

    public void updateMap() {
        if (map.onVisionTile()) {
            mapUI.update(map.getGrid());
        } else {
            mapUI.update(map.getFoggyGrid(player.getVisibility(), player.getLoc()));
        }
    }

    public void gameOver() {
        clearBuffer();
        mapUI.setEnabled(false);

        Label label = new Label("Game Over!");
        Button button = new Button("Restart");
        Popup popup = new Popup();
        VBox box = new VBox();

        label.setFont(Font.font(20));

        box.setStyle("-fx-background-color:lightgray;-fx-border-color:darkgray;-fx-border-width:2;");
        box.setMinSize(150, 80);
        box.setSpacing(15);
        box.setPadding(new Insets(15));

        button.setOnAction(action -> {
            popup.hide();
            restart();
        });

        box.getChildren().add(label);
        box.getChildren().add(button);
        box.setAlignment(Pos.CENTER);

        popup.getContent().add(box);
        popup.show(stage);
    }

    public void win() {
        clearBuffer();
        mapUI.setEnabled(false);

        Label label = new Label("You win!");
        Button button = new Button("Restart");
        Popup popup = new Popup();
        VBox box = new VBox();

        label.setFont(Font.font(20));

        box.setStyle("-fx-background-color:lightgray;-fx-border-color:darkgray;-fx-border-width:2;");
        box.setMinSize(150, 80);
        box.setSpacing(15);
        box.setPadding(new Insets(15));

        button.setOnAction(action -> {
            popup.hide();
            restart();
        });

        box.getChildren().add(label);
        box.getChildren().add(button);
        box.setAlignment(Pos.CENTER);

        popup.getContent().add(box);
        popup.show(stage);
    }

    public void restart() {
        mapUI.reset();
        loadMap();
        updateMap();
    }

    private void loadMap() {
        map = new GenerateBasicMap(10, 6).get();
        PopulateMap basicPopulate = new BasicPopulate();
        player = basicPopulate.generatePlayer(map);
        enemies = basicPopulate.generateEnemies(map, this);
        basicPopulate.generateMap(map);
    }

    public void clearBuffer() {
        for (Task<Void> task : buffer) {
            task.cancel();
        }
        while (!buffer.isEmpty()) {
            buffer.remove(0);
        }
    }

    /**
     * Used to stop auto-movement if an enemy comes in sight of the player
     */
    public void alertPlayer() {
        clearBuffer();
    }

    /**
     * alerts enemies, causing them to go towards the alert center
     * @param center location to center alert on
     * @param distance maximum distance from center where enemies will receive the alert
     */
    public void alertEnemies(Pair<Integer, Integer> center, int distance) {
        int[][] fogGrid = map.getFoggyGrid(distance, center);
        for (int y = Math.max(0, center.getValue() - distance); y < Math.min(fogGrid.length, center.getValue() + distance); y++) {
            for (int x = Math.max(0, center.getKey() - distance); x < Math.min(fogGrid[0].length, center.getKey() + distance); x++) {
                if (fogGrid[y][x] == Map.ENEMY || fogGrid[y][x] == Map.WANDERING_ENEMY || fogGrid[y][x] == Map.MOVING_ENEMY) {
                    for (Enemy enemy : enemies) {
                        if (Objects.equals(enemy.getPosition(), new Pair<>(x, y)) && enemy.getPosition() != center) {
                            enemy.alert(center);
                        }
                    }
                }
            }
        }
    }

    /**
     * buffers moves towards the destination with FAST_TURN_WAIT used for wait time
     * @param x x-position to move to
     * @param y y-position to move to
     */
    public void fastBufferedMove(int x, int y) {
        bufferedMove(x, y, FAST_TURN_WAIT);
    }

    /**
     * buffers moves towards the destination with TURN_WAIT used for wait time
     * @param x x-position to move to
     * @param y y-position to move to
     */
    public void bufferedMove(int x, int y) {
        bufferedMove(x, y, TURN_WAIT);
    }

    /**
     * buffers moves for the player to move to a destination
     * @param x x-position for player to move to
     * @param y y-position for player to move to
     * @param wait time to wait before advancing the next turn
     */
    public void bufferedMove(int x, int y, long wait) {
        clearBuffer();
        if (new Pair<>(x, y).equals(player.getLoc())) {
            move(player.getLoc().getKey(), player.getLoc().getValue());
        }
        ArrayList<Pair<Integer, Integer>> directions = map.getPath(map.getPlayerLoc(), new Pair<>(x, y));
        final long waitF = directions.size() > 10 ? FAST_TURN_WAIT : wait;

        for (int i = 0; i < directions.size(); i++) {
            Pair<Integer, Integer> item = directions.get(i);
            int finalI = i;

            buffer.add(new Task<>() {
                @Override
                protected Void call() {
                    try {
                        Thread.sleep(waitF * finalI);
                    } catch (InterruptedException ignored) {
                    }
                    return null;
                }
            });

            buffer.get(buffer.size() - 1).setOnSucceeded(Event -> move(item.getKey(), item.getValue()));
            new Thread(buffer.get(buffer.size() - 1)).start();
        }
    }

    public Map getMap() {
        return map;
    }
    public Stage getStage() {
        return stage;
    }

    public MapUI getMapUI() {
        return mapUI;
    }
}
