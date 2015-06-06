package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import DAO.PlaceDAO;
import DAO.PropertyDAO;
import model.Place;
import model.Property;

/**
 * Created by nimrod on 5/24/15.
 */
public class YagoImportService implements Runnable {
	

	@Override
	public void run() {

		System.out.println("Yago Import Service Started...");
        Parser yagoTsvParser = new Parser();

        Map<String, Place> chunkOfPlaces;
    	Progress progrs = Progress.getStatus();
        try{

			System.out.println("Drop Foreign Keys");
			PlaceDAO.dropForeignKeys();

			System.out.println("Delete All Places");
			PlaceDAO.deleteAllPlaces();

			System.out.println("Parser Initialize");
	        yagoTsvParser.init(progrs);

			System.out.println("Retrieve First Chunk Of Places");
	        chunkOfPlaces  = yagoTsvParser.getChunkOfPlaces(progrs);
	        while (!chunkOfPlaces.isEmpty()){
	            //parse chunk & save

				//System.out.println("Update places locations");
	        	yagoTsvParser.updatePlaceLocation(chunkOfPlaces);
	            List<Place> places = new ArrayList<Place>(chunkOfPlaces.values());

				//System.out.println("Save All Places");
	            Place.saveAll(places);

				//System.out.println("Retrieve Chunk Of Places");
	            chunkOfPlaces  = yagoTsvParser.getChunkOfPlaces(progrs);
	        }

			System.out.println("Sync Places Properties relation");
			PropertyDAO.SyncPlacesProperties();

			System.out.println("Create foreign Keys");
			PlaceDAO.createForeignKeys();
        	progrs.setFinish();
        }catch(Exception e){
        	progrs.setError();
        	System.out.println("Error has occured:"+e.getStackTrace());
        }
	}

}
