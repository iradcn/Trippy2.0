package services;

import model.Place;

import java.io.FileNotFoundException;
import java.io.IOException;
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

    }

    //private static List<>
}
