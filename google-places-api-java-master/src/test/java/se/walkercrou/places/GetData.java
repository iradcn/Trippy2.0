package se.walkercrou.places;


import static se.walkercrou.places.GooglePlacesInterface.MAXIMUM_RADIUS;
import static se.walkercrou.places.GooglePlacesInterface.MAXIMUM_RESULTS;
import static se.walkercrou.places.GooglePlacesInterface.STRING_TYPES;

import java.io.InputStream;
import org.apache.commons.io.IOUtils;

public class GetData{
	    private static final String API_KEY_FILE_NAME = "places_api.key";
	    private static int RADIUS=5000;
	    /**
	     * the initial lat lon
	     */
	    private static  double PLACE_START_LAT ;
		private static double PLACE_START_LNG;
		/**
		 * if we want to go NUMBER_EAST_KM to east, then divide by the RADIUS
		 */
		private static int NUMBER_EAST_KM = 80;
		private static double EAST_KM = NUMBER_EAST_KM/(RADIUS/1000);
		private static int NUMBER_SOUTH_KM = 150;
		private static double SOUTH_KM = NUMBER_SOUTH_KM/(RADIUS/1000);
		private static int EARTH_RADIUS = 6378137;
	    private static GooglePlaces google;
	    
	    /**
	     * For the CSV file
	     * pathes to the file
	     */
	    private static final String FILEDIR= "src//main//resources//Files//";
	    private static final String ROTHSCILDfILE = FILEDIR + "Rothschild.txt";
	    private static final String  JERUSALEM = FILEDIR + "jerusalem.txt";
	    private static final String  YARKON = FILEDIR + "Yarkon.txt";
	    private static final String  BEER_SHEVA = FILEDIR + "Beer-sheva.txt";
	    private static final String  EILAT = FILEDIR + "eilat.txt";
	    private static final String  RISHON = FILEDIR + "Rishon.txt";
	    private static final String  DIZINGOF = FILEDIR + "dizingof.txt";
	    private static final String  HAIFA = FILEDIR + "haifa.txt";

	     
	    
	    public static void main(String [] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
	    	setUp();
	    	getAllPlaces();
	    	//GetNearbyPlaces();
	    }
	    public static void setUp() {
	        try {
	            InputStream in = GooglePlacesTest.class.getResourceAsStream("/" + API_KEY_FILE_NAME);
	            if (in == null)
	                throw new RuntimeException("API key not found.");
	            google = new GooglePlaces(IOUtils.toString(in));
	            google.setDebugModeEnabled(true);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    /*
	     * Getts all the places at once
	     */
	    public static void getAllPlaces() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
	    	GetNearbyPlaces(ROTHSCILDfILE,32.068177,34.775915);
	    	GetNearbyPlaces(JERUSALEM,31.765564,35.211261);
	    	GetNearbyPlaces(YARKON,32.092392,34.776652);
	    	GetNearbyPlaces(BEER_SHEVA,31.249535,34.796235);
	    	GetNearbyPlaces(EILAT,29.564031,34.951469);
	    	GetNearbyPlaces(RISHON,31.977733,34.787317);
	    	GetNearbyPlaces(DIZINGOF,32.077000,34.773671);
	    	GetNearbyPlaces(HAIFA,32.819554,34.968398);
	    	
	    	
	    }
	    public static void GetNearbyPlaces(String path, double start_lon, double start_lng) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
	        System.out.println("******************** getNearbyPlaces ********************");
	        PLACE_START_LAT=  start_lon;
	        PLACE_START_LNG= start_lng;
	        Types type = new Types();

	        double currentLon=PLACE_START_LNG;
	        double currentLat=PLACE_START_LAT;
	        
	        GooglePlaces.createCSVfile(path);
	   
	        //EAST_KM
	        for(int east=0;east<1;east++){
	        	for(int south=0;south<1;south++){	
	        		 for(String name : Types.getTypes()){
	        			System.out.println("******************** name= ********************"+name);
	     	        	google.getNearbyPlaces(currentLat, currentLon, RADIUS,MAXIMUM_RESULTS,TypeParam.name(STRING_TYPES).value(name));
	     	        }
	        		currentLon = calcLon(currentLon,currentLat);
	        	}
	        	currentLat = calcLat(currentLat);
	        }
	        GooglePlaces.closeCSVfile();
	    }
	    /*
	     * calculate the next point to search for places around it
	     */
	    private static double calcLat (double lat){
	    	int nextCenter = RADIUS*2+1000;
	    	double dlat =  nextCenter/EARTH_RADIUS;
	    	return lat+dlat*180/Math.PI;    	
	    }
	    
	    private static double calcLon (double lon,double lat){
	    	int nextCenter = RADIUS*2+1000;
	    	double dLon = nextCenter/(EARTH_RADIUS*Math.cos(Math.PI*lat/180));
	    	return lon+dLon*180/Math.PI;
	    }
}
