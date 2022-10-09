package MapLogic;

import GameLogic.BasicEnemy;
import GameLogic.Enemy;
import GameLogic.Game;
import GameLogic.Player;
import javafx.util.Pair;

import java.util.ArrayList;

public class BasicPopulate implements PopulateMap{
    private static final int LAYER = 60;
    private static final int NUM_ENEMIES = 3;
    @Override
    public Player generatePlayer(Map map) {
        map.setPlayerLoc(map.getStart()); // Init player location
        return new Player(map.getStart());
    }

    @Override
    public ArrayList<Enemy> generateEnemies(Map map, Game game) {
        ArrayList<Enemy> enemies = new ArrayList<>();
        ArrayList<Room> rooms = new ArrayList<>(map.getRooms());
        rooms.removeIf(room -> room.getY() < LAYER);

        for (int i = 0; i < NUM_ENEMIES; i++) {
            Room current = rooms.get((int)(Math.random() * rooms.size()));

            ArrayList<Pair<Integer, Integer>> spaces = current.getSpaces();
            Enemy enemy = new BasicEnemy(spaces.get((int)(Math.random() * spaces.size())), game);
            map.addEnemy(enemy);

            enemies.add(enemy);
            rooms.remove(current);
        }

        return enemies;
    }

    @Override
    public void generateMap(Map map) {
        for (Room room : map.getRooms()) {
            map.drawVisionTile(room.getCenter());
        }

        ArrayList<Room> rooms = map.getRooms();
        rooms.removeIf(room -> room.getY() < map.getGrid().length - 40);
        map.setExit(rooms.get((int)(Math.random() * rooms.size())));
    }
}
