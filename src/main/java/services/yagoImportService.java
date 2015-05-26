package services;

import model.Place;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by nimrod on 5/24/15.
 */
public class yagoImportService {

    public static void importData() throws IOException {

        parser yagoTsvParser = new parser();
        yagoTsvParser.init();
        Map<String, Place> chunkOfPlaces  = yagoTsvParser.getChunkOfPlaces();
        yagoTsvParser.updatePlaceLocation(chunkOfPlaces);
        yagoTsvParser.parseYagoIds(chunkOfPlaces);
        List<Place> places = new ArrayList<Place>(chunkOfPlaces.values());
        Place.saveAll(places);
    }

    //private static List<>
}
