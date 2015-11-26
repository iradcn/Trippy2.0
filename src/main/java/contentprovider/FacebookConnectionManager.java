package contentprovider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import protocol_model.FacebookAuthResponse;
import protocol_model.FacebookCheckInData;
import protocol_model.FacebookNextPage;

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
		
		return sendFacebookRequest(url);
	}
	
	public String sendFacebookRequest(String url) throws IOException {
		URL obj;
		HttpURLConnection con;
		
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
		return response.toString();
	}

	public FacebookAuthResponse sendAuthRequest() throws IOException {
		String respo = sendFacebookRequest(RequestType.AUTH);
		FacebookAuthResponse authResponse = new Gson().fromJson(respo, FacebookAuthResponse.class);
		return authResponse;
	}
	
	public List<FacebookCheckInData> sendCheckInRequest() throws IOException {
		List<FacebookCheckInData> faceBookCheckInDataLst = new ArrayList<>();
		String respo = sendFacebookRequest(RequestType.CHECK_INS);
		FacebookCheckInData checkInResponse = new Gson().fromJson(respo, FacebookCheckInData.class);
		faceBookCheckInDataLst.add(checkInResponse);
		FacebookNextPage nextPage = checkInResponse.getPaging();
		while (nextPage != null && nextPage.getNext() != null){
			String nextUrl = nextPage.getNext();
			respo = sendFacebookRequest(nextUrl);
			checkInResponse = new Gson().fromJson(respo, FacebookCheckInData.class);
			faceBookCheckInDataLst.add(checkInResponse);
			nextPage = checkInResponse.getPaging();
		}
		
		return faceBookCheckInDataLst;
	}

	
}
