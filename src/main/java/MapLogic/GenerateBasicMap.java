package MapLogic;

import java.util.ArrayList;
import java.util.Comparator;

public class GenerateBasicMap {
    private final int width;
    private final int height;
    private final ArrayList<Room> rooms;
    private final ArrayList<Edge> edges;

    private static final int ROOM_SIZE = 3;
    private final int NUM_COLS; // numbered from 0

    private static final int COL_WIDTH = 15; // with rooms (interior) completely within this area
    private static final int COL_SPACING = 5;

    private static final int ROW_SPACING = 6;
    private static final int ROW_RAND = 16;


    public GenerateBasicMap(int aveRows, int cols) {
        NUM_COLS = cols;
        width = COL_WIDTH * NUM_COLS + COL_SPACING * (NUM_COLS - 1);
        height = aveRows * (ROOM_SIZE + ROW_SPACING + ROW_RAND / 2);
        // Average rooms per row is height / (ROOM_SIZE + ROW_SPACING + ROW_RAND / 2)
        // Currently average is 8 or so. For 10 rooms, 80 height
        rooms = new ArrayList<>();
        edges = new ArrayList<>();
    }

    private Room defaultRoom(int x, int y) {
        return new Room(x, y, ROOM_SIZE, ROOM_SIZE);
    }

    /**
     * Runs logic for generating all rooms
     */
    private void generateRooms() {
        //rooms.add(defaultRoom((width - ROOM_SIZE) / 2, height - ROOM_SIZE - 1)); // Bottom room
        rooms.add(defaultRoom((width - ROOM_SIZE) / 2, 1)); // Top room
        for (int i = 0; i < NUM_COLS; i++) {
            genColumn(i);
        }
    }

    /**
     * Generates rooms to fill a column
     * @param col number of column to generate
     */
    private void genColumn(int col) {
        int y = ROOM_SIZE + 1;
        if (Math.abs(col - ((NUM_COLS - 1) / 2)) > 1) { // Not center two columns
            y = - ROW_SPACING / 2;
        }
        while (y < height) {
            Room r = genRoomInColumn(col, y);
            if (r.getY() < height - ROOM_SIZE - 1){
                rooms.add(r);
            } else break;
            y = r.getY() + ROOM_SIZE + 1;
        }
    }

    /**
     * Generates a room in the given column, randomized distance from the given y value
     * @param col number of column to generate room in
     * @param y y value of previous room
     * @return room generated from values
     */
    private Room genRoomInColumn(int col, int y) {
        //int yOffset = (int)(Math.random() * ROW_RAND) + ROW_SPACING;
        double bias = 1.5;
        int yOffset;
        if (Math.random() > 0.5) {
            yOffset = (int)(Math.pow(Math.random(), bias) * ROW_RAND) + ROW_SPACING;
        } else {
            yOffset = (int)(Math.pow(Math.random(), 1 / bias) * ROW_RAND) + ROW_SPACING;
        }
        int xOffset = (int)(Math.random() * (COL_WIDTH - ROOM_SIZE));

        int roomy = y + yOffset;
        int roomx = getColX(col) + xOffset;

        return defaultRoom(roomx, roomy);
    }

