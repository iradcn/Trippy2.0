package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Place;
import model.User;

public class UserDAO {
    private static String AddUserToDB = "Insert into users (`user_id`,`password`,`enabled`,`access_token`) values (?,?,?,?)";
    private static String UpdateUser = "UPDATE users SET `access_token`=?, `password`=? WHERE `user_id`=?";
    private static String InsertRole = "Insert into user_roles (`user_id`,`role`) values (?, 'USER')";
    private static String UserExists = "SELECT `user_id` from users where `user_id`=?";
    private static String InsertCheckIn = "INSERT INTO users_check_in (`user_id`,`place_id`) VALUES (?,?)";
    private static String FindCheckIn = "SELECT place_id FROM users_check_in WHERE user_id=?";
    private static String incrementSentDataCounter = "UPDATE users set `sent_data_counter`=`sent_data_counter`+1 where `user_id`=?"; 
    private static String incrementRelCounter = "UPDATE users set `rel_counter`=`rel_counter`+1 where `user_id`=?"; 
    private static String decrementRelCounter = "UPDATE users set `rel_counter`=`rel_counter`-1 where `user_id`=?"; 
    private static String getSentDataCounterByUserId = "SELECT * FROM users where user_id = ?";
    
    public static void Insert(User user) throws SQLException {
    	List<PreparedStatement> list = new ArrayList<>();
        Connection conn = JDBCConnection.getConnection();
        PreparedStatement insertState = conn.prepareStatement(AddUserToDB);
        insertState.setString(1, user.getUserId());
        insertState.setString(2, user.getPassword());
        insertState.setLong(3, user.getIsEnabled());
        insertState.setString(4, user.getAccessToken());
        
        PreparedStatement insertRoleState = conn.prepareStatement(InsertRole);
        insertRoleState.setString(1, user.getUserId());
        
        list.add(insertState);
        list.add(insertRoleState);
        
        JDBCConnection.executeUpdate(list, conn);
    }

    public static void Update(User user) throws SQLException {
    	List<PreparedStatement> list = new ArrayList<>();
        Connection conn = JDBCConnection.getConnection();
        PreparedStatement updateState = conn.prepareStatement(UpdateUser);
        updateState.setString(1, user.getAccessToken());
        updateState.setString(2, user.getPassword());
        updateState.setString(3, user.getUserId());
        list.add(updateState);
        JDBCConnection.executeUpdate(list, conn);
    }
    
    public static void createOrUpdate(User user) throws SQLException {

		Connection conn = JDBCConnection.getConnection();
		PreparedStatement stmt = conn.prepareStatement(UserExists);
		stmt.setString(1, user.getUserId());
		ResultSet rs = stmt.executeQuery();
		JDBCConnection.closeConnection(conn);

        if (!rs.next()){
            Insert(user);
        } else {
            Update(user);
        }
    }

	public static void InsertUserPlace(List<Place> confirmedCandidates,
			User user) throws SQLException {
		
		Connection conn = JDBCConnection.getConnection();
		List<PreparedStatement> lst = new ArrayList<PreparedStatement>();
		for (Place place : confirmedCandidates) {
			PreparedStatement stmt = conn.prepareStatement(InsertCheckIn);
			stmt.setString(1, user.getUserId());
			stmt.setString(2, place.getGoogleId());
			lst.add(stmt);
		}
		JDBCConnection.executeUpdate(lst, conn);
	}
	
	public static void IncrementUserNumSearches (String userId) throws SQLException {
		Connection conn = JDBCConnection.getConnection();
		List<PreparedStatement> lst = new ArrayList<PreparedStatement>();
		PreparedStatement stmt = conn.prepareStatement(incrementSentDataCounter);
		stmt.setString(1, userId);
		lst.add(stmt);
		JDBCConnection.executeUpdate(lst, conn);
	}
	
	public static void IncrementUserRelCounter (String userId) throws SQLException {
		Connection conn = JDBCConnection.getConnection();
		List<PreparedStatement> lst = new ArrayList<PreparedStatement>();
		PreparedStatement stmt = conn.prepareStatement(incrementRelCounter);
		stmt.setString(1, userId);
		lst.add(stmt);
		JDBCConnection.executeUpdate(lst, conn);
	}
	
	public static void DecrementUserRelCounter (String userId) throws SQLException {
		Connection conn = JDBCConnection.getConnection();
		List<PreparedStatement> lst = new ArrayList<PreparedStatement>();
		PreparedStatement stmt = conn.prepareStatement(decrementRelCounter);
		stmt.setString(1, userId);
		lst.add(stmt);
		JDBCConnection.executeUpdate(lst, conn);
	}
	
	public static int getSentDataCounter(String UserId) throws SQLException{
		Connection conn = JDBCConnection.getConnection();
		if (conn == null)  throw new SQLException();

		PreparedStatement ps = conn.prepareStatement(getSentDataCounterByUserId);
		ps.setString(1, UserId);
		ResultSet rs = JDBCConnection.executeQuery(ps, conn);
		if (rs.next()) {
			return rs.getInt("sent_data_counter");
		}
		return -1;
		
	}

    public static Set<String> getUserCheckInPlaces(String userId) throws SQLException {
        Set<String> placesIds = new HashSet<>();

        Connection conn = JDBCConnection.getConnection();
        if (conn == null)  throw new SQLException();
        PreparedStatement ps = conn.prepareStatement(FindCheckIn);
        ps.setString(1, userId);
        ResultSet rs = JDBCConnection.executeQuery(ps, conn);

        while (rs.next()) {
            placesIds.add(rs.getString("place_id"));
        }
        return placesIds;
    }

}
