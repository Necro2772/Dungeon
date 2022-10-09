import MapLogic.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapTest {
    @Test
    public void addRoomTest(){
        Map m = new MapBuilder().setSize(10, 10)
                .addRoom(2, 2, 3, 3).get();
        //Utilities.printMap(m);
        assertEquals(m.getGrid()[0][0], Map.WALL);
        assertEquals(m.getGrid()[2][2], Map.ROOM);
        assertEquals(m.getGrid()[4][4], Map.ROOM);
        assertEquals(m.getGrid()[5][5], Map.WALL);
    }

    @Test
    public void drawLineTest() {
        Map m = new Map(15, 15);
        int[] x1Values;
        int[] y1Values;
        int[] x2Values;
        int[] y2Values;
        // Diagonals, v2 > v1
        x1Values = new int[]{0, 3, 0, 3};
        y1Values = new int[]{0, 10, 10, 0};
        x2Values = new int[]{3, 0, 3, 0};
        y2Values = new int[]{10, 0, 0, 10};
        for (int i = 0; i < x1Values.length; i++) {
            testLine(m, x1Values[i], y1Values[i], x2Values[i], y2Values[i]);
        }
        // Diagonals, v1 > v2
        x1Values = new int[]{0, 10, 0, 10};
        y1Values = new int[]{0, 3, 3, 0};
        x2Values = new int[]{10, 0, 10, 0};
        y2Values = new int[]{3, 0, 0, 3};
        for (int i = 0; i < x1Values.length; i++) {
            testLine(m, x1Values[i], y1Values[i], x2Values[i], y2Values[i]);
        }
        // Straight lines, v1 == 0 || v2 == 0
        x1Values = new int[]{0, 0, 10, 0};
        y1Values = new int[]{0, 0, 0, 10};
        x2Values = new int[]{10, 0, 0, 0};
        y2Values = new int[]{0, 10, 0, 0};
        for (int i = 0; i < x1Values.length; i++) {
            testLine(m, x1Values[i], y1Values[i], x2Values[i], y2Values[i]);
        }
    }

    public static void testLine(Map m, int x1, int y1, int x2, int y2) {
        Room r1 = new Room(x1, y1, 3, 3), r2 = new Room(x2, y2, 3, 3);
        Edge e = new Edge(r1, r2);
        m.drawLine(e);
        m.drawRoom(r1);
        m.drawRoom(r2);
        Utilities.printMap(m);
        assertEquals(m.getGrid()[e.getDoor1().getValue()][e.getDoor1().getKey()], Map.PATH);
        assertEquals(m.getGrid()[e.getDoor2().getValue()][e.getDoor2().getKey()], Map.PATH);
        assertTrue(Utilities.pathExists(m.getGrid(), x1, y1, x2, y2));
        m.clear();
    }
}
