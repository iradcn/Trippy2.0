package services;

import model.Place;
import model.Progress;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by nimrod on 5/24/15.
 */
public class yagoImportService implements Runnable {
	

	@Override
	public void run() {
    	Progress progrs;
        parser yagoTsvParser = new parser();
        Map<String, Place> chunkOfPlaces;
        int totalNumPlaces;
        
        try{
	        yagoTsvParser.init();
	        totalNumPlaces = yagoTsvParser.countOverallItems();
	        progrs = new Progress(totalNumPlaces);
	        chunkOfPlaces  = yagoTsvParser.getChunkOfPlaces(progrs);
	        while (!chunkOfPlaces.isEmpty()){
	            //parse chunk & save
	        	yagoTsvParser.updatePlaceLocation(chunkOfPlaces);
	            //yagoTsvParser.parseYagoIds(chunkOfPlaces);
	            List<Place> places = new ArrayList<Place>(chunkOfPlaces.values());
	            Place.saveAll(places);
	            //save progress
	            chunkOfPlaces  = yagoTsvParser.getChunkOfPlaces(progrs);
	        }
        }catch(Exception e){
        	System.out.println("Error has occured:"+e.getStackTrace());
        }
	}

}
