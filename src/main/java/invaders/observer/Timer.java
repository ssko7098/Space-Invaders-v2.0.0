package invaders.observer;

public class Timer extends Subject{

    private double timer = 0;

    public Timer() {
        //default constructor
    }

    public Timer(Timer timer) {
        this.timer = timer.getTimer();
    }

    public void setTimer(double timer) {
        this.timer = timer;
    }

    public double getTimer() {
        return timer;
    }

}
