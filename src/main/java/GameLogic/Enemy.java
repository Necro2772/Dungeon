package GameLogic;

import MapLogic.Map;
import javafx.util.Pair;

public interface Enemy {
    Pair<Integer, Integer> getPosition();
    EnemyState getState();
    void setPosition(Pair<Integer, Integer> position);

    /**
     * performs one turn for the enemy
     * @param map map to use for determining movement
     */
    void move(Map map);

    /**
     * checks surrounding area for player based on visibility
     * @param map map to use to determine destination from
     */
    void look(Map map);

    /**
     * performs look in a square
     * @param map map to use to determine destination from
     */
    void lookSq(Map map);

    void alert(Pair<Integer, Integer> destination);

    void call();
}
