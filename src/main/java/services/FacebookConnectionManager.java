package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;

public class FacebookConnectionManager {

	private String accessToken;
	private String userID;
	private final String VALIDATE_TOKEN = "https://graph.facebook.com/me?access_token=%s";
	private final String CHECKINS = "https://graph.facebook.com/me/tagged_places?access_token=%s";

	public FacebookConnectionManager (String accessToken, String userID) {
		this.accessToken = accessToken;
		this.userID = userID;
	}
	
	public enum RequestType {
	    AUTH, CHECK_INS;
	}
	
	public Map sendFacebookRequest(RequestType reqType) throws IOException {
		String url = "";
		URL obj;
		HttpURLConnection con;
		
		if (reqType == RequestType.AUTH) {
			url = String.format(VALIDATE_TOKEN, this.accessToken);
		} else if (reqType == RequestType.CHECK_INS) {
			url = String.format(CHECKINS, this.accessToken);
		}
		
		obj = new URL(url);
		con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();

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
		JsonParserFactory factory=JsonParserFactory.getInstance();
		JSONParser parser=factory.newJsonParser();
		Map jsonMap = parser.parseJson(response.toString());
		return jsonMap;

	}
	
	
	
}
