package Logic;

import java.util.ArrayList;

public class Map {
    private final int[][] grid;
    private final ArrayList<Room> rooms;
    private final ArrayList<Edge> edges;

    public static final int WALL = 0;
    public static final int PATH = 1;
    public static final int ROOM = 2;
    public static final int START = 3;
    public static final int EXIT = 4;

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

    // Depreciated, does not work well
//    public void drawEdge(Edge edge) { //
//        int x = edge.getMinX() + 1;
//        int y = edge.getMinY() + 1;
//        while (x < edge.getMaxX() || y < edge.getMaxY()) {
//            grid[y][x] = OPEN;
//            if (x >= edge.getMaxX()) {
//                y++;
//            } else if (y >= edge.getMaxY()) {
//                x++;
//            } else {
//                if (Math.random() > 0.5) y++;
//                else x++;
//            }
//        }
//    }

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

        while (x * v1s <= edge.getDoor2().getKey() * v1s) {
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

    public int[][] getGrid() {
        return grid;
    }
}
