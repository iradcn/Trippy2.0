package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Property;

/**
 * Created by nimrod on 5/29/15.
 */
public class PropertyDAO {

    private static String insertProperty = "INSERT property (`name`) VALUES(?)";
    //private static String selectProperty = "SELECT * FROM property WHERE name=?";
    //private static String selectPropertyById = "SELECT * FROM property WHERE id=?";
    private static String updateProperty = "UPDATE property SET (`name`=?) WHERE `id`=?";
    private static String syncProperties = "DELETE FROM placesprops"
            + " WHERE PlaceId NOT IN (SELECT Id from places)";
    private static String deleteProperty = "DELETE FROM property WHERE `id`=?";

    public static void Insert(Property prop) throws SQLException {
        Connection conn = JDBCConnection.getConnection();
        PreparedStatement insertState = conn.prepareStatement(insertProperty);
        List<PreparedStatement> statements = new ArrayList<>();
        insertState.setString(1, prop.getName());
        statements.add(insertState);
        JDBCConnection.executeUpdate(statements, conn);
    }

    public static void SyncPlacesProperties() throws SQLException {
        Connection conn = JDBCConnection.getConnection();
        PreparedStatement deleteState = conn.prepareStatement(syncProperties);
        List<PreparedStatement> statements = new ArrayList<>();
        statements.add(deleteState);
        JDBCConnection.executeUpdate(statements,conn);
    }

    public static void Update(Property prop) throws SQLException {
        Connection conn = JDBCConnection.getConnection();
        PreparedStatement updateState = conn.prepareStatement(updateProperty);
        List<PreparedStatement> statements = new ArrayList<>();
        updateState.setString(1, prop.getName());
        updateState.setInt(1, prop.getId());
        statements.add(updateState);
        JDBCConnection.executeUpdate(statements, conn);
    }
    public static void Delete(Property prop) throws SQLException {
        Connection conn = JDBCConnection.getConnection();
        PreparedStatement deleteState = conn.prepareStatement(deleteProperty);
        List<PreparedStatement> statements = new ArrayList<>();
        deleteState.setInt(1, prop.getId());
        statements.add(deleteState);
        JDBCConnection.executeUpdate(statements, conn);
    }
    
}