    /**
     * Generates edges for the entire map, given that all rooms have been generated
     */
    private void generateEdges() {
        // Organize rooms by y-value
        rooms.sort(Comparator.comparingInt(Room::getY));
        // Go from top of map, fill rooms to minimum conditions
        for (int j = 0; j < rooms.size(); j++) {
            Room r = rooms.get(j);
            if (r.equals(rooms.get(0))) { // Starting room
                // 2 edges near
                for (int i = 0; i < 2; i++) { // Add near
                    ArrayList<Room> nearbyList = new ArrayList<>(rooms);
                    nearbyList.remove(0); // Remove self
                    Room near = getRoomNear(r, nearbyList);
                    genEdge(r, near);
                    nearbyList.remove(near);
                }
            } else {
                int index = 0;
                ArrayList<Room> nearby = new ArrayList<>();
                if (!lastInCol(r)) {
                    index = rooms.indexOf(r) + 1;
                    while (rooms.get(index).getY() == r.getY()) {
                        if (index >= rooms.size()) break;
                        index++;
                    }
                    nearby = new ArrayList<>(rooms.subList(index, rooms.size()));
                }
                int minEdges;
                if (lastInCol(r)) {
                    minEdges = 2; // Last in column, 2 edges min and no edges down
                } else if (getCol(r) == 0 || getCol(r) == NUM_COLS - 1) {
                    genEdge(r, getRoomNear(r, nearby)); // Add 1 edge down
                    minEdges = 2; // Edge column, 2 edges min
                } else {
                    nearby.removeIf((Room item) -> getCol(item) == getCol(r)); // Remove from same column.
                    // Removing from same column seems to very occasionally cause rooms to be blocked off, with very long vertical paths...
                    // But that usually doesn't happen! it's fine!!

                    genEdge(r, getRoomNear(r, nearby)); // Add 1 edge down
                    minEdges = 3; // Center column, 3 edges min
                }
                while (numEdges(r) < minEdges) {
                    ArrayList<Room> nearbyList = new ArrayList<>(rooms);
                    nearbyList.removeIf((Room item) -> getCol(item) == getCol(r));
                    Room near = getRoomNear(r, nearbyList);
                    if (near == null) break; // May want to handle some other way?
                    genEdge(r, near);
                    nearbyList.remove(near);
                }
            }
        }


        ArrayList<Room> disconnectedRooms = new ArrayList<>();
        for (Room room : rooms) {
            boolean isConnected = false;
            for (Edge edge : edges) {
                if (edge.getRoom1().equals(room) || edge.getRoom2().equals(room)) {
                    isConnected = true;
                    break;
                }
            }
            if (!isConnected) disconnectedRooms.add(room);
        }
        if (!disconnectedRooms.isEmpty()) rooms.removeAll(disconnectedRooms);
        // TODO: check for disconnected components at end and remove them. Seems to be pretty rare though for now
        // TODO: update check for validity, sometimes allows edges to cross rooms
    }
    private int numEdges(Room r) {
        return (int) edges.stream().filter(edge -> edge.getRoom1().equals(r) || edge.getRoom2().equals(r)).count();
    }

    /**
     * returns the nearest room to the given room from the list, excluding those already connected
     * prevents intersections, null if no room is found that would prevent intersections
     * @param r room to search from
     * @param others list of rooms to search
     * @return room closest to the input r from the list others, may return null
     */
    private Room getRoomNear(Room r, ArrayList<Room> others) {
        others.removeIf((Room o) -> edges.contains(new Edge(r, o)));
        Room closest = others.get(0);
        if (!validEdge(new Edge(r, closest))) {
            for (Room other : others) {
                if (validEdge(new Edge(r, other)))  {
                    closest = other;
                    break;
                }
            }
            if (closest.equals(others.get(0))) {
                //System.err.println("Null room returned in getRoomNear!");
                return null;
            }
        }
        for (Room other : others) {
            if (pathIsShorter(r, closest, other) && validEdge(new Edge(r, other))) closest = other;
        }
        return closest;
    }

    private void genEdge(Room r1, Room r2) {
        if (r1 == null || r2 == null) return;
        edges.add(new Edge(r1, r2));
    }

    private boolean validEdge(Edge edge) {
        for (Edge oEdge : edges) {
            if (Utilities.willIntercept(edge, oEdge)) return false;
        }
        return true;
    }

    // Helper Methods

    /**
     * returns the starting x value of the given column
     * @param col column to return start of
     * @return starting position x of column
     */
    private static int getColX(int col) {
        if (col == 0) return 0;
        return col * (COL_WIDTH + COL_SPACING);
    }

    /**
     * returns the column number of the given room
     * @param r room to check column of
     * @return column number of given room
     */
    private int getCol(Room r) {
        int border = COL_WIDTH;
        for (int i = 0; i < NUM_COLS; i++) {
            if (r.getX() < border) return i;
            border += COL_SPACING + COL_WIDTH;
        }
        System.err.println("Cannot determine column number for room at x=" + r.getX());
        System.exit(1);
        return -1;
    }

    /**
     * returns dist(r, o1) > dist(r, o2)
     * @param r room to compare paths from
     * @param o1 first option
     * @param o2 second option
     * @return true if strictly closer to o2, false if closer to o1 or equal
     */
    private static boolean pathIsShorter(Room r, Room o1, Room o2) {
        return Math.hypot(r.getX() - o1.getX(), r.getY() - o1.getY()) >
                Math.hypot(r.getX() - o2.getX(), r.getY() - o2.getY());
    }
    private boolean lastInCol(Room r) {
        rooms.sort(Comparator.comparingInt(Room::getY));
        for (int i = rooms.indexOf(r) + 1; i < rooms.size(); i++) {
            if (getCol(rooms.get(i)) == getCol(r)) {
                return false;
            }
        }
        return true;
    }

    public Map get() {
        generateRooms();
        generateEdges();
        return new MapBuilder().setSize(width, height).addRooms(rooms).addEdges(edges).get();
    }
}
