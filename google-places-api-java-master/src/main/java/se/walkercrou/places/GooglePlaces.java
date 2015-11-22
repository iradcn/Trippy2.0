package se.walkercrou.places;


import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import se.walkercrou.places.exception.GooglePlacesException;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

/**
 * Main class of API. Used for all entry web-api operations.
 */
public class GooglePlaces implements GooglePlacesInterface {

    /*
     * Argument #1: API Base URL
     * Argument #2: API Method
     * Argument #3: API Method arguments
     */
    public static String API_URL_FORMAT_STRING = "%s%s/json?%s";

    private String apiKey;
    private RequestHandler requestHandler;
    private boolean debugModeEnabled;

    /**
      * CSV columns  
      */
//    private static  String PLACE_NAME = "place name";
//    private static  String PLACE_ID = "place ID";
//    private static  String  LAT = "lat";
//    private static  String LON = "lon";
//    private static  String CATEGORY = "category";
//
//    private static String FILE_HEADER = PLACE_ID+","+PLACE_NAME+","+LAT+","+LON+","+CATEGORY;
  //  private static FileWriter fileWriter = null;
    private static FileOutputStream fileStream =null;
    private static OutputStreamWriter writer = null;
  /**
   * Delimiter used in CSV file
   */
     private static final String TAB_DELIMITER = "\t";
     private static final String NEW_LINE_SEPARATOR = "\n";

     
    /**
     * Creates a new GooglePlaces object using the specified API key and the specified {@link RequestHandler}.
     *
     * @param apiKey         that has been registered on the Google Developer Console
     * @param requestHandler to handle HTTP traffic
     */
    public GooglePlaces(String apiKey, RequestHandler requestHandler) {
        this.apiKey = apiKey;
        this.requestHandler = requestHandler;
    }

    /**
     * Creates a new GooglePlaces object using the specified API key.
     *
     * @param apiKey that has been registered on the Google Developer Console
     */
    public GooglePlaces(String apiKey) {
        this(apiKey, new DefaultRequestHandler());
    }

    /**
     * Creates a new GooglePlaces object using the specified API key and character encoding. Using a character encoding
     * other than UTF-8 is not advised.
     *
     * @param apiKey            that has been registered on the Google Developer Console
     * @param characterEncoding to parse data with
     */
    public GooglePlaces(String apiKey, String characterEncoding) {
        this(apiKey, new DefaultRequestHandler(characterEncoding));
    }

    private static String addExtraParams(String base, Param... extraParams) {
        for (Param param : extraParams)
            base += "&" + param.name + (param.value != null ? "=" + param.value : "");
        return base;
    }

    private static String buildUrl(String method, String params, Param... extraParams) {
        String url = String.format(Locale.ENGLISH, API_URL_FORMAT_STRING, API_URL, method, params);
        url = addExtraParams(url, extraParams);   
        url+= "&language=en";
        url = url.replace(' ', '+');
        return url;
    }

    protected static void checkStatus(String statusCode, String errorMessage) {
    	if(statusCode.compareTo(STATUS_ZERO_RESULTS)!=0){
    		GooglePlacesException e = GooglePlacesException.parse(statusCode, errorMessage);
    		if (e != null)
    			throw e;
    	}
    }

    /**
     * Parses the specified raw json String into a list of places.
     *
     * @param places to parse into
     * @param str    raw json
     * @param limit  the maximum amount of places to return
     * @return Next page token
     * @throws IOException 
     */
    public static String parse(GooglePlaces client, List<Place> places, String str, int limit) throws IOException {
        // parse json
        JSONObject json = new JSONObject(str);

        // check root elements
        String statusCode = json.getString(STRING_STATUS);
        checkStatus(statusCode, json.optString(STRING_ERROR_MESSAGE));
        if (statusCode.equals(STATUS_ZERO_RESULTS))
            return null;

        JSONArray results = json.getJSONArray(ARRAY_RESULTS);
        parseResults(client, places, results, Math.min(limit, MAXIMUM_PAGE_RESULTS));

        return json.optString(STRING_NEXT_PAGE_TOKEN, null);
    }
    
