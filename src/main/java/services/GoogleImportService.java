package services;

import java.util.ArrayList;
import java.util.List;

import model.Place;
import DAO.PlaceDAO;

/**
 * Created by nimrod on 5/24/15.
 */
public class GoogleImportService {

	public void importData() {
		GoogleParser googleParser = new GoogleParser();

        try{
			List<Place> allPlaces = new ArrayList<>();
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/Beer-sheva.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/dizingof.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/eilat.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/jerusalem.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/Rishon.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/Rothschild.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/Yarkon.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/haifa.txt"));
			
			PlaceDAO.SavePlacesAndPlaceCats(allPlaces);
        }catch(Exception e){
        	System.out.println("Error has occured:"+e.getStackTrace());
        }
	}

}
