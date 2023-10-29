package invaders.observer;

import javafx.scene.control.Label;

public class TimerObserver implements Observer{

    private Label label;
    private Timer timer;

    public TimerObserver(Timer timer, Label label) {
        this.label = label;
        this.timer = timer;
    }

    @Override
    public void update() {
        int seconds = (int) timer.getTimer()[1];
        int minutes = (int) timer.getTimer()[0];

        if(seconds == 60) {
            minutes ++;
            timer.setTimer(new double[]{minutes, 0});
        }

        this.label.setText("Time: " + String.format("%d:%02d", minutes, seconds));
    }
}
