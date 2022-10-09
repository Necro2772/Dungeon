package MapLogic;

import javafx.util.Pair;

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
//        if (e1.getRoom1().equals(e2.getRoom1()) || e1.getRoom1().equals(e2.getRoom2()) ||
//        e1.getRoom2().equals(e2.getRoom1()) || e1.getRoom2().equals(e2.getRoom2())) return false;

        //Untested
        if (e1.getDoor1().equals(e2.getDoor1())) {
            return sameDoorIntercept(e1.getDoor1(), e1.getDoor2(), e2.getDoor2());
        }
        if (e1.getDoor1().equals(e2.getDoor2())) {
            return sameDoorIntercept(e1.getDoor1(), e1.getDoor2(), e2.getDoor1());
        }
        if (e1.getDoor2().equals(e2.getDoor1())) {
            return sameDoorIntercept(e1.getDoor2(), e1.getDoor1(), e2.getDoor2());
        }
        if (e1.getDoor2().equals(e2.getDoor2())) {
            return sameDoorIntercept(e1.getDoor2(), e1.getDoor1(), e2.getDoor1());
        }

        return ccw(e1.getRoom1(), e2.getRoom1(), e2.getRoom2()) != ccw(e1.getRoom2(), e2.getRoom1(), e2.getRoom2()) &&
                ccw(e2.getRoom1(), e1.getRoom1(), e1.getRoom2()) != ccw(e2.getRoom2(), e1.getRoom1(), e1.getRoom2());
    }

    // Helper methods
    private static boolean sameDoorIntercept(Pair<Integer, Integer> overlap, Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
        double v1x = p1.getKey() - overlap.getKey();
        double v1y = overlap.getValue() - p1.getValue();
        double v2x = p2.getKey() - overlap.getKey();
        double v2y = overlap.getValue() - p2.getValue();

        double mag1 = Math.hypot(v1x, v1y);
        double mag2 = Math.hypot(v2x, v2y);

        v1x /= mag1;
        v1y /= mag1;
        v2x /= mag2;
        v2y /= mag2;

        double dot = v1x * v2x + v1y * v2y;
        return Math.abs(dot) > 0.8;
    }

    private static boolean ccw(Room a, Room b, Room c) {
        return (c.getY() - a.getY()) * (b.getX() - a.getX()) > (b.getY() - a.getY()) * (c.getX() - a.getX()); // positive triangle area
    }

    public static boolean pathExists(int[][] m, int x1, int y1, int x2, int y2) {
        if (m[y1][x1] == Map.WALL || m[y2][x2] == Map.WALL) return false;
        if (x1 == y1 && x2 == y2) return true;
        Stack<Pair<Integer, Integer>> found = new Stack<>();
        ArrayList<Pair<Integer, Integer>> searched = new ArrayList<>();
        found.push(new Pair<>(x1, y1));
        while (!found.isEmpty()) {
            Pair<Integer, Integer> c = found.pop();
            if (c.equals(new Pair<>(x2, y2))) return true;
            searched.add(c);
            for (Pair<Integer, Integer> item : neighbors(c, m)) {
                if (!searched.contains(item) && !found.contains(item)) found.push(item);
            }
        }
        return false;
    }

    private static ArrayList<Pair<Integer, Integer>> neighbors(Pair<Integer, Integer> current, int[][] m) {
        ArrayList<Pair<Integer, Integer>> out = new ArrayList<>();
        for (int x = current.getKey() - 1; x <= current.getKey() + 1; x++) {
            for (int y = current.getValue() - 1; y <= current.getValue() + 1; y++) {
                if ((x != current.getKey() || y != current.getValue()) && x >= 0 && y >= 0 && x < m[0].length && y < m.length) {
                    if (m[y][x] != Map.WALL) out.add(new Pair<>(x, y));
                }
            }
        }
        return out;
    }
}
