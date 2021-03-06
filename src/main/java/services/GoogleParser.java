package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import model.Category;
import model.CategoryMap;
import model.Location;
import model.Place;




public class GoogleParser {

	
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
                    String[] placeInfo = line.substring(0, index).split("\t");
                    String[] categoriesInfo = line.substring(index+1, line.length() - 2).split(",");
                    HashSet<Category> categoriesSet = new HashSet<>();
                    for (String categoryStr : categoriesInfo) {               	
                    	if(!CategoryMap.getMap().containsKey(categoryStr))
                    		continue;
                    	int CategoryIndex = CategoryMap.getMap().get(categoryStr);
                        categoriesSet.add(new Category(CategoryIndex,categoryStr));
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
