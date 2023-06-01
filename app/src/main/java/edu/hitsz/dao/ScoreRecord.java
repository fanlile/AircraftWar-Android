package edu.hitsz.dao;

/**
 * @author fll
 */
public class ScoreRecord {
    private String userName;
    private int score;
    private String time;
    public ScoreRecord(String userName, int score, String time) {
        this.userName = userName;
        this.score = score;
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }
    public int getScore() {
        return score;
    }
    public String getTime() {
        return time;
    }
}
