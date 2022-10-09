package MapLogic;

import GameLogic.Enemy;
import GameLogic.Game;
import GameLogic.Player;

import java.util.ArrayList;

public interface PopulateMap {
    Player generatePlayer(Map map);
    ArrayList<Enemy> generateEnemies(Map map, Game game);

    void generateMap(Map map);
}
