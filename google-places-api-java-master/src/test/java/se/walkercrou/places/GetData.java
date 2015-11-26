package se.walkercrou.places;


import static se.walkercrou.places.GooglePlacesInterface.MAXIMUM_RADIUS;
import static se.walkercrou.places.GooglePlacesInterface.MAXIMUM_RESULTS;
import static se.walkercrou.places.GooglePlacesInterface.STRING_TYPES;

import java.io.InputStream;
import org.apache.commons.io.IOUtils;

public class GetData{
	    private static final String API_KEY_FILE_NAME = "places_api.key";
	    private static int RADIUS=100;
	    /**
	     * the initial lat lon
	     */
	    private static  double PLACE_START_LAT ;
		private static double PLACE_START_LNG;
		/**
		 * if we want to go NUMBER_EAST_KM to east, then divide by the RADIUS
		 */
		private static int NUMBER_EAST_KM = 80;
		//private static double EAST_KM = NUMBER_EAST_KM/(RADIUS/1000);
		private static int NUMBER_SOUTH_KM = 150;
	//	private static double SOUTH_KM = NUMBER_SOUTH_KM/(RADIUS/1000);
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
	    private static final String  BAZEL = FILEDIR + "bazel.txt";
	    private static final String  NORTH_DIZINGOF = FILEDIR + "north_dizingof.txt";
	    private static final String  YARKON_STREET = FILEDIR + "yarkon_street.txt";
	    private static final String  RAMAT_GAN = FILEDIR + "ramat_gan.txt";
	    private static final String  GIVATAYIM = FILEDIR + "givatayim.txt";
	    private static final String  KING_GORGE_TA = FILEDIR + "king_george_ta.txt";
	    private static final String  NEVE_TZEDEK = FILEDIR + "neve_tzedek.txt";
	    private static final String  BAT_YAM = FILEDIR + "bat_yam.txt";
	    private static final String  JAFFO = FILEDIR + "jaffo.txt";
	    private static final String  HERZLIYA = FILEDIR + "herzliya.txt";
	    private static final String  SAFED = FILEDIR + "safed.txt";
	    
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
	    	GetNearbyPlaces(RISHON,31.975215, 34.811689);
	    	GetNearbyPlaces(DIZINGOF,32.077000,34.773671);
	    	GetNearbyPlaces(HAIFA,32.819554,34.968398);
	    	GetNearbyPlaces(BAZEL,32.089936,34.779404);
	    	GetNearbyPlaces(NORTH_DIZINGOF,32.093154,34.776486);
	    	GetNearbyPlaces(YARKON_STREET,32.087819, 34.771154);
    		GetNearbyPlaces(RAMAT_GAN,32.064988, 34.823827);
	    	GetNearbyPlaces(GIVATAYIM,32.075211, 34.809046);
	    	GetNearbyPlaces(KING_GORGE_TA,32.070322, 34.771298);
	    	GetNearbyPlaces(NEVE_TZEDEK,32.061758, 34.765930);
	    	GetNearbyPlaces(BAT_YAM,32.014886, 34.748081);
	    	GetNearbyPlaces(JAFFO,32.054668, 34.754387);
	    	GetNearbyPlaces(HERZLIYA,32.162566, 34.807273);
	    	GetNearbyPlaces(SAFED,32.964715, 35.501809);
	    	
	    	
	    	
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
	        for(int east=0;east<15;east++){
	        	for(int south=0;south<15;south++){	
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
	    	int nextCenter = RADIUS*2+100;
	    	double dlat =  nextCenter/EARTH_RADIUS;
	    	if(dlat==0)
	    		return lat+0.01;
	    	return lat+(dlat*180/Math.PI);    	
	    }
	    
	    private static double calcLon (double lon,double lat){
	    	int nextCenter = RADIUS*2+100;
	    	double dLon = nextCenter/(EARTH_RADIUS*Math.cos(Math.PI*lat/180));
	    	return lon+dLon*180/Math.PI;
	    }
}
