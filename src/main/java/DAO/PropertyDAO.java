package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Category;
import model.Place;
import model.Property;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by nimrod on 5/29/15.
 */
public class PropertyDAO {

    private static String insertProperty = "INSERT INTO properties (`name`) VALUES(?)";
    //private static String selectProperty = "SELECT * FROM property WHERE name=?";
    //private static String selectPropertyById = "SELECT * FROM property WHERE id=?";
    private static String updateProperty = "UPDATE properties SET `name`=? WHERE `id`=?";
    private static String syncProperties = "DELETE FROM places_props"
            + " WHERE PlaceId NOT IN (SELECT Id from places)";
    private static String deleteProperty = "DELETE FROM properties WHERE `id`=?";
    private static String deletePropPlaces = "DELETE FROM places_props WHERE 'PropId'=?";
    private static String getAllProperties = "SELECT * FROM properties";
    private static String InsertPropToPlace = "INSERT INTO places_props (PlaceId,PropId) Values(?,?)";
    private static String DeletePropFromPlace = "DELETE FROM places_props WHERE PlaceId=? AND PropId=?";
    private static String GetPropertyById = "SELECT * FROM properties where Id=?";
    private static String GetMostPopPropInCat = "SELECT ppv.propId, p.Name " +
                                                "FROM places_props_view ppv, places_categories pc, properties p " +
                                                "WHERE ppv.placeId = pc.placeId AND " +
                                                "p.Id = ppv.propId AND " +
                                                "pc.CategoryId in (select CategoryId from places_categories where placeId = ?) " +
                                                "GROUP BY ppv.placeId, ppv.propId, p.Name " +
                                                "HAVING ppv.propId NOT IN " +
                                                "(SELECT distinct(p.Id) " +
                                                "FROM properties p, uservotes uv " +
                                                "WHERE p.Id = uv.PropId AND uv.vote = 0 AND uv.userId = ?) " +
                                                "ORDER BY sum(ppv.rank)";
    private static String InsertPropSearch = "INSERT INTO users_search_props_history VALUES(?,?)";
    private static String getPopullarSearchProp = "SELECT us.prop_id, p.Name, count(*) c " +
                                                  "FROM users_search_props_history us, properties p " +
                                                  "WHERE p.Id = us.prop_id " +
                                                  "AND us.user_id = ? " +
                                                  "GROUP BY us.prop_id, us.user_id " +
                                                  "ORDER BY c DESC " +
                                                  "LIMIT 1";

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
        updateState.setInt(2, prop.getId());
        statements.add(updateState);
        JDBCConnection.executeUpdate(statements, conn);
    }
    public static void Delete(Property prop) throws SQLException {
        Connection conn = JDBCConnection.getConnection();
        PreparedStatement deletePropPlace = conn.prepareStatement(deletePropPlaces);
        PreparedStatement deleteState = conn.prepareStatement(deleteProperty);
        List<PreparedStatement> statements = new ArrayList<>();
        deletePropPlace.setInt(1, prop.getId());
        deleteState.setInt(1, prop.getId());
        statements.add(deleteState);
        statements.add(deletePropPlace);
        JDBCConnection.executeUpdate(statements, conn);
    }
    public static List<Property> getAll() throws SQLException {
        Connection conn = JDBCConnection.getConnection();
        PreparedStatement deletePropPlace = conn.prepareStatement(getAllProperties);
        ResultSet rs = JDBCConnection.executeQuery(deletePropPlace, conn);
        List<Property> props = new ArrayList<>();
        while (rs.next()) {
            Property prop = new Property(rs.getInt("Id"));
            prop.setName(rs.getString("Name"));
            props.add(prop);
        }
        return props;
    }

    public static Property getPropById(int id) throws SQLException {
        Connection conn = JDBCConnection.getConnection();
        PreparedStatement getProp = conn.prepareStatement(GetPropertyById);
        getProp.setInt(1, id);
        ResultSet rs = JDBCConnection.executeQuery(getProp, conn);
        List<Property> props = new ArrayList<>();
        while (rs.next()) {
            Property prop = new Property(rs.getInt("Id"));
            prop.setName(rs.getString("Name"));
            return prop;
        }
        return null;
    }

    public static void AddPropToPlace(Place placeToAdd) throws SQLException {
        Connection conn = JDBCConnection.getConnection();
        PreparedStatement addPropPlace = conn.prepareStatement(InsertPropToPlace);
        addPropPlace.setString(1, placeToAdd.getGoogleId());
        addPropPlace.setInt(2, placeToAdd.getProperties().iterator().next().getId());
        List<PreparedStatement> statements = new ArrayList<>();
        statements.add(addPropPlace);
        JDBCConnection.executeUpdate(statements,conn);
    }

    public static void RemovePropFromPlace(Place placeToAdd) throws SQLException {
        Connection conn = JDBCConnection.getConnection();
        PreparedStatement addPropPlace = conn.prepareStatement(DeletePropFromPlace);
        addPropPlace.setString(1, placeToAdd.getGoogleId());
        addPropPlace.setInt(2, placeToAdd.getProperties().iterator().next().getId());
        List<PreparedStatement> statements = new ArrayList<>();
        statements.add(addPropPlace);
        JDBCConnection.executeUpdate(statements,conn);
    }

    public static List<Property> GetPopularPropertiesForCategory(String PlaceId, String userId) throws SQLException {
        Connection conn = JDBCConnection.getConnection();
        PreparedStatement deletePropPlace = conn.prepareStatement(GetMostPopPropInCat);
        deletePropPlace.setString(1, PlaceId);
        deletePropPlace.setString(2, userId);
        ResultSet rs = JDBCConnection.executeQuery(deletePropPlace, conn);
        List<Property> props = new ArrayList<>();
        while (rs.next()) {
            Property prop = new Property(rs.getInt("propId"));
            prop.setName(rs.getString("Name"));
            props.add(prop);
        }
        return props;
    }

    public static void savePropsSearch(List<Property> properties) throws SQLException {
        if (properties == null || properties.size() == 0) return;
        Connection conn = JDBCConnection.getConnection();
        PreparedStatement insertPropSearch = conn.prepareStatement(InsertPropSearch);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();;
        for (Property prop : properties) {
            insertPropSearch.setString(1, userId);
            insertPropSearch.setInt(2, prop.getId());
            insertPropSearch.addBatch();
        }
        List<PreparedStatement> statements = new ArrayList<PreparedStatement>();
        statements.add(insertPropSearch);
        JDBCConnection.executeBatch(statements,conn);
    }

    public static  Property GetPopularPropertyInSearch(String userId) throws SQLException {
        Connection conn = JDBCConnection.getConnection();
        PreparedStatement getProp = conn.prepareStatement(getPopullarSearchProp);
        getProp.setString(1, userId);
        ResultSet rs = JDBCConnection.executeQuery(getProp, conn);
        while (rs.next()) {
            Property prop = new Property(rs.getInt("prop_id"));
            prop.setName(rs.getString("Name"));
            return prop;
        }
        return null;
    }


}
