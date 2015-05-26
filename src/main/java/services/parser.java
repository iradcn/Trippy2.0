package services;

import model.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nimrod on 5/24/15.
 */
public class parser {
    private BufferedReader br;
    public int chunkSize = 100;
    private List<Category> allCategories;
    private final String PATH = "Resources//yagoGeonamesTypes.tsv";
    public void init() throws FileNotFoundException {
         br = new BufferedReader(new FileReader(PATH));
         allCategories = Category.loadAll();
    }

    public Map<String,Place> getChunkOfPlaces(Progress progrs) throws IOException {

        Map<String,Place> chunkList = new HashMap<String,Place>();
        String str;

        while ((str = br.readLine()) != null && chunkList.size() <= chunkSize) {
            progrs.setRead(progrs.getRead()+1);
            if (str.trim().length() == 0) {
                continue;
            }

            String[] values = str.split("\\t");
            if (values[3] != null){
                Category placeMatchingCat = Category.isYagoIdInCatList(allCategories, values[3]);
                
                if (placeMatchingCat != null) {
                    Place newPlace = new Place(values[1]);
                    newPlace.addCategory(placeMatchingCat);
                    chunkList.put(values[1],newPlace);
                }
            }
        }
        return chunkList;
    }
    public int countOverallItems() throws IOException{
    	BufferedReader count_br = new BufferedReader(new FileReader(PATH));
    	int sum=0;
    	String str;
        while ((str = count_br.readLine()) != null){
        	sum++;
    	}
        count_br.close();
        return sum;
    }
    public void updatePlaceLocation(Map<String, Place> entities) throws IOException {

        BufferedReader brPlace = new BufferedReader(new FileReader("Resources/yagoGeonamesOnlyData.tsv"));
        String str;
        while ((str = brPlace.readLine()) != null) {
            if (str.trim().length() == 0) {
                continue;
            }

            String[] values = str.split("\\t");
            if (values[1] != null)
                if (entities.containsKey(values[1]) && ((values[2].equals("<hasLatitude>")) || 
                		(values[2].equals("<hasLongitude>"))|| (values[2].equals("rdfs:label") && values[3].endsWith("@eng")))) {

                    if ((values[2].equals("<hasLatitude>")))
                        entities.get(values[1]).setLat( Double.parseDouble(values[4]));
                    else if ((values[2].equals("<hasLongitude>")))
                    	 entities.get(values[1]).setLon(Double.parseDouble(values[4]));
                    else
                    	entities.get(values[1]).setName(values[3].split("@eng")[0]);
                }
        }
    }
/*
    public void parseYagoIds(Map<String,Place> entities) throws IOException {

        BufferedReader brNames = new BufferedReader(new FileReader("Resources/yagoLabels.tsv"));
        String str;
        while ((str = brNames.readLine()) != null) {
            if (str.trim().length() == 0) {
                continue;
            }

            String[] values = str.split("\\t");
            if (values[3] != null && values[2].equalsIgnoreCase("rdfs:label") && values[3].endsWith("@eng") &&
                    entities.containsKey(values[1])) {
                entities.get(values[1]).setName(values[3].split("@")[0]);
            }
        }
    }
*/
    public boolean checkCategoryMatch(String str) {
    	for (Category cat : allCategories){
    		if (cat.getYagoId().equalsIgnoreCase(str)){
                return true;
    		}
    		
    	}
        return false;
    }
}
