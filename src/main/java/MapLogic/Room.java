package MapLogic;

import javafx.util.Pair;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Objects;
import java.lang.Object;

public class Room {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean contains(int x, int y) {
        return this.x <= x && this.y <= y &&
                this.x + width > x && this.y + height > y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public ArrayList<Pair<Integer, Integer>> getSpaces() {
        ArrayList<Pair<Integer, Integer>> spaces = new ArrayList<>();
        for (int x = this.x; x < this.x + width - 1; x++) {
            for (int y = this.y; y < this.y + height - 1; y++) {
                spaces.add(new Pair<>(x, y));
            }
        }
        return spaces;
    }

    public Pair<Integer, Integer> getTop() {
        return new Pair<>(x + width / 2, y - 1);
    }

    public Pair<Integer, Integer> getBottom() {
        return new Pair<>(x + width / 2, y + height);
    }

    public Pair<Integer, Integer> getLeft() {
        return new Pair<>(x - 1, y + height / 2);
    }

    public Pair<Integer, Integer> getRight() {
        return new Pair<>(x + width, y + height / 2);
    }

    public Pair<Integer, Integer> getCenter() {
        return new Pair<>(x + width / 2, y + height / 2);
    }

    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return x == room.x && y == room.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
