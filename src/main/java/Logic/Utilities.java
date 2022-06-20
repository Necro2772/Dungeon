package Logic;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Stack;

public class Utilities {
    public static void printMap(Map m) {
        int[][] grid = m.getGrid();
        System.out.println();
        for (int[] y : grid) {
            for (int x : y) {
                System.out.print(x);
            }
            System.out.println();
        }
    }

    public static void printMapWithAxes(Map m) {
        int[][] grid = m.getGrid();
        System.out.print("--");
        for (int i = 0; i < grid[0].length; i++) {
            System.out.print(i % 10);
        }
        System.out.println();
        for (int i = 0; i < grid.length; i++) {
            int[] y = grid[i];
            System.out.print(i % 10 + " ");
            for (int x : y) {
                System.out.print(x);
            }
            System.out.println();
        }
    }

    public static boolean willIntercept(Edge e1, Edge e2) {
        if (e1.getMaxY() <= e2.getMinY() || e2.getMaxY() <= e1.getMinY() ||
        e1.getMaxX() <= e2.getMinX() || e2.getMaxX() <= e1.getMinX()) return false;
        if (e1.getRoom1().equals(e2.getRoom1()) || e1.getRoom1().equals(e2.getRoom2()) ||
        e1.getRoom2().equals(e2.getRoom1()) || e1.getRoom2().equals(e2.getRoom2())) return false;

        return ccw(e1.getRoom1(), e2.getRoom1(), e2.getRoom2()) != ccw(e1.getRoom2(), e2.getRoom1(), e2.getRoom2()) &&
                ccw(e2.getRoom1(), e1.getRoom1(), e1.getRoom2()) != ccw(e2.getRoom2(), e1.getRoom1(), e1.getRoom2());
    }

    // Helper methods

    private static boolean ccw(Room a, Room b, Room c) {
        return (c.getY() - a.getY()) * (b.getX() - a.getX()) > (b.getY() - a.getY()) * (c.getX() - a.getX()); // positive triangle area
    }

    public static boolean pathExists(int[][] m, int x1, int y1, int x2, int y2) {
        if (m[y1][x1] == Map.WALL || m[y2][x2] == Map.WALL) return false;
        if (x1 == y1 && x2 == y2) return true;
        Stack<AbstractMap.SimpleEntry<Integer, Integer>> found = new Stack<>();
        ArrayList<AbstractMap.SimpleEntry<Integer, Integer>> searched = new ArrayList<>();
        found.push(new AbstractMap.SimpleEntry<>(x1, y1));
        while (!found.isEmpty()) {
            AbstractMap.SimpleEntry<Integer, Integer> c = found.pop();
            if (c.equals(new AbstractMap.SimpleEntry<>(x2, y2))) return true;
            searched.add(c);
            for (AbstractMap.SimpleEntry<Integer, Integer> item : neighbors(c, m)) {
                if (!searched.contains(item) && !found.contains(item)) found.push(item);
            }
        }
        return false;
    }

    private static ArrayList<AbstractMap.SimpleEntry<Integer, Integer>> neighbors(AbstractMap.SimpleEntry<Integer, Integer> current, int[][] m) {
        ArrayList<AbstractMap.SimpleEntry<Integer, Integer>> out = new ArrayList<>();
        for (int x = current.getKey() - 1; x <= current.getKey() + 1; x++) {
            for (int y = current.getValue() - 1; y <= current.getValue() + 1; y++) {
                if ((x != current.getKey() || y != current.getValue()) && x >= 0 && y >= 0 && x < m[0].length && y < m.length) {
                    if (m[y][x] != Map.WALL) out.add(new AbstractMap.SimpleEntry<>(x, y));
                }
            }
        }
        return out;
    }
}
