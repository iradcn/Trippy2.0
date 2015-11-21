package contentprovider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import protocol_model.FacebookAuthResponse;
import protocol_model.FacebookCheckInData;

import com.google.gson.Gson;

public class FacebookConnectionManager {

	private String accessToken;
	private final String VALIDATE_TOKEN = "https://graph.facebook.com/me?access_token=%s";
	private final String CHECKINS = "https://graph.facebook.com/me/tagged_places?access_token=%s";

	public FacebookConnectionManager (String accessToken, String userID) {
		this.accessToken = accessToken;
	}
	
	public enum RequestType {
	    AUTH, CHECK_INS;
	}
	
	public String sendFacebookRequest(RequestType reqType) throws IOException {
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
	
	public FacebookAuthResponse sendAuthRequest() throws IOException {
		String respo = sendFacebookRequest(RequestType.AUTH);
		FacebookAuthResponse authResponse = new Gson().fromJson(respo, FacebookAuthResponse.class);
		return authResponse;
	}
	
	public FacebookCheckInData sendCheckInRequest() throws IOException {
		String respo = sendFacebookRequest(RequestType.CHECK_INS);
		FacebookCheckInData checkInResponse = new Gson().fromJson(respo, FacebookCheckInData.class);
		return checkInResponse;
	}

	
}