    /**
     * Parses the specified Radar raw json String into a list of places.
     *
     * @param places to parse into
     * @param str    Radar raw json
     * @param limit  the maximum amount of places to return
     * @throws IOException 
     */
    public static void parseRadar(GooglePlaces client, List<Place> places, String str, int limit) throws IOException {
      // parse json
      JSONObject json = new JSONObject(str);
      
      // check root elements
      String statusCode = json.getString(STRING_STATUS);
      checkStatus(statusCode, json.optString(STRING_ERROR_MESSAGE));
      if (statusCode.equals(STATUS_ZERO_RESULTS))
        return;
      
      JSONArray results = json.getJSONArray(ARRAY_RESULTS);
      parseResults(client, places, results, Math.min(limit, MAXIMUM_RADAR_RESULTS));
    }

    private static void parseResults(GooglePlaces client, List<Place> places, JSONArray results, int limit) throws IOException {
        for (int i = 0; i < limit; i++) {

            // reached the end of the page
            if (i >= results.length())
                return;

            JSONObject result = results.getJSONObject(i);

            // location
            JSONObject location = result.getJSONObject(OBJECT_GEOMETRY).getJSONObject(OBJECT_LOCATION);
            double lat = location.getDouble(DOUBLE_LATITUDE);
            double lon = location.getDouble(DOUBLE_LONGITUDE);

            String placeId = result.getString(STRING_PLACE_ID);
            String iconUrl = result.optString(STRING_ICON, null);
            String name = result.optString(STRING_NAME);
            String addr = result.optString(STRING_ADDRESS, null);
            double rating = result.optDouble(DOUBLE_RATING, -1);
            String vicinity = result.optString(STRING_VICINITY, null);

            
            // see if the place is open, fail-safe if opening_hours is not present
            JSONObject hours = result.optJSONObject(OBJECT_HOURS);
            boolean hoursDefined = hours != null && hours.has(BOOLEAN_OPENED);
            Status status = Status.NONE;
            if (hoursDefined) {
                boolean opened = hours.getBoolean(BOOLEAN_OPENED);
                status = opened ? Status.OPENED : Status.CLOSED;
            }

            // get the price level for the place, fail-safe if not defined


            // the place "types"
            List<String> types = new ArrayList<>();
            JSONArray jsonTypes = result.optJSONArray(ARRAY_TYPES);
            if (jsonTypes != null) {
                for (int a = 0; a < jsonTypes.length(); a++) {
                    types.add(jsonTypes.getString(a));
                }
            }
            
            Place place = new Place();
            // build a place object
            places.add(place.setClient(client).setPlaceId(placeId).setLatitude(lat).setLongitude(lon).setIconUrl(iconUrl).setName(name)
                    .setAddress(addr).setRating(rating).setStatus(status)
                    .addTypes(types).setVicinity(vicinity).setJson(result));
            
            JSONArray jsonPhotos = result.optJSONArray(ARRAY_PHOTOS);
            if(jsonPhotos != null){
            		JSONObject jsonPhoto = jsonPhotos.getJSONObject(0);
            		String photoRefrence = jsonPhoto.getString(STRING_PHOTO_REFERENCE);
            		  int width = jsonPhoto.getInt(INTEGER_WIDTH);
            		  int height = jsonPhoto.getInt(INTEGER_HEIGHT);
            		  Photo photo = new Photo(place,photoRefrence, width,height);
            		  BufferedImage photoStream = photo.download();
            		  writePhotoToFile(photoStream,placeId);
            	}
           
            
            toCSVfile(placeId,name,Double.toString(lat),Double.toString(lon),types.toString());

        }
    }

    
    /**
     * writes the photo to file in  the folder src//main//resources.
     * the name of the file should be the placeId
     * @throws IOException 
     */
    public static void writePhotoToFile (BufferedImage img,String placeId) throws IOException{
    	String currentPath = "src//main//resources//Photos//" + placeId + ".jpg";
    
    	try {
    	    // retrieve image
    	    File outputfile = new File(currentPath);
    	    ImageIO.write(img, "jpg", outputfile);
    	} catch (IOException e) {
    		System.out.println("Error in writing the image file");
    		e.printStackTrace();
    	}
    }


    
    /**
     * Writing to the csv file in the PATH
     * the first row is the FILE_HEADER
     */
    public static void  toCSVfile(String... args){
    	try{
    		if(writer!=null){
    			for(String arg : args){
    				writer.write(arg);
    				writer.write(TAB_DELIMITER);
    			}
    			writer.write(NEW_LINE_SEPARATOR);
    		}
    	}
    	catch (Exception e){
    		System.out.println("Error in CSV fileWriter");
    		e.printStackTrace();
    	}
    }
    
