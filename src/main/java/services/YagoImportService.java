package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Place;

/**
 * Created by nimrod on 5/24/15.
 */
public class YagoImportService implements Runnable {
	

	@Override
	public void run() {
        Parser yagoTsvParser = new Parser();
        Map<String, Place> chunkOfPlaces;
    	Progress progrs = Progress.getStatus();
        try{
	        yagoTsvParser.init(progrs);
	        chunkOfPlaces  = yagoTsvParser.getChunkOfPlaces(progrs);
	        while (!chunkOfPlaces.isEmpty()){
	            //parse chunk & save
	        	yagoTsvParser.updatePlaceLocation(chunkOfPlaces);
	            List<Place> places = new ArrayList<Place>(chunkOfPlaces.values());
	            Place.saveAll(places);
	            chunkOfPlaces  = yagoTsvParser.getChunkOfPlaces(progrs);
	        }
        	progrs.setFinish();
        }catch(Exception e){
        	progrs.setError();
        	System.out.println("Error has occured:"+e.getStackTrace());
        }
	}

}
