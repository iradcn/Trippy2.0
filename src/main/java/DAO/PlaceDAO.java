package DAO;

import java.sql.*;
import java.util.*;

import model.Category;
import model.Location;
import model.Place;
import model.Property;
import protocol_model.SearchByLocation;

/**
 * Created by nimrod on 5/24/15.
 */
public class PlaceDAO {

	private static final String DROP_FOREIGN_KEYS_STORED_PROC = "{call RemoveForeignKeys()}";
	private static final String CREATE_FOREIGN_KEYS_STORED_PROC = "{call CreateForeignKeys()}";
	private static String collectionEmpty = "OR 1=1";
	private static String insertPlacesSQL = "INSERT INTO places"
			+ " (`id`,`name`,`lat`,`lon`) VALUES"
			+ "(?,?,?,?) ON DUPLICATE KEY UPDATE `id`=`id`";

	private static String insertCategoriesSQL = "INSERT INTO placescategories"
			+ " (`placeid`,`categoryid`) VALUES"
			+ "(?,?) ON DUPLICATE KEY UPDATE `PlaceId`=`PlaceId`";

	private static String getPlacesByLocation = "SELECT  p.name, p.lat, p.lon, p.id, pc.CategoryId, pp.PropId " +
												"FROM places p " +
												     "LEFT OUTER JOIN placescategories pc ON p.id = pc.placeid " +
													 "LEFT OUTER JOIN placesprops pp ON pp.placeId = p.ID " +
												"WHERE "+
													"3956 * 2 * ASIN(SQRT(POWER(SIN((? - p.lat) * PI() / 180 / 2), " +
													"2) + COS(? * PI() / 180) * COS(p.lat * PI() / 180) * " +
													"POWER(SIN((? - p.lon) * PI() / 180 / 2),2))) < ? " +
													"AND (pc.CategoryId IN (%s) %s) " +
													"AND (pp.PropId IN (%s) %s)";



	private static String deletePlacesSQL = "DELETE from places";
	private static String deletePlacesCatsSQL = "DELETE from placescategories";

	public static void SavePlacesAndPlaceCats(List<Place> places) throws SQLException {
		//perform update, if fails rollback and throw sql exception	
    	Connection conn = JDBCConnection.getConnection();
		if (conn == null)  throw new SQLException();
		PreparedStatement insertPlaceState = conn.prepareStatement(insertPlacesSQL);
		PreparedStatement insertPlaceCategoryState = conn.prepareStatement(insertCategoriesSQL);
		for (Place place : places) {
			insertPlaceState.setString(1, place.getYagoId());
			insertPlaceState.setString(2, place.getName());
			insertPlaceState.setDouble(3, place.getLoc().getLat());
			insertPlaceState.setDouble(4, place.getLoc().getLon());
			insertPlaceState.addBatch();

			for (Category cat : place.getCaegories()) {
				insertPlaceCategoryState.setString(1, place.getYagoId());
				insertPlaceCategoryState.setString(2, cat.getYagoId());
				insertPlaceCategoryState.addBatch();
			}
		}
		List<PreparedStatement> statements = new ArrayList<PreparedStatement>();
		statements.add(insertPlaceState);
		statements.add(insertPlaceCategoryState);
		JDBCConnection.executeBatch(statements,conn);
    }
	public static void deleteAllPlaces() throws SQLException {
		Connection conn = JDBCConnection.getConnection();
		if (conn == null)  throw new SQLException();
		PreparedStatement deletePlacesSttmnt = conn.prepareStatement(deletePlacesSQL);
		PreparedStatement deletePlacesCatsSttmnt = conn.prepareStatement(deletePlacesCatsSQL);
		List<PreparedStatement> statements = new ArrayList<PreparedStatement>();
		statements.add(deletePlacesCatsSttmnt);
		statements.add(deletePlacesSttmnt);
		JDBCConnection.executeUpdate(statements, conn);
	}

	public static void dropForeignKeys() throws SQLException {
		Connection conn = JDBCConnection.getConnection();
		CallableStatement cs = conn.prepareCall(DROP_FOREIGN_KEYS_STORED_PROC);
		JDBCConnection.executeStoredProcedures(cs, conn);
	}


	public static void createForeignKeys() throws SQLException {
		Connection conn = JDBCConnection.getConnection();
		CallableStatement cs = conn.prepareCall(CREATE_FOREIGN_KEYS_STORED_PROC);
		JDBCConnection.executeStoredProcedures(cs, conn);
	}

	public static List<Place> getPlacesByLocation(SearchByLocation query) throws SQLException {
		Connection conn = JDBCConnection.getConnection();
		String sql = null;
		String catIn =  null;
		String catOr = null;
		String propIn = null;
		String propOr = null;

		int i = 5;
		if (query.getCategories().size() > 0) {
			catIn = JDBCConnection.preparePlaceHolders(query.getCategories().size());
			catOr = "";
		} else {
			catIn = "''";
			catOr = collectionEmpty;
		}
		if (query.getProperties().size() > 0) {
			propIn = JDBCConnection.preparePlaceHolders(query.getProperties().size());
			propOr = "";
		} else {
			propIn = "''";
			propOr = collectionEmpty;
		}

		sql = String.format(getPlacesByLocation,catIn,catOr,propIn,propOr);
		PreparedStatement selectPlaces = conn.prepareStatement(sql);
		selectPlaces.setDouble(1,query.getLoc().getLat());
		selectPlaces.setDouble(2,query.getLoc().getLat());
		selectPlaces.setDouble(3,query.getLoc().getLon());
		selectPlaces.setInt(4, (int) (query.getLoc().getRadius() / 1.609));

		i = 5;
		for (Category cat : query.getCategories()) {
			selectPlaces.setString(i, cat.getYagoId());
			i++;
		}
		for (Property prop : query.getProperties()) {
			selectPlaces.setString(i, prop.getYagoId());
			i++;
		}

		ResultSet rs = JDBCConnection.executeQuery(selectPlaces, conn);
		Map<String, Place> places = new HashMap<>();
		while (rs.next()) {
			if (!places.containsKey(rs.getString("id"))) {
				Place newPlace = new Place(rs.getString("id"));
				newPlace.getLoc().setLat(rs.getDouble("lat"));
				newPlace.getLoc().setLon(rs.getDouble("lon"));
				newPlace.setName(rs.getString("name"));
				places.put(newPlace.getYagoId(), newPlace);
			}
			places.get(rs.getString("id")).getCaegories().add(new Category(rs.getString("Categoryid"), 0, ""));
			if (rs.getInt("PropId") != 0)
				places.get(rs.getString("id")).getProperties().add(new Property(rs.getInt("PropId")));
		}

		return new ArrayList<>(places.values());

	}
}