    public static void createCSVfile(String path){
    	try{
    		fileStream = new FileOutputStream(path);
    		writer = new OutputStreamWriter(fileStream, "UTF-8");
    	}
    	catch (Exception e){
    		System.out.println("Error in CSV fileWriter");		
    		e.printStackTrace();
    	}
    }
    
    public static void closeCSVfile(){
    	try{
    		if(writer!=null)
    			writer.close();
		}
		catch(IOException e){
			System.out.println("Error while closing the CSV fileWriter");
			e.printStackTrace();
		}
    
    }
    @Override
    public boolean isDebugModeEnabled() {
        return debugModeEnabled;
    }

    @Override
    public void setDebugModeEnabled(boolean debugModeEnabled) {
        this.debugModeEnabled = debugModeEnabled;
    }

    private void debug(String msg) {
        if (debugModeEnabled)
            System.out.println(msg);
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public RequestHandler getRequestHandler() {
        return requestHandler;
    }

    @Override
    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public List<Place> getNearbyPlaces(double lat, double lng, double radius, int limit, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_NEARBY_SEARCH, String.format(Locale.ENGLISH, "key=%s&location=%f,%f&radius=%f",
                    apiKey, lat, lng, radius), extraParams);
            return getPlaces(uri, METHOD_NEARBY_SEARCH, limit);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public List<Place> getNearbyPlaces(double lat, double lng, double radius, Param... extraParams) {
        return getNearbyPlaces(lat, lng, radius, DEFAULT_RESULTS, extraParams);
    }

    @Override
    public List<Place> getNearbyPlacesRankedByDistance(double lat, double lng, int limit, Param... params) {
        try {
            String uri = buildUrl(METHOD_NEARBY_SEARCH, String.format(Locale.ENGLISH, "key=%s&location=%f,%f&rankby=distance",
                    apiKey, lat, lng), params);
            return getPlaces(uri, METHOD_NEARBY_SEARCH, limit);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public List<Place> getNearbyPlacesRankedByDistance(double lat, double lng, Param... params) {
        return getNearbyPlacesRankedByDistance(lat, lng, DEFAULT_RESULTS, params);
    }

    @Override
    public List<Place> getPlacesByQuery(String query, int limit, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_TEXT_SEARCH, String.format("query=%s&key=%s", query, apiKey),
                    extraParams);
            return getPlaces(uri, METHOD_TEXT_SEARCH, limit);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public List<Place> getPlacesByQuery(String query, Param... extraParams) {
        return getPlacesByQuery(query, DEFAULT_RESULTS, extraParams);
    }

    @Override
    public List<Place> getPlacesByRadar(double lat, double lng, double radius, int limit, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_RADAR_SEARCH, String.format(Locale.ENGLISH, "key=%s&location=%f,%f&radius=%f",
                    apiKey, lat, lng, radius), extraParams);
            return getRadarPlaces(uri, METHOD_RADAR_SEARCH, limit);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public List<Place> getPlacesByRadar(double lat, double lng, double radius, Param... extraParams) {
        return getPlacesByRadar(lat, lng, radius, MAXIMUM_RESULTS, extraParams);
    }

    @Override
    public Place getPlaceById(String placeId, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_DETAILS, String.format("key=%s&placeid=%s", apiKey, placeId), extraParams);
            return Place.parseDetails(this, requestHandler.get(uri));
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public Place addPlace(PlaceBuilder builder, boolean returnPlace, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_ADD, String.format("key=%s", apiKey));
            JSONObject input = builder.toJson();
            HttpPost post = new HttpPost(uri);
            post.setEntity(new StringEntity(input.toString()));
            JSONObject response = new JSONObject(requestHandler.post(post));
            String status = response.getString(STRING_STATUS);
            checkStatus(status, response.optString(STRING_ERROR_MESSAGE));
            return returnPlace ? getPlaceById(response.getString(STRING_PLACE_ID)) : null;
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public void deletePlaceById(String placeId, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_DELETE, String.format("key=%s", apiKey), extraParams);
            JSONObject input = new JSONObject().put(STRING_PLACE_ID, placeId);
            HttpPost post = new HttpPost(uri);
            post.setEntity(new StringEntity(input.toString()));
            JSONObject response = new JSONObject(requestHandler.post(post));
            String status = response.getString(STRING_STATUS);
            checkStatus(status, response.optString(STRING_ERROR_MESSAGE));
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public void deletePlace(Place place, Param... extraParams) {
        deletePlaceById(place.getPlaceId(), extraParams);
    }

    protected BufferedImage download(String uri) {
        try {
            BufferedImage bi = requestHandler.getBufferedImage(uri);
            if (bi == null)
                throw new GooglePlacesException("Could not attain input stream at " + uri);
            debug("Successfully attained InputStream at " + uri);
            return bi;
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    protected BufferedImage downloadPhoto(Photo photo, int maxHeight, int maxWight) {
        try {
            String uri = String.format("%sphoto?photoreference=%s&key=%s", API_URL, photo.getReference(),
                    apiKey);
          
            //List<Param> params = new ArrayList<>();
            Param[] extraParams =  new Param [2];
            if (maxHeight != -1) extraParams[0] = (Param.name("maxheight").value(maxHeight));
            if (maxWight != -1) extraParams[1] = (Param.name("maxwidth").value(maxWight));
            uri = addExtraParams(uri, extraParams);

            return download(uri);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    private List<Prediction> getPredictions(String input, String method, Param... extraParams) {
        try {
            String uri = buildUrl(method, String.format("input=%s&key=%s", input, apiKey),
                    extraParams);
            String response = requestHandler.get(uri);
            return Prediction.parse(this, response);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public List<Prediction> getPlacePredictions(String input, int offset, int lat, int lng, int radius,
                                                Param... extraParams) {
        List<Param> params = new ArrayList<>();
        if (offset != -1)
            params.add(Param.name("offset").value(offset));
        if (lat != -1 && lng != -1)
            params.add(Param.name("location").value(lat + "," + lng));
        params.addAll(new ArrayList<>(Arrays.asList(extraParams)));

        return getPredictions(input, METHOD_AUTOCOMPLETE, params.toArray(new Param[params.size()]));
    }

    @Override
    public List<Prediction> getPlacePredictions(String input, int offset, Param... extraParams) {
        return getPlacePredictions(input, offset, -1, -1, -1, extraParams);
    }

    @Override
    public List<Prediction> getPlacePredictions(String input, Param... extraParams) {
        return getPlacePredictions(input, -1, extraParams);
    }

    @Override
    public List<Prediction> getQueryPredictions(String input, int offset, int lat, int lng, int radius,
                                                Param... extraParams) {
        List<Param> params = new ArrayList<>();
        if (offset != -1)
            params.add(Param.name("offset").value(offset));
        if (lat == -1 && lng == -1)
            params.add(Param.name("location").value(lat + "," + lng));
        params.addAll(new ArrayList<>(Arrays.asList(extraParams)));

        return getPredictions(input, METHOD_QUERY_AUTOCOMPLETE, params.toArray(new Param[params.size()]));
    }

    @Override
    public List<Prediction> getQueryPredictions(String input, int offset, Param... extraParams) {
        return getQueryPredictions(input, offset, -1, -1, -1, extraParams);
    }

    @Override
    public List<Prediction> getQueryPredictions(String input, Param... extraParams) {
        return getQueryPredictions(input, -1, extraParams);
    }

    private List<Place> getPlaces(String uri, String method, int limit) throws IOException {
        limit = Math.min(limit, MAXIMUM_RESULTS); // max of 60 results possible
        int pages = (int) Math.ceil(limit / (double) MAXIMUM_PAGE_RESULTS);

        List<Place> places = new ArrayList<>();
        // new request for each page
        for (int i = 0; i < pages; i++) {
            debug("Page: " + (i + 1));
            String raw = requestHandler.get(uri);
            debug(raw);
            String nextPage = parse(this, places, raw, limit);
            // reduce the limit, update the uri and wait for token, but only if there are more pages to read
            if (nextPage != null && i < pages - 1) {
                limit -= MAXIMUM_PAGE_RESULTS;
                uri = String.format("%s%s/json?pagetoken=%s&key=%s",
                        API_URL, method, nextPage, apiKey);
                sleep(3000); // Page tokens have a delay before they are available
            } else {
                continue;
            }
        }
        
        return places;
    }

    private List<Place> getRadarPlaces(String uri, String method, int limit) throws IOException {
     limit = Math.min(limit, MAXIMUM_RADAR_RESULTS); // max of 200 results possible
      List<Place> places = new ArrayList<>();
      String raw = requestHandler.get(uri);
      debug(raw);
      parseRadar(this, places, raw, limit);

      return places;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
