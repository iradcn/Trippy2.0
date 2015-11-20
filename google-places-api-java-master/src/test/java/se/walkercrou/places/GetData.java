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
	    private static final String RothschildFile = "C:\\dev\\Trippy2.0\\GoogleData\\Rothschild.txt";
	     
	    
	    public static void main (String [] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
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
	    	GetNearbyPlaces(RothschildFile,32.068177,34.775915);
	    }
	    public static void GetNearbyPlaces(String path, double start_lon, double start_lng) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
	        System.out.println("******************** getNearbyPlaces ********************");
	        PLACE_START_LAT=  start_lon;
	        PLACE_START_LNG= start_lng;
	        Types type = new Types();
	        //the point in the sea = 33.060865; 34.628895;
	        //tel aviv points: 32.084721;34.794548;
	        //jerusalem 31.765564, 35.211261
	        //haifa  32.819554;34.968398;
	        //Yarkon 32.092392, 34.776652
	        //Beer-sheva : 31.249535, 34.796235
	        //Eilat 29.564031, 34.951469
	        //Rishon 31.977733, 34.787317
	        //Yaffo 32.055130, 34.767502
	        //Rosh_Pina 32.963869, 35.544308
	        //North of diingof tel aviv 32.091732, 34.776791
	        //dizingof center 32.077000, 34.773671
	      
	        //rotchild 32.068177, 34.775915
	        
	        //bnei-dan 32.094649, 34.788790
	        //ramat-hahayal 32.110208, 34.834966
	        //ramat-gan 32.070359, 34.822092
	        //givataim 32.075450, 34.808702
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
