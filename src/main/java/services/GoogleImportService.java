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
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/Tel-Aviv_demo_pipe.txt"));
			PlaceDAO.SavePlacesAndPlaceCats(allPlaces);
        }catch(Exception e){
        	System.out.println("Error has occured:"+e.getStackTrace());
        }
	}

}
