package invaders.observer;

import javafx.scene.control.Label;

import java.text.DecimalFormat;

public class TimerObserver implements Observer{

    private Label label;
    private Timer timer;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public TimerObserver(Timer timer, Label label) {
        this.label = label;
        this.timer = timer;
    }

    @Override
    public void update() {
        this.label.setText("Time: " + df.format(timer.getTimer()));
    }
}
