package UI;

import GameLogic.Game;
import MapLogic.Map;
import javafx.scene.Group;

public class MapUI {
    private final Game game;
    int width;
    int height;
    int sideLength = 10;
    private final Group root;
    private double rootOffsetY;
    private boolean enabled;

    GridSquare[][] gridSpaces;

    public MapUI(Game game) {
        enabled = true;
        this.root = new Group();
        this.game = game;
        width = game.getMap().getGrid()[0].length;
        height = game.getMap().getGrid().length;
        gridSpaces = new GridSquare[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                GridSquare node = new GridSquare(x, y, sideLength, sideLength, game.getMap().getGrid()[y][x]);
                root.getChildren().add(node);
                gridSpaces[y][x] = node;
            }
        }

        root.setTranslateX(50);
        root.setTranslateY(30);

        // View controls for map
        root.setOnMousePressed(event -> {
            rootOffsetY = root.getTranslateY() - event.getScreenY();
        });

        root.setOnMouseDragged(event -> {
            if (validYOffset(event.getScreenY() + rootOffsetY) && enabled) {
                root.setTranslateY(event.getScreenY() + rootOffsetY);
            }
        });

        //root.setOnScrollStarted(event -> rootOffsetY = root.getTranslateY());

        root.setOnScroll(event -> {
            rootOffsetY = root.getTranslateY();
            double scroll = root.getTranslateY() + event.getDeltaY() * 1.5;
            if (validYOffset(scroll) && enabled) {
                root.setTranslateY(scroll);
            }
        });

        // Click controls
        root.setOnMouseClicked(event -> {
            int x = (int) event.getX() / 10;
            int y = (int) event.getY() / 10;
            if (game.getMap().getGrid()[y][x] != Map.WALL && enabled) {
                if (event.getClickCount() == 2) {
                    game.fastBufferedMove(x, y);
                } else {
                    game.bufferedMove(x, y);
                }
            } else {
                game.clearBuffer();
            }
        });
    }

    public int getWidth() {
        return width;
    }

    public Group getRoot() {
        return root;
    }

    public boolean validYOffset(double yOffset) {
        double border = 150;
        if (!(yOffset < border)) { // Top border
            return yOffset < rootOffsetY;
        }
        if (!(yOffset > height * -sideLength + game.getStage().getScene().getHeight() - border)) { // Bottom border
            return yOffset > rootOffsetY;
        }
        return true;
    }

    public void update(int[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                GridSquare node = gridSpaces[y][x];
                node.setType(map[y][x]);
            }
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void reset() {
        setEnabled(true);
        root.setTranslateY(0);
    }
}
