package Logic;

import java.util.ArrayList;
import java.util.List;

public class MapBuilder {
    private int width;
    private int height;
    ArrayList<Room> rooms;
    ArrayList<Edge> edges;

    public MapBuilder() {
        width = 10;
        height = 10;
        rooms = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public MapBuilder setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public MapBuilder addRoom(Room room) {
        rooms.add(room);
        return this;
    }

    public MapBuilder addRoom(int x, int y, int width, int height) {
        rooms.add(new Room(x, y, width, height));
        return this;
    }

    public MapBuilder addRooms(List<Room> rooms) {
        for (Room room : rooms) {
            addRoom(room);
        }
        return this;
    }

    public MapBuilder addEdge(Edge edge) {
        edges.add(edge);
        return this;
    }

    public MapBuilder addEdges(List<Edge> edges) {
        for (Edge edge : edges) {
            addEdge(edge);
        }
        return this;
    }

    public Map get() {
        Map map = new Map(width, height);
        for (Room room : rooms) {
            map.addRoom(room);
            map.drawRoom(room);
        }
        for (Edge edge : edges) {
            map.addEdge(edge);
            map.drawLine(edge);
        }
        return map;
    }
}
