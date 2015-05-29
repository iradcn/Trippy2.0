package DAO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import model.Category;
import model.Place;

import org.json.simple.parser.ParseException;

/**
 * Created by nimrod on 5/24/15.
 */
public class PlaceDAO {

	private static final String DROP_FOREIGN_KEYS_STORED_PROC = "{call RemoveForeignKeys()}";
	private static final String CREATE_FOREIGN_KEYS_STORED_PROC = "{call CreateForeignKeys()}";

	private static String insertPlacesSQL = "INSERT INTO places"
			+ " (`id`,`name`,`lat`,`lon`) VALUES"
			+ "(?,?,?,?) ON DUPLICATE KEY UPDATE `id`=`id`";

	private static String insertCategoriesSQL = "INSERT INTO placescategories"
			+ " (`placeid`,`categoryid`) VALUES"
			+ "(?,?) ON DUPLICATE KEY UPDATE `PlaceId`=`PlaceId`";

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
			insertPlaceState.setDouble(3, place.getLat());
			insertPlaceState.setDouble(4, place.getLon());
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
		JDBCConnection.executeUpdate(statements,conn);
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

}