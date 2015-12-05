package model;

/**
 * Created by nimrodoron on 12/5/15.
 */
public enum EnumVoteValue {
    VOTE_YES(1),
    VOTE_NOT (-1),
    VOTE_IGNORE(0);

    private int val;

    private EnumVoteValue(int val) {
        this.val = val;
    }

    public static boolean isLegalValue(int voteVal) {
        if (voteVal == VOTE_YES.val || voteVal == VOTE_NOT.val || voteVal == VOTE_IGNORE.val) {
            return true;
        }
        return false;
    }
}
