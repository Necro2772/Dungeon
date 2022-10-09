package GameLogic;

import MapLogic.Map;
import javafx.util.Pair;

public class Player {
    private Pair<Integer, Integer> loc;
    private int visibility = 20;

    public Player(Pair<Integer, Integer> start) {
        loc = start;
    }

    public Pair<Integer, Integer> getLoc() {
        return loc;
    }

    public int getVisibility() {
        return visibility;
    }

    public void move(Map map, int x, int y) {
        map.setPlayerLoc(x, y);
        loc = new Pair<>(x, y);
    }
}
