package DAO;

import model.Category;
import model.Place;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.security.MD5Encoder;

import services.JDBCConnection;

/**
 * Created by nimrod on 5/24/15.
 */
public class PlaceDAO {
	
    public static boolean SavePlacesAndPlaceCats(List<Place> places) {
		try{
			Connection conn = JDBCConnection.getConnection();
			SavePlaces(places, conn);
			
		}catch(Exception e){
			System.out.println("ERROR executeQuery - " + e.getMessage());
			return false;
		}

		return true;
    }

	private static void SavePlaces(List<Place> places, Connection conn)
			throws SQLException {
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
				sb_place_categories.append("(");
				sb_place_categories.append('"').append(place.getYagoId()).append('"').append(',');
				sb_place_categories.append(cat.getId());
				sb_place_categories.append(")");
				
			}

		
		}
		sb_places.append(" ON DUPLICATE KEY UPDATE `id`=`id`");
		System.out.println(sb_places.toString());
		System.out.flush();
		//System.out.println(sb_place_categories.toString());
		int rs_places = stmt.executeUpdate(sb_places.toString());
		int rs_places_cats = stmt.executeUpdate(sb_place_categories.toString());
		System.out.println("Num of places rows inserted:"+rs_places);
		System.out.println("Num of place-cats rows inserted:"+rs_places_cats);
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
