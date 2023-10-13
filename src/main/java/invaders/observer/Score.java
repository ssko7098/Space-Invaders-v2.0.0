package invaders.observer;

public class Score extends Subject{

    private int score = 0;

    public void addScore(int point) {
        score += point;
    }

    public int getScore() {
        return score;
    }

}
