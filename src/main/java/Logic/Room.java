package Logic;

import java.util.AbstractMap;
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

    public AbstractMap.SimpleEntry<Integer, Integer> getTop() {
        return new AbstractMap.SimpleEntry<>(x + width / 2, y - 1);
    }

    public AbstractMap.SimpleEntry<Integer, Integer> getBottom() {
        return new AbstractMap.SimpleEntry<>(x + width / 2, y + height);
    }

    public AbstractMap.SimpleEntry<Integer, Integer> getLeft() {
        return new AbstractMap.SimpleEntry<>(x - 1, y + height / 2);
    }

    public AbstractMap.SimpleEntry<Integer, Integer> getRight() {
        return new AbstractMap.SimpleEntry<>(x + width, y + height / 2);
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
