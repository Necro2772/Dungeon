package Logic;

import java.util.AbstractMap;
import java.util.Objects;

public class Edge { // Unweighted
    private final Room r1;
    private final Room r2;

    private final AbstractMap.SimpleEntry<Integer, Integer> door1;

    private final AbstractMap.SimpleEntry<Integer, Integer> door2;

    public Edge(Room r1, Room r2) {
        this.r1 = r1;
        this.r2 = r2;

        double v1 = r2.getX() - r1.getX();
        double v2 = r2.getY() - r1.getY();

        double l = Math.hypot(v1, v2);

        v1 = v1 / l;
        v2 = v2 / l;

        if (v1 >= Math.sqrt(2) / 2) {
            door1 = r1.getRight();
            door2 = r2.getLeft();
        } else if (v1 < - Math.sqrt(2) / 2) {
            door1 = r1.getLeft();
            door2 = r2.getRight();
        } else if (v2 < 0) {
            door1 = r1.getTop();
            door2 = r2.getBottom();
        } else {
            door1 = r1.getBottom();
            door2 = r2.getTop();
        }
    }

    public Edge(int x1, int y1, int x2, int y2) {
        this(new Room(x1, y1, 3, 3), new Room(x2, y2, 3, 3));
    }

    public Room getRoom1() {
        return r1;
    }

    public Room getRoom2() {
        return r2;
    }

    public AbstractMap.SimpleEntry<Integer, Integer> getDoor1() {
        return door1;
    }

    public AbstractMap.SimpleEntry<Integer, Integer> getDoor2() {
        return door2;
    }

    public int getMinX() {
        return Math.min(r1.getX(), r2.getX());
    }

    public int getMaxX() {
        return Math.max(r1.getX(), r2.getX());
    }

    public int getMinY() {
        return Math.min(r1.getY(), r2.getY());
    }

    public int getMaxY() {
        return Math.max(r1.getY(), r2.getY());
    }

    @Override
    public String toString() {
        return "Edge{" +
                "(" + r1 +
                ") -> (" + r2 +
                ")}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return (r1.equals(edge.r1) && r2.equals(edge.r2))
                || (r1.equals(edge.r2) && r2.equals(edge.r1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(r1, r2);
    }
}
