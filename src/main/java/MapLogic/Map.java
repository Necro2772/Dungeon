package MapLogic;

import GameLogic.Enemy;
import javafx.util.Pair;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Map {
    private final int[][] grid;
    private final ArrayList<Room> rooms;
    private final ArrayList<Edge> edges;

    private Pair<Integer, Integer> playerLoc;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    public static final int WALL = 0;
    public static final int PATH = 1;
    public static final int ROOM = 2;
    public static final int START = 3;
    public static final int EXIT = 4;
    public static final int PLAYER = 5;
    public static final int ENEMY = 6;
    public static final int FOG = 7;
    public static final int VISION_TILE = 8;
    public static final int WANDERING_ENEMY = 9;
    public static final int MOVING_ENEMY = 10;

    public Map(int width, int height) {
        grid = new int[height][width];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = WALL;
            }
        }
        rooms = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public void setPlayerLoc(Pair<Integer, Integer> loc) {
        playerLoc = loc;
    }

    public void setPlayerLoc(int x, int y) {
        playerLoc = new Pair<>(x, y);
    }

    public void setExit(Room room) {
        grid[room.getCenter().getValue()][room.getCenter().getKey()] = EXIT;
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public Pair<Integer, Integer> getPlayerLoc() {
        return playerLoc;
    }

    public ArrayList<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public int getNumRooms() {
        return rooms.size();
    }

    public void drawRoom(Room room) {
        for (int x = room.getX(); x < room.getX() + room.getWidth(); x++) {
            for (int y = room.getY(); y < room.getY() + room.getHeight(); y++) {
                grid[y][x] = ROOM;
            }
        }
    }

    public void drawLine(Edge edge) {
        // Vector <v1, v2> from room1 to room2
        int v1 = edge.getDoor2().getKey() - edge.getDoor1().getKey();
        int v2 = edge.getDoor2().getValue() - edge.getDoor1().getValue();

        // Account for straight lines
        if (v1 == 0) {
            int x = edge.getDoor1().getKey();
            int y = edge.getDoor1().getValue();
            int v2s = v2 / Math.abs(v2);
            while (y * v2s <= edge.getDoor2().getValue() * v2s) {
                grid[y][x] = PATH;
                y += v2s;
            }
            return;
        } else if (v2 == 0) {
            int x = edge.getDoor1().getKey();
            int y = edge.getDoor1().getValue();
            int v1s = v1 / Math.abs(v1);
            while (x * v1s <= edge.getDoor2().getKey() * v1s) {
                grid[y][x] = PATH;
                x += v1s;
            }
            return;
        }
        int v1s = v1 / Math.abs(v1);
        int v2s = v2 / Math.abs(v2);

        double x = edge.getDoor1().getKey();
        double y = edge.getDoor1().getValue();

        double slope = (double) v2 / v1;

        while (x * v1s <= edge.getDoor2().getKey() * v1s || y * v2s <= edge.getDoor2().getValue() * v2s) {
            grid[(int) Math.round(y)][(int) Math.round(x)] = PATH;
            if (Math.abs(slope) > 1) {
                y += v2s;
                x += v1s / Math.abs(slope);
            } else {
                x += v1s;
                y += Math.abs(slope) * v2s;
            }
        }
    }
    
    public ArrayList<Pair<Integer, Integer>> getPath(Pair<Integer, Integer> start, Pair<Integer, Integer> end) {
        ArrayList<Pair<Integer, Integer>> path = new ArrayList<>();
        ArrayList<Pair<Integer, Integer>> searched = new ArrayList<>();
        ArrayList<Pair<Integer, Integer>> found = new ArrayList<>();
        found.add(start);
        ArrayList<Integer> indexes = new ArrayList<>();
        indexes.add(0);

        if (!getPathRec(start, end, searched, found, indexes)) {
            return path;
        }

        Pair<Integer, Integer> current = end;

        while (!current.equals(start)) {
            path.add(0, current);
            current = searched.get(indexes.get(searched.indexOf(current)));
        }

        return path;
    }

    private boolean getPathRec(Pair<Integer, Integer> current, Pair<Integer, Integer> end, ArrayList<Pair<Integer, Integer>> searched,
                                                        ArrayList<Pair<Integer, Integer>> found, ArrayList<Integer> indexes) {
        found.remove(current);
        searched.add(current);

        if (current.equals(end)) {
            return true;
        }

        for (Pair<Integer, Integer> item : getNeighbors(current)) {
            if (!searched.contains(item) && !found.contains(item)) {
                found.add(item);
                indexes.add(searched.indexOf(current));
            }
        }
        if (found.size() == 0) return false;
        return getPathRec(found.get(0), end, searched, found, indexes);
    }
    
    public ArrayList<Pair<Integer, Integer>> getNeighbors(Pair<Integer, Integer> node) {
        ArrayList<Pair<Integer, Integer>> out = new ArrayList<>();
        for (int x : new int[]{0, -1, 1}) {
            for (int y : new int[]{0, -1, 1}) {
                if (x != 0 || y != 0) {
                    Pair<Integer, Integer> current = new Pair<>(node.getKey() + x, node.getValue() + y);
                    if (current.getValue() >= 0 && current.getKey() >= 0 && current.getValue() < grid.length && current.getKey() < grid[0].length
                            && grid[current.getValue()][current.getKey()] != WALL) {
                        out.add(current);
                    }
                }
            }
        }
        return out;
    }

    /**
     * clears the map to all walls. Should only be used for testing
     */
    public void clear() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = WALL;
            }
        }
        rooms.clear();
        edges.clear();
    }

    /**
     * returns the grid with player and enemies added on base layer
     * @return int[][] representing the grid, including player and enemies
     */
    public int[][] getGrid() {
        int[][] display = new int[grid.length][grid[0].length];

        for (int y = 0; y < grid.length; y++) {
            System.arraycopy(grid[y], 0, display[y], 0, grid[0].length);
        }

        if (playerLoc != null) {
            display[playerLoc.getValue()][playerLoc.getKey()] = PLAYER;
        }

        for (Enemy enemy : enemies) {
            //display[enemy.getPosition().getValue()][enemy.getPosition().getKey()] = ENEMY;
            switch (enemy.getState()) {
                case MOVING -> display[enemy.getPosition().getValue()][enemy.getPosition().getKey()] = MOVING_ENEMY;
                case WANDERING -> display[enemy.getPosition().getValue()][enemy.getPosition().getKey()] = WANDERING_ENEMY;
            }
        }

        return display;
    }

    /**
     * returns a version of getGrid with fog overlay on spaces more than visibility spaces from center,
     * determined by BFS on non-wall tiles
     * @param visibility distance from center that can be seen
     * @param center position to remove fog from
     * @return display of getGrid with fog added
     */
    public int[][] getFoggyGrid(int visibility, Pair<Integer, Integer> center) {
        int[][] foggyGrid = new int[grid.length][grid[0].length];

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] != WALL) foggyGrid[y][x] = FOG;
            }
        }

        ArrayList<Pair<Integer, Integer>> searched = new ArrayList<>();
        ArrayList<Pair<Integer, Integer>> found = new ArrayList<>();
        found.add(center);
        int[][] t = new int[grid.length][grid[0].length];
        t[center.getValue()][center.getKey()] = visibility;
        addVisibilityRec(searched, found, foggyGrid, t);

        return foggyGrid;
    }

    private void addVisibilityRec(ArrayList<Pair<Integer, Integer>> searched, ArrayList<Pair<Integer, Integer>> found, int[][] fogGrid, int[][] t) {
        Pair<Integer, Integer> current = found.get(0);
        found.remove(0);
        fogGrid[current.getValue()][current.getKey()] = getGrid()[current.getValue()][current.getKey()];
        searched.add(current);
        if (t[current.getValue()][current.getKey()] == 1) return;

        for (Pair<Integer, Integer> item : getNeighbors(current)) {
            if (!searched.contains(item) && !found.contains(item)) {
                found.add(item);
                t[item.getValue()][item.getKey()] = t[current.getValue()][current.getKey()] - 1;
            }
        }
        if (found.isEmpty()) return;
        addVisibilityRec(searched, found, fogGrid, t);
    }

    public int getSpace(Pair<Integer, Integer> space) {
        return grid[space.getValue()][space.getKey()];
    }

    public Pair<Integer, Integer> getStart() {
        return rooms.get(0).getCenter();
    }

    public void drawVisionTile(Pair<Integer, Integer> center) {
        grid[center.getValue()][center.getKey()] = VISION_TILE;
    }

    public boolean onVisionTile() {
        return getSpace(playerLoc) == VISION_TILE;
    }
}
