package invaders;

import invaders.singleton.Difficulty;
import javafx.application.Application;
import javafx.stage.Stage;
import invaders.engine.GameEngine;
import invaders.engine.GameWindow;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GameEngine model = new GameEngine(Difficulty.getInstance().getConfigPath());
        GameWindow window = new GameWindow(model);
        window.run();

        primaryStage.setTitle("Space Invaders");
        primaryStage.setScene(window.getScene());
        primaryStage.show();

        window.run();
    }
}
