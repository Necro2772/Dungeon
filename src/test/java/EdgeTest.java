import Logic.Edge;
import Logic.Room;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EdgeTest {
    @Test
    public void doorsTest() {
        // Test cases: right, up, left, down, and all diagonals
        Room[] r1 = new Room[]{new Room(5, 5, 3, 3),
                new Room(5, 15, 3, 3),
                new Room(15, 5, 3, 3),
                new Room(5, 5, 3, 3),
                new Room(10, 10, 3, 3),
                new Room(10, 10, 3, 3),
                new Room(10, 10, 3, 3),
                new Room(10, 10, 3, 3)};
        Room[] r2 = new Room[]{new Room(15, 5, 3, 3),
                new Room(5, 5, 3, 3),
                new Room(5, 5, 3, 3),
                new Room(5, 15, 3, 3),
                new Room(15, 5, 3, 3),
                new Room(5, 5, 3, 3),
                new Room(5, 15, 3, 3),
                new Room(15, 15, 3, 3)};
        LinkedList<AbstractMap.SimpleEntry<Integer, Integer>> expected1 = new LinkedList<>(), expected2 = new LinkedList<>();
        expected1.add(r1[0].getRight());
        expected1.add(r1[1].getTop());
        expected1.add(r1[2].getLeft());
        expected1.add(r1[3].getBottom());

        expected1.add(r1[4].getTop());
        expected1.add(r1[5].getTop());
        expected1.add(r1[6].getBottom());
        expected1.add(r1[7].getBottom());

        expected2.add(r2[0].getLeft());
        expected2.add(r2[1].getBottom());
        expected2.add(r2[2].getRight());
        expected2.add(r2[3].getTop());

        expected2.add(r2[4].getBottom());
        expected2.add(r2[5].getBottom());
        expected2.add(r2[6].getTop());
        expected2.add(r2[7].getTop());

        for (int i = 0; i < r1.length; i++) {
            Edge e = new Edge(r1[i], r2[i]);
            assertEquals(expected1.get(i), e.getDoor1(), String.format("Room 1 failed test i=%d\n", i));
            assertEquals(expected2.get(i), e.getDoor2(), String.format("Room 2 failed test i=%d\n", i));
        }
    }

}
