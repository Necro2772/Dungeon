package UI;

import MapLogic.Map;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GridSquare extends StackPane {
    private final Rectangle r;
    public GridSquare(int x, int y, double width, double height, int type) {
        r = new Rectangle(x*10, y*10, width, height);

        r.setStroke(Color.BLACK);

        setType(type);

        r.setTranslateX(x*10);
        r.setTranslateY(y*10);

        getChildren().add(r);
    }

    public void setType(int type) {
        setFillBorder(Color.BLACK, Color.BLACK);

        switch (type) {
            case Map.WALL -> r.setFill(Color.BLACK);
            case Map.ROOM, Map.PATH -> r.setFill(Color.LIGHTBLUE);
            case Map.EXIT -> r.setFill(Color.GREEN);
            case Map.PLAYER -> r.setFill(Color.YELLOW);
            case Map.ENEMY -> r.setFill(Color.RED);
            case Map.MOVING_ENEMY -> r.setFill(Color.DARKRED);
            case Map.WANDERING_ENEMY -> r.setFill(Color.ORANGERED);
            case Map.FOG -> setFillBorder(Color.DARKGRAY, Color.DARKGRAY);
            case Map.VISION_TILE -> r.setFill(Color.PURPLE);
        }
    }

    private void setFillBorder(Color fill, Color border) {
        r.setFill(fill);
        r.setStroke(border);
    }
}
