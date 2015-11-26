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
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/Rothschild_small.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/Yarkon.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/haifa.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/bazel.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/north_dizingof.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/yarkon_street.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/ramat_gan.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/givatayim.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/king_george_ta.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/neve_tzedek.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/bat_yam.txt"));

			
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/Beer-sheva_2.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/dizingof_2.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/eilat_2.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/jerusalem_2.txt"));

			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/Yarkon_2.txt"));

			
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/bazel_2.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/north_dizingof_2.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/yarkon_street_2.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/ramat_gan_2.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/givatayim_2.txt"));
			allPlaces.addAll(googleParser.getPlacesFromCsvFile("GoogleData/king_george_ta_2.txt"));

			
			
			
			PlaceDAO.SavePlacesAndPlaceCats(allPlaces);
        }catch(Exception e){
        	System.out.println("Error has occured:"+e.getStackTrace());
        }
	}

}
