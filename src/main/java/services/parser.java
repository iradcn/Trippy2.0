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

    public void init() throws FileNotFoundException {
         br = new BufferedReader(new FileReader("Resources//yagoSimpleTypes.tsv"));
    }

    public Map<String,Place> getChunkOfPlaces() throws IOException {

        Map<String,Place> chunkList = new HashMap<String,Place>();
        String str;
        while ((str = br.readLine()) != null && chunkList.size() <= chunkSize) {
            if (str.trim().length() == 0) {
                continue;
            }

            String[] values = str.split("\\t");
            if (values[3] != null)
                if (checkCategoryMatch(values[3])) {
                    Place newPlace = new Place(values[1]);
                    Category newCat = new Category(values[3]);
                    newPlace.addCategory(newCat);
                    chunkList.put(values[1],newPlace);
                }
        }
        return chunkList;
    }

    public void updatePlaceLocation(Map<String, Place> entities) throws IOException {

        BufferedReader brPlace = new BufferedReader(new FileReader("Resources/yagoLiteralFacts.tsv"));
        String str;
        while ((str = brPlace.readLine()) != null) {
            if (str.trim().length() == 0) {
                continue;
            }

            String[] values = str.split("\\t");
            if (values[1] != null)
                if (entities.containsKey(values[1]) && ((values[2].equals("<hasLatitude>")) || (values[2].equals("<hasLongitude>")))) {

                    if ((values[2].equals("<hasLatitude>")))
                        entities.get(values[1]).setLat( Double.parseDouble(values[4]));
                     if ((values[2].equals("<hasLongitude>")))
                    entities.get(values[1]).setLon(Double.parseDouble(values[4]));
                }
        }
    }

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

    public boolean checkCategoryMatch(String str) {
        if (str.equalsIgnoreCase("<wordnet_place_of_worship_103953416>") ||
                str.equalsIgnoreCase("<wordnet_plaza_103965456>") ||
                str.equalsIgnoreCase("<wordnet_museum_103800563>"))
            return true;
        return false;
    }
}
