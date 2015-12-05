package model;

import protocol_model.SearchByLocation;

/**
 * Created by nimrodoron on 12/5/15.
 */
public class VoteAnswer extends Vote {
    private int[] answers = new int[3];
    private SearchByLocation query;

    public SearchByLocation getQuery() {
        return query;
    }

    public void setQuery(SearchByLocation query) {
        this.query = query;
    }

    public int[] getAnswers() {
        return answers;
    }

    public void setAnswers(int[] answers) {
        this.answers = answers;
    }
}
