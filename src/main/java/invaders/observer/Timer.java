package invaders.observer;

public class Timer extends Subject{

    private double timer = 0;

    public void setTimer(double timer) {
        this.timer = timer;
    }

    public double getTimer() {
        return timer;
    }

}
