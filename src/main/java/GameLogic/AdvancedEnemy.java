package GameLogic;

import MapLogic.Map;
import javafx.util.Pair;

public class AdvancedEnemy implements Enemy {
    private final int callDistance = 60;
    private final int visibility = 20;
    private Pair<Integer, Integer> position;
    private Pair<Integer, Integer> destination;
    private EnemyState state;
    private final Game game;

    public AdvancedEnemy(Pair<Integer, Integer> pos, Game game) {
        this.game = game;
    }
    @Override
    public Pair<Integer, Integer> getPosition() {
        return position;
    }

    @Override
    public EnemyState getState() {
        return state;
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
        } else if (state == EnemyState.WANDERING) { // TODO: probably update. ?
            // Moves to random direction if player location is unknown
            // Does nothing 60% of moves
            if (Math.random() < 0.6) {
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

    @Override
    public void look(Map map) {
        int[][] fogGrid = map.getFoggyGrid(visibility, position);
        for (int y = Math.max(0, position.getValue() - visibility); y < Math.min(fogGrid.length, position.getValue() + visibility); y++) {
            for (int x = Math.max(0, position.getKey() - visibility); x < Math.min(fogGrid[0].length, position.getKey() + visibility); x++) {
                if (fogGrid[y][x] == Map.PLAYER) {
                    //destination = new Pair<>(x, y);
                    setDestination(map);
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
//        if (map.getPlayerLoc().getValue() < position.getValue() + visibility && position.getValue() - visibility < map.getPlayerLoc().getValue() &&
//                map.getPlayerLoc().getKey() < position.getKey() + visibility && position.getKey() - visibility < map.getPlayerLoc().getKey()) {
//            destination = map.getPlayerLoc();
//            if (state == EnemyState.WANDERING) {
//                call();
//                game.alertPlayer();
//            }
//            state = EnemyState.MOVING;
//        }
        look(map);
    }

    private void setDestination(Map map) {
        // Checks all points of 1/2 dist to player from player's location, adds to list
        // From that, check distance of enemies to those locations. Whichever has the fewest enemies within a radius (20?), it goes to!
        int dist = map.getPath(position, map.getPlayerLoc()).size();
        if (dist < 10) {
            destination = map.getPlayerLoc();
            return;
        }
        //int[][] grid = map.getFoggyGrid();
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

    }
}
