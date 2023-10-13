package invaders.observer;

import javafx.scene.control.Label;

public class TimerObserver implements Observer{

    private Label label;
    private Timer timer;
    private int minutes;
    private int seconds;

    public TimerObserver(Timer timer, Label label) {
        this.label = label;
        this.timer = timer;
    }

    @Override
    public void update() {
        seconds = (int) timer.getTimer();

        if(seconds == 60) {
            timer.setTimer(0);
            minutes ++;
        }

        this.label.setText("Time: " + String.format("%d:%02d", minutes, seconds));
    }
}
