package invaders.observer;

public class Score extends Subject{

    private int score = 0;

    public Score () {
        // default constructor
    }

    public Score(Score score) {
        this.score = score.getScore();
    }

    public void addScore(int point) {
        score += point;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

}
