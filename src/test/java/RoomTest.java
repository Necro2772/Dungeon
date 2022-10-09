import MapLogic.Room;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {
    @Test
    public void containsTest() {
        Room r = new Room(3, 3, 5, 5);
        assertFalse(r.contains(2, 2));
        assertFalse(r.contains(8, 8));

        assertTrue(r.contains(3, 3));
        assertTrue(r.contains(7, 7));

        assertTrue(r.contains(3, 7));
        assertFalse(r.contains(7, 8));
    }

    @Test
    public void sidesTest() {
        testSides(new int[]{6, 6, 4, 8},
                new int[]{4, 8, 6, 6},
                new Room(5, 5, 3, 3));

        testSides(new int[]{12, 12, 9, 15},
                new int[]{9, 15, 12, 12},
                new Room(10, 10, 5, 5));

    }

    private void testSides(int[] expectedX, int[] expectedY, Room r) {
        for (int i = 0; i < expectedX.length; i++) {
            Pair<Integer, Integer> out = switch (i) {
                case 0 -> r.getTop();
                case 1 -> r.getBottom();
                case 2 -> r.getLeft();
                case 3 -> r.getRight();
                default -> null;
            };
            assertEquals(out, new Pair<>(expectedX[i], expectedY[i]));
        }
    }
}