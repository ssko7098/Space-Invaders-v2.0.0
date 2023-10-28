package invaders.observer;

public class Timer extends Subject{

    private double seconds = 0;
    private double minutes = 0;

    public Timer() {
        //default constructor
    }

    public Timer(Timer timer) {
        this.minutes = timer.getTimer()[0];
        this.seconds = timer.getTimer()[1];
    }

    public void setTimer(double[] timer) {
        this.minutes = timer[0];
        this.seconds = timer[1];
    }

    public double[] getTimer() {
        return new double[]{minutes, seconds};
    }

}
