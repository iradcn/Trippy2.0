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
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/Yarkon_demo_pipe.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/Yafo_demo_pipe.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/Jerusalem_demo_pipe.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/Eilat_demo_pipe.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/Haifa_demo_pipe.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/Rishon_demo_pipe.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/Rosh_Pina_pipe.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/BeerSheva_demo_pipe.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/North_dizingof_tel_aviv_pipe.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/dizingof_center_pipe.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/dizingof_center_pipe.txt"));
			
			
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/rotchild_pipe.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/dizingof_center_pipe.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/dizingof_center_pipe.txt"));
			
			PlaceDAO.SavePlacesAndPlaceCats(allPlaces);
        }catch(Exception e){
        	System.out.println("Error has occured:"+e.getStackTrace());
        }
	}

}
