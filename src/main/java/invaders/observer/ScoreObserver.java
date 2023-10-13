package invaders.observer;

import javafx.scene.control.Label;

public class ScoreObserver implements Observer{

    private Label label;
    private Score score;

    public ScoreObserver(Score score, Label label) {
        this.label = label;
        this.score = score;
    }

    @Override
    public void update() {
        this.label.setText("Score: " + score.getScore());
    }
}
