package model;

/**
 * Created by nimrodoron on 12/5/15.
 */
public class VoteAnswer extends Vote {
    private int[] answers = new int[3];

    public int[] getAnswers() {
        return answers;
    }

    public void setAnswers(int[] answers) {
        this.answers = answers;
    }
}
