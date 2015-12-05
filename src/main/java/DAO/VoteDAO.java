package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by nimrodoron on 11/28/15.
 */
public class VoteDAO {
    private static String InsertVotePropToPlace = "INSERT INTO UserVotes (userId, placeId, propId, vote, fTimestamp, placenid) Values(?,?,?,?,?,?)";

    public static void insertVoteAnswer(int propId, String placeId, int voteValue, String username, long nId) throws SQLException {
        Connection conn = JDBCConnection.getConnection();
        PreparedStatement addVotePropPlace = conn.prepareStatement(InsertVotePropToPlace);
        addVotePropPlace.setString(1, username);
        addVotePropPlace.setString(2, placeId);
        addVotePropPlace.setInt(3, propId);
        addVotePropPlace.setInt(4, voteValue);

        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        addVotePropPlace.setDate(5, date);
        addVotePropPlace.setLong(6, nId);
        List<PreparedStatement> statements = new ArrayList<>();
        statements.add(addVotePropPlace);
        JDBCConnection.executeUpdate(statements,conn);
    }
}
