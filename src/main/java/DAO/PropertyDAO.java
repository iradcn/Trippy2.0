package DAO;

import model.Property;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nimrod on 5/29/15.
 */
public class PropertyDAO {



    private static String syncProperties = "DELETE FROM placesprops"
            + " WHERE PlaceId NOT IN (SELECT Id from places)";

    public static void Insert(Property prop) {

    }

    public static void Delete(Property prop) {

    }

    public static void SyncPlacesProperties() throws SQLException {
        Connection conn = JDBCConnection.getConnection();
        PreparedStatement deleteState = conn.prepareStatement(syncProperties);
        List<PreparedStatement> statements = new ArrayList<>();
        statements.add(deleteState);
        JDBCConnection.executeUpdate(statements,conn);
    }


}
