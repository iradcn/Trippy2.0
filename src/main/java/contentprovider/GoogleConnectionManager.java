package contentprovider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import model.GoogleSearchableEntity;
import model.Location;
import model.Place;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GoogleConnectionManager {

	private final String GEO_API = "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s";
	private final String PLACES_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"+
										"location=%s,%s&radius=%s&name=%s&key=%s";
	private final String RADIUS = "5000";
	private final String KEY = "AIzaSyDce9mYYCBRGUW5QLLDVYUXiLNl_CLc3cY";
	
	public Place queryPlace(GoogleSearchableEntity googleSearchableEntity) throws IOException, URISyntaxException {

		if (googleSearchableEntity.getLocation() == null) {
			try {
				updatePlaceLocation(googleSearchableEntity);
			} catch (RuntimeException ex) {
				System.out.println("error finding location for:"+googleSearchableEntity.getName());
				return null;
			}
		}
		Place newPlace = getPlaceByLocation(googleSearchableEntity);
		return newPlace;
	}
	
	
	public String queryGoogleUrl(String url) throws IOException, URISyntaxException {
		URL obj;
		HttpURLConnection con;
		
		obj = new URL(url);
	    URI uri = new URI(obj.getProtocol(), obj.getUserInfo(), obj.getHost(), obj.getPort(), obj.getPath(), obj.getQuery(), obj.getRef());
	    url=uri.toASCIIString();
	    obj = new URL(url);
	    con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());

		return response.toString();
	}
	
	public Place getPlaceByLocation (GoogleSearchableEntity googleSearchableEntity) throws IOException, URISyntaxException {
		
		String url = getNearPlaceApiUrl(googleSearchableEntity);
		String response = queryGoogleUrl(url);
		System.out.println(response.toString());
		try {
			
			JsonObject resultJson = new Gson().fromJson(response, JsonObject.class);
			JsonArray resultsArr = resultJson.get("results").getAsJsonArray();		
			String id = resultsArr.get(0).getAsJsonObject().get("id").getAsString();
			System.out.println("Found id="+id);
			return null;
			
		} catch (RuntimeException e) {
			System.out.println("error parsing json");
			return null;
		}
		
	}
	public void updatePlaceLocation(GoogleSearchableEntity googleSearchableEntity) throws IOException, URISyntaxException {
		
		String url = getGeoLocationApiUrl(googleSearchableEntity);
		String response = queryGoogleUrl(url);
			JsonObject resultJson = new Gson().fromJson(response, JsonObject.class);
			JsonArray resultsArr = resultJson.get("results").getAsJsonArray();
			JsonObject geometry = resultsArr.get(0).getAsJsonObject().get("geometry").getAsJsonObject();
			JsonObject location = geometry.get("location").getAsJsonObject();
			
			String lat = location.get("lat").getAsString();
			String lon = location.get("lng").getAsString();
			
			
			Location loc = new Location(Double.parseDouble(lat),Double.parseDouble(lon));
			
			googleSearchableEntity.setLocation(loc);
		
	}
	
	public String getNearPlaceApiUrl(GoogleSearchableEntity googleSearchableEntity) {
		
		String url = String.format(PLACES_API,googleSearchableEntity.getLocation().getLat(),googleSearchableEntity.getLocation().getLon()
									,RADIUS,googleSearchableEntity.getName(),KEY);
		return url;
	}
	
	public String getGeoLocationApiUrl(GoogleSearchableEntity googleSearchableEntity) {
		
		String url = String.format(GEO_API,googleSearchableEntity.getAddress(),KEY);
		return url;
		
	}

	
}
