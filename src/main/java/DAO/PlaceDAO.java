package DAO;

import java.sql.*;
import java.util.*;

import model.Category;
import model.Location;
import model.Place;
import model.Property;
import model.User;
import protocol_model.ResultMultipleSearch;
import protocol_model.SearchByLocation;
import protocol_model.SearchByMultipleLocation;

/**
 * Created by nimrod on 5/24/15.
 */
public class PlaceDAO {

	private static String collectionEmpty = "OR 1=1";
	private static String insertPlacesSQL = "INSERT INTO places"
			+ " (`id`,`name`,`lat`,`lon`) VALUES"
			+ "(?,?,?,?) ON DUPLICATE KEY UPDATE `id`=`id`";
	private static String insertOnlyCategoriesSQL = "INSERT INTO categories" + "(`name`, `id`) VALUES"
																+"(airport,1),"
																+ "(amusement_park,2),"
																+ "(aquarium,3)"
																+ "(art_gallery,4),"
																+ "(bakery,5),"
																+ "(bar,6),"
																+ "(cafe,7),"
																+ "(casino,8),"
																+ "(clothing_store,9),"
																+ "(convenience_store,10),"
																+ "(department_store,11),"
																+ "(food,12),"
																+ "(grocery_or_supermarket,13),"
																+ "(gym,14),"
																+ "(health,15),"
																+ "(movie_theater,16),"
																+ "(museum,17),"
																+ "(night_club,18),"
																+ "(park,19),"
																+ "(restaurant,20),"
																+ "(shopping_mall,21),"
																+ "(spa,22),"
																+ "(zoo,23);";

																private static String insertCategoriesSQL = "INSERT INTO places_categories"
			+ " (`placeid`,`categoryid`) VALUES"
			+ "(?,?) ON DUPLICATE KEY UPDATE `PlaceId`=`PlaceId`";

	private static String getPlacesByLocation = "SELECT  p.name, p.lat, p.lon, p.id, pc.CategoryId, pp.PropId " +
												"FROM places p " +
												     "LEFT OUTER JOIN places_categories pc ON p.id = pc.placeid " +
													 "LEFT OUTER JOIN places_props pp ON pp.placeId = p.ID " +
												"WHERE "+
													"3956 * 2 * ASIN(SQRT(POWER(SIN((? - p.lat) * PI() / 180 / 2), " +
													"2) + COS(? * PI() / 180) * COS(p.lat * PI() / 180) * " +
													"POWER(SIN((? - p.lon) * PI() / 180 / 2),2))) < ? " +
													"AND (pc.CategoryId IN (%s) %s) " +
													"AND (pp.PropId IN (%s) %s)";

	private static String countPlacesByLoc = "SELECT COUNT(DISTiNCT(p.name)) AS cms " +
											 "FROM places p,  places_categories pc, places_props pp" +
											 " WHERE " +
												"3956 * 2 * ASIN(SQRT(POWER(SIN((? - p.lat) * PI() / 180 / 2), " +
												"2) + COS(? * PI() / 180) * COS(p.lat * PI() / 180) * " +
												"POWER(SIN((? - p.lon) * PI() / 180 / 2),2))) < ? " +
												"AND p.id = pc.placeid AND pc.CategoryId = ?";
	private static String addPropQuery = " AND pp.placeId = p.ID AND pp.PropId = ?";

	private static String deletePlacesSQL = "DELETE from places";
	private static String deletePlacesCatsSQL = "DELETE from places_categories";
	private static String selectByName = "SELECT `Id` from places where TRIM(LOWER(places.name))=TRIM(LOWER(?))";
	private static String insertCheckIn = "INSERT INTO users_check_in (`user_id`,`place_id`) values(?,?)";
	private static String selectPlaceById = "SELECT `Name`,`Lat`,`Lon`,`Id` from places where Id=?";
	

