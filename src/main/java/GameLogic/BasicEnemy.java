package GameLogic;

import MapLogic.Map;
import javafx.util.Pair;

public class BasicEnemy implements Enemy{
    private final int callDistance = 60;
    private final int visibility = 20;
    private Pair<Integer, Integer> position;
    private Pair<Integer, Integer> destination;
    private EnemyState state;
    private final Game game;

    public BasicEnemy(Pair<Integer, Integer> pos, Game game) {
        position = pos;
        state = EnemyState.WANDERING;
        destination = null;
        this.game = game;
    }

    @Override
    public EnemyState getState() {
        return state;
    }

    @Override
    public Pair<Integer, Integer> getPosition() {
        return position;
    }

    @Override
    public void setPosition(Pair<Integer, Integer> position) {
        this.position = position;
    }

    @Override
    public void move(Map map) {
        if (state == EnemyState.MOVING) { // Goes straight towards player
            position = map.getPath(position, destination).get(0);
            if (Math.random() < 0.1) call();
            if (position.equals(destination)) {
                destination = null;
                state = EnemyState.WANDERING;
            }
        } else if (state == EnemyState.WANDERING){
            // Moves to random direction if player location is unknown
            // Does nothing 30% of moves
            if (Math.random() < 0.3) {
                return;
            }
            if (Math.random() < 0.1 || destination == null) { // changes destination 10% of moves
                destination = map.getRooms().get((int)(Math.random() * map.getNumRooms())).getCenter();
            }

            position = map.getPath(position, destination).get(0);
            if (position.equals(destination)) {
                destination = null;
            }
        }
    }

//    public void look(Map map) {
//        destination = map.getPlayerLoc();
//        if (state == EnemyState.WANDERING) {
//            call();
//            game.alertPlayer();
//        }
//        state = EnemyState.MOVING;
//    }

    @Override
    public void look(Map map) {
        int[][] fogGrid = map.getFoggyGrid(visibility, position);
        for (int y = Math.max(0, position.getValue() - visibility); y < Math.min(fogGrid.length, position.getValue() + visibility); y++) {
            for (int x = Math.max(0, position.getKey() - visibility); x < Math.min(fogGrid[0].length, position.getKey() + visibility); x++) {
                if (fogGrid[y][x] == Map.PLAYER) {
                    destination = new Pair<>(x, y);
                    if (state == EnemyState.WANDERING) {
                        call();
                        game.alertPlayer();
                    }
                    state = EnemyState.MOVING;
                }
            }
        }
    }

    @Override
    public void lookSq(Map map) {
        int visibility = (int) (this.visibility * 1.5);
        if (map.getPlayerLoc().getValue() < position.getValue() + visibility && position.getValue() - visibility < map.getPlayerLoc().getValue() &&
            map.getPlayerLoc().getKey() < position.getKey() + visibility && position.getKey() - visibility < map.getPlayerLoc().getKey()) {
            destination = map.getPlayerLoc();
            if (state == EnemyState.WANDERING) {
                call();
                game.alertPlayer();
            }
            state = EnemyState.MOVING;
        }
    }

    @Override
    public void alert(Pair<Integer, Integer> destination) {
        if (state == EnemyState.MOVING) return;
        this.destination = destination;
        game.alertPlayer();
        state = EnemyState.MOVING;
    }

    @Override
    public void call() {
        game.alertEnemies(position, callDistance);
    }
}
