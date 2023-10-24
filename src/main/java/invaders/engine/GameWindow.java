package invaders.engine;

import java.util.List;
import java.util.ArrayList;

import invaders.App;
import invaders.ConfigReader;
import invaders.entities.EntityViewImpl;
import invaders.entities.SpaceBackground;
import invaders.gameobject.GameObject;
import invaders.memento.Caretaker;
import invaders.observer.*;
import invaders.singleton.Difficulty;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;

import invaders.entities.EntityView;
import invaders.rendering.Renderable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import org.json.simple.JSONObject;

public class GameWindow {
	private final int width;
    private final int height;
	private Scene scene;
    private Pane pane;
    private GameEngine model;
    private List<EntityView> entityViews =  new ArrayList<EntityView>();
    private Renderable background;

    private double xViewportOffset = 0.0;
    private double yViewportOffset = 0.0;
    // private static final double VIEWPORT_MARGIN = 280.0;

    private Score score;
    private Timer timer;

    private Caretaker caretaker;

    private Label timerLabel = new Label();
    private Label scoreLabel = new Label();

    private boolean gameOver = false;
    private Label endGameLabel = new Label();

	public GameWindow(GameEngine model){
        this.model = model;
		this.width =  model.getGameWidth();
        this.height = model.getGameHeight();
        this.timer = model.getTimer();
        this.score = model.getScore();
        this.caretaker = new Caretaker();

        pane = new Pane();
        scene = new Scene(pane, width, height + 50);
        this.background = new SpaceBackground(model, pane);

        VBox bottomBar = new VBox();
        bottomBar.setLayoutY(height - 30);

        HBox labels = new HBox();
        labels.setSpacing(70);
        labels.setPadding(new Insets(20, 0, 0, 50));

        timerLabel.setTextFill(Paint.valueOf("white"));
        timerLabel.setFont(new Font(20));
        timer.attach(new TimerObserver(timer, timerLabel));

        scoreLabel.setTextFill(Paint.valueOf("white"));
        scoreLabel.setFont(new Font(20));
        score.attach(new ScoreObserver(score, scoreLabel));


        Button redo = new Button();
        redo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try{
                    model.recoverState(caretaker.getMemento());
                }catch(NullPointerException e) {
                    System.out.println("You have no states currently saved");
                }
                caretaker.setMemento(null);
            }
        });
        redo.setText("REDO");
        redo.setFocusTraversable(false);

        ObservableList<String> difficulties = FXCollections.observableArrayList(
                "Easy",
                        "Medium",
                        "Hard"
        );

        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(difficulties));
        comboBox.setValue("Easy");
        comboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Difficulty.getInstance().setDifficulty(comboBox.getValue());
                model.resetGame((Difficulty.getInstance().getConfigPath()));
                endGameLabel.setText("");
                gameOver = false;
                caretaker = new Caretaker();
            }
        });
        comboBox.setFocusTraversable(false);

        labels.getChildren().addAll(scoreLabel, timerLabel, redo, comboBox);
        bottomBar.getChildren().addAll(labels);
        pane.getChildren().add(bottomBar);

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler(this.model);

        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);

        endGameLabel.setFont(new Font("Arial", 30));
        endGameLabel.setMinWidth(width);
        endGameLabel.setMinHeight(height);
        endGameLabel.setAlignment(Pos.CENTER);
        endGameLabel.setTextFill(Paint.valueOf("WHITE"));
        pane.getChildren().add(endGameLabel);
    }

	public void run() {
         Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17), t -> this.draw()));

         timeline.setCycleCount(Timeline.INDEFINITE);
         timeline.play();
    }


    private void draw(){
        model.update();

        timer.Notify();
        score.Notify();

        if(model.playerHasShot()) {
            caretaker.setMemento(model.saveState());
            model.playerStoppedShooting();
        }

        List<Renderable> renderables = model.getRenderables();
        for (Renderable entity : renderables) {
            boolean notFound = true;
            for (EntityView view : entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    view.update(xViewportOffset, yViewportOffset);
                    break;
                }
            }
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

        for (Renderable entity : renderables){
            if (!entity.isAlive()){
                for (EntityView entityView : entityViews){
                    if (entityView.matchesEntity(entity)){
                        entityView.markForDelete();
                        model.getPendingToRemoveRenderable().add(entity);
                    }
                }
            }
        }

        for (EntityView entityView : entityViews) {
            if (entityView.isMarkedForDelete()) {
                pane.getChildren().remove(entityView.getNode());
            }
        }


        model.getGameObjects().removeAll(model.getPendingToRemoveGameObject());
        model.getGameObjects().addAll(model.getPendingToAddGameObject());
        model.getRenderables().removeAll(model.getPendingToRemoveRenderable());
        model.getRenderables().addAll(model.getPendingToAddRenderable());

        model.getPendingToAddGameObject().clear();
        model.getPendingToRemoveGameObject().clear();
        model.getPendingToAddRenderable().clear();
        model.getPendingToRemoveRenderable().clear();

        entityViews.removeIf(EntityView::isMarkedForDelete);

        if(!model.getPlayer().isAlive() && endGameLabel.getText().isEmpty()) {
            gameOver = true;
            endGameLabel.setText("GAME OVER");
        }

        else if(model.getPlayer().isAlive() && endGameLabel.getText().isEmpty()) {
            boolean noEnemiesLeft = true;
            for(Renderable renderable : model.getRenderables()) {
                if(renderable.getRenderableObjectName().equals("Enemy")) {
                    if(renderable.isAlive()) {
                        noEnemiesLeft = false;
                    }
                }
            }

            if(noEnemiesLeft) {
                gameOver = true;
                endGameLabel.setText("PLAYER WINS!");
            }
        }

        if(gameOver) {
            model.endGame();
        }

    }

	public Scene getScene() {
        return scene;
    }
}
