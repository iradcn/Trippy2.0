package DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.springframework.security.core.context.SecurityContextHolder;

import model.Property;
import model.Vote;

/**
 * Created by nimrodoron on 11/28/15.
 */
public class VoteDAO {
    private static String InsertVotePropToPlace = "INSERT INTO uservotes (userId, placeId, propId, vote, fTimestamp, nPlaceId) Values(?,?,?,?,?,?)";
    private static String selectOpenQuestion = "SELECT uservotes.nPlaceId, uservotes.placeId, uservotes.propId, properties.Name FROM uservotes,properties "+ 
    											"WHERE uservotes.is_opened = 1 and uservotes.userId =? and uservotes.propId = properties.Id";
    private static String voteQuestion = "UPDATE uservotes set `vote`=?,`is_opened`=0 WHERE `userId`=? and `propId`=?";
    private static String GetPlacesUserVote = "SELECT placeId FROM uservotes WHERE userId = ? GROUP BY placeId";
    public static void insertNewQuestion(int propId, String placeId, int voteValue, String userId, long nPlaceId) throws SQLException {
        Connection conn = JDBCConnection.getConnection();
        PreparedStatement addVotePropPlace = conn.prepareStatement(InsertVotePropToPlace);
        addVotePropPlace.setString(1, userId);
        addVotePropPlace.setString(2, placeId);
        addVotePropPlace.setInt(3, propId);
        addVotePropPlace.setInt(4, voteValue);

        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        addVotePropPlace.setDate(5, date);
        addVotePropPlace.setLong(6, nPlaceId);
        List<PreparedStatement> statements = new ArrayList<>();
        statements.add(addVotePropPlace);
        JDBCConnection.executeUpdate(statements,conn);
    }
    
	public static Vote getOpenQuestion() throws SQLException {
		
		Property[] property = new Property[3];
		String placeId;
		
		Connection conn = JDBCConnection.getConnection();
		if (conn == null)  throw new SQLException();
	
		PreparedStatement ps = conn.prepareStatement(selectOpenQuestion);
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		ps.setString(1, userId);
		ResultSet rs = JDBCConnection.executeQuery(ps, conn);
		
		Vote openQuestionVote = null;
		
		if (!rs.next())
			return null;
		
		for(int i=0; i< 3 ; i++){
				property[i] =new Property( rs.getInt("propId"), rs.getString("Name"));
				if (!rs.next())
					break;
		}
		
		placeId = rs.getString("placeId");
		openQuestionVote = new Vote(placeId,PlaceDAO.getPlaceNameByPlaceId(placeId), property, rs.getLong("nPlaceId"));
		
		return openQuestionVote;
	}

    public static void setQuestionAsAnswered(int propId, String placeId, int voteValue, String username, long nId) throws SQLException {
		Connection conn = JDBCConnection.getConnection();
		List<PreparedStatement> psLst = new ArrayList<>();
		PreparedStatement ps = conn.prepareStatement(voteQuestion);
		ps.setInt(1, voteValue);
		ps.setString(2,username);
		ps.setInt(3, propId);
		psLst.add(ps);
		JDBCConnection.executeUpdate(psLst, conn);
    }

    public static Set<String> getPlacesUserVotes(String userId) throws SQLException {
        Set<String> placesIds = new HashSet<>();

        Connection conn = JDBCConnection.getConnection();
        if (conn == null)  throw new SQLException();
        PreparedStatement ps = conn.prepareStatement(GetPlacesUserVote);
        ps.setString(1, userId);
        ResultSet rs = JDBCConnection.executeQuery(ps, conn);

        while (rs.next()) {
            placesIds.add(rs.getString("placeId"));
        }
        return placesIds;
    }
}