	public static Place getPlace(String Id) throws SQLException {
		
    	Connection conn = JDBCConnection.getConnection();
		if (conn == null)  throw new SQLException();

		PreparedStatement ps = conn.prepareStatement(selectPlaceById);
		ps.setString(1, Id);
		ResultSet rs = JDBCConnection.executeQuery(ps, conn);
		
		Place foundPlace = null;
		
		if (rs.next()) {
			foundPlace = new Place(rs.getString("Id"));
		}
		
		return foundPlace;
		
		
	}
	public static void SavePlacesAndPlaceCats(List<Place> places) throws SQLException {
		//perform update, if fails rollback and throw sql exception	
    	Connection conn = JDBCConnection.getConnection();
		if (conn == null)  throw new SQLException();
		PreparedStatement insertCategoriesState = conn.prepareStatement(insertOnlyCategoriesSQL);
		PreparedStatement insertPlaceState = conn.prepareStatement(insertPlacesSQL);
		PreparedStatement insertPlaceCategoryState = conn.prepareStatement(insertCategoriesSQL);
		for (Place place : places) {
			insertPlaceState.setString(1, place.getGoogleId());
			insertPlaceState.setString(2, place.getName());			
			insertPlaceState.setDouble(3, place.getLoc().getLat());
			insertPlaceState.setDouble(4, place.getLoc().getLon());
			insertPlaceState.addBatch();



			for (Category cat : place.getCategories()) {
				insertPlaceCategoryState.setString(1, place.getGoogleId());
				insertPlaceCategoryState.setInt(2, cat.getId());
				insertPlaceCategoryState.addBatch();
			}
		}
		List<PreparedStatement> statements = new ArrayList<PreparedStatement>();
		statements.add(insertPlaceState);
		statements.add(insertPlaceCategoryState);
		statements.add(insertCategoriesState);
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
			selectPlaces.setInt(i, cat.getId());
			i++;
		}
		for (Property prop : query.getProperties()) {
			selectPlaces.setInt(i, prop.getId());
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
				places.put(newPlace.getGoogleId(), newPlace);
			}
			places.get(rs.getString("id")).getCategories().add(new Category(rs.getInt("Categoryid"), ""));
			if (rs.getInt("PropId") != 0)
				places.get(rs.getString("id")).getProperties().add(new Property(rs.getInt("PropId")));
		}

		return new ArrayList<>(places.values());

	}

	public static List<ResultMultipleSearch> getPlacesAggregation(SearchByMultipleLocation query) throws SQLException {
		Connection conn = JDBCConnection.getConnection();
		List<ResultMultipleSearch> result = new ArrayList<ResultMultipleSearch>();
		PreparedStatement selectPlaces = null;

		if (query.getProperty() == 0) {
			selectPlaces = conn.prepareStatement(countPlacesByLoc);

		} else {
			selectPlaces = conn.prepareStatement(countPlacesByLoc+addPropQuery);
		}

		for (Location loc : query.getLocs()) {
			selectPlaces.setDouble(1,loc.getLon());
			selectPlaces.setDouble(2,loc.getLon());
			selectPlaces.setDouble(3, loc.getLat());
			selectPlaces.setInt(4, (int) (loc.getRadius() / 1.609));
			selectPlaces.setString(5, query.getCategory());

			if (query.getProperty() != 0)
				selectPlaces.setInt(6, query.getProperty());

			ResultSet rs = JDBCConnection.executeQuery(selectPlaces, conn);
			ResultMultipleSearch rms = new ResultMultipleSearch();
			rs.next();
			rms.setLoc(loc);
			rms.setCountPlaces(rs.getInt("cms"));
			result.add(rms);
		}

		return  result;
	}
	//ליצור עוד אחד כזה
	public static Place findPlaceByName(String placeName) throws SQLException {
		Connection conn = JDBCConnection.getConnection();

		PreparedStatement selectPlaces = null;
		selectPlaces = conn.prepareStatement(selectByName);
		//להכניס פרמטר לסטרינג, למקומות של הסימן שאלה בלמעלה
		selectPlaces.setString(1, placeName);

		ResultSet rs = JDBCConnection.executeQuery(selectPlaces, conn);
		
		if (rs.next()) {
			Place newPlace = new Place(rs.getString(1));
			return newPlace;
		}
		else {
			return null;
		}

	}
	
	public static void insertCheckIn(User user, Place place) throws SQLException {
		Connection conn = JDBCConnection.getConnection();
		PreparedStatement checkInSt;
		checkInSt = conn.prepareStatement(insertCheckIn);
		checkInSt.setString(1,user.getUserId());
		checkInSt.setString(2,place.getGoogleId());
		List<PreparedStatement> lst = new ArrayList<>();
		lst.add(checkInSt);
		JDBCConnection.executeUpdate(lst, conn);
	}
	
}