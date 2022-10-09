package UI;

import GameLogic.Game;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class Main extends Application{

    @Override
    public void start(Stage stage) {
        Game game = new Game(stage);

        Scene scene = new Scene(game.getMapUI().getRoot(), game.getMapUI().getWidth() * 10 + 100, 800);
        scene.setFill(Paint.valueOf("BLACK"));

        stage.setTitle("Dungeon");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
