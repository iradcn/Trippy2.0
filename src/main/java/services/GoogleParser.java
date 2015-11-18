package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Place;

import model.Category;
import model.CategoryMap;
import model.Location;




public class GoogleParser {

	private static final String IS_ENGLISH_REGEX = "^[ \\w \\d \\s \\. \\& \\+ \\- \\, \\! \\@ \\# \\$ \\% \\^ \\* \\( \\) \\; \\\\ \\/ \\| \\< \\> \\\" \\' \\? \\= \\: \\[ \\] ]*";
	private static final  Pattern Question = Pattern.compile("\\?\\?\\?+");
	
	public List<Place> getPlacesFromCsvFile(String filePath) throws NumberFormatException, IOException{
		Matcher matchRegex;
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
                    if(!placeInfo[1].matches(IS_ENGLISH_REGEX)){
                    	continue;
                    }
                    matchRegex = Question.matcher(placeInfo[1]);
                    if(matchRegex.find()){
                    	continue;
                    }
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
