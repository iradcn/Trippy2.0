package DAO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import model.Category;
import model.Place;

import org.json.simple.parser.ParseException;

import services.JDBCConnection;

/**
 * Created by nimrod on 5/24/15.
 */
public class PlaceDAO {
	
    public static void SavePlacesAndPlaceCats(List<Place> places) throws FileNotFoundException, IOException, ParseException, SQLException {
		//perform update, if fails rollback and throw sql exception	
    	Connection conn = JDBCConnection.getConnection();
			try {
				conn.setAutoCommit(false);
				SavePlaces(places, conn);
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
				if (conn != null){
					try {
						conn.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				throw new SQLException();
			}finally{
				conn.setAutoCommit(true);
			}
    }

	private static void SavePlaces(List<Place> places, Connection conn)
			throws SQLException {
		//perform update

		Statement stmt = conn.createStatement();
		StringBuilder sb_places = new StringBuilder();
		StringBuilder sb_place_categories = new StringBuilder();

		sb_places.append("INSERT INTO places (`id`,`name`,`lat`,`lon`) VALUES");
		sb_place_categories.append("INSERT INTO placescategories (`placeid`,`categoryid`) VALUES");

		boolean isFirstPlaceEntry = true;
		boolean isFirstPlaceCatEntry = true;
		
		for (Place place:places){
			if (!isFirstPlaceEntry){
				sb_places.append(",");
			}
			else{
				isFirstPlaceEntry=false;
			}
			buildNextPlaceEntry(sb_places, place);
			
			for (Category cat:place.getCaegories()){
				if (!isFirstPlaceCatEntry){
					sb_place_categories.append(",");
				}
				else{
					isFirstPlaceCatEntry=false;
				}
				buildNextPlaceCatEntry(sb_place_categories, place, cat);
				
			}

		
		}
		sb_places.append(" ON DUPLICATE KEY UPDATE `id`=`id`");
		//System.out.println(sb_places.toString());
		//System.out.println(sb_place_categories.toString());
		stmt.executeUpdate(sb_places.toString());
		stmt.executeUpdate(sb_place_categories.toString());
		
		//System.out.println("Num of places rows inserted:"+rs_places);
		//System.out.println("Num of place-cats rows inserted:"+rs_places_cats);
		
		JDBCConnection.closeConnection(conn);
	}

	private static void buildNextPlaceCatEntry(
			StringBuilder sb_place_categories, Place place, Category cat) {
		sb_place_categories.append("(");
		sb_place_categories.append('"').append(place.getYagoId()).append('"').append(',');
		sb_place_categories.append(cat.getId());
		sb_place_categories.append(")");
	}

	private static void buildNextPlaceEntry(StringBuilder sb_places, Place place) {
		sb_places.append("(");
		sb_places.append('"').append(place.getYagoId()).append('"').append(',');
		sb_places.append(place.getName()).append(',');
		sb_places.append(String.format("%.4f",place.getLat())).append(',');
		sb_places.append(String.format("%.4f",place.getLon()));
		sb_places.append(")");
	}
}
