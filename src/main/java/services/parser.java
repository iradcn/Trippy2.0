package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.parser.ParseException;

import model.Category;
import model.Place;

/**
 * Created by nimrod on 5/24/15.
 */
public class Parser {
	private BufferedReader br;
    public int chunkSize = 30000;
    private Map<String,Category> allCategories;
    private final String PATH = "Resources//yagoGeonamesTypes.tsv";
    public void init(Progress progrs) throws IOException, ParseException, SQLException {
         br = new BufferedReader(new FileReader(PATH));
         allCategories = Category.loadAll();
         progrs.setLocal_total_read(this.countOverallItems());
    }

    public Map<String,Place> getChunkOfPlaces(Progress progrs) throws IOException {

        Map<String,Place> chunkList = new HashMap<String,Place>();
        String str;
        
        while ((str = br.readLine()) != null && chunkList.size() <= chunkSize) {
            progrs.setLocal_read(progrs.getLocal_read()+1);
            progrs.save();
            if (str.trim().length() == 0) {
                continue;
            }

            String[] values = str.split("\\t");
            if (values[3] != null){
                boolean placeMathingCat = allCategories.containsKey(values[3]);
                if (placeMathingCat) {
                    Place newPlace = new Place(values[1]);
                    newPlace.addCategory(allCategories.get(values[3]));
                    chunkList.put(values[1],newPlace);
                }
            }
        }
        return chunkList;
    }
    public int countOverallItems() throws IOException{
    	BufferedReader count_br = new BufferedReader(new FileReader(PATH));
    	int sum=0;
        while (count_br.readLine() != null){
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
        brPlace.close();
    }

}
