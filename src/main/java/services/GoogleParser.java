package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import model.Place;
import org.json.simple.parser.ParseException;

import model.Category;
import model.Location;

public class GoogleParser {
	private BufferedReader br;
    private Map<String,Category> allCategories;

    public List<Place> getPlacesFromCsvFile(String filePath) throws NumberFormatException, IOException{

    	Map <String,Place> allPlaces = new HashMap<>();
    	String line;
    	 
    	try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            while ((line = br.readLine()) != null) {

                try {
                    if (line.trim().length() == 0) {
                        continue;
                    }

                    int index = line.indexOf('[');
                    String[] placeInfo = line.substring(0, index).split("\\|");
                    String[] categoriesInfo = line.substring(index+1, line.length() - 2).split(",");
                    HashSet<Category> categoriesSet = new HashSet<>();
                    for (String categoryStr : categoriesInfo) {
                        categoriesSet.add(new Category(categoryStr));
                    }
                    Location loc = new Location();
                    loc.setLat(Double.parseDouble(placeInfo[2]));
                    loc.setLon(Double.parseDouble(placeInfo[3]));
                    Place place = new Place(placeInfo[0], placeInfo[1], loc, categoriesSet);
                    allPlaces.put(place.getGoogleId(), place);

                } catch (Exception ex) {
                    System.out.print(ex.getMessage());
                    continue;
                }
            }
            return (new ArrayList<Place>(allPlaces.values()));
        }
        catch (Exception ex) {
            System.out.print(ex.getMessage());
            return null;
        }
    }
}
