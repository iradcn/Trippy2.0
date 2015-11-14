package services;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONArray;

import protocol_model.FacebookAuthResponse;
import protocol_model.FacebookCheckInData;
import protocol_model.FacebookPlaceEntry;
import model.User;
import services.FacebookConnectionManager.RequestType;

public class FacebookService {
	
	private String accessToken;
	private String userID;
	private FacebookConnectionManager facebookConnectionManager;
	
	public FacebookService (String accessToken, String userID) {
		this.accessToken = accessToken;
		this.userID = userID;
		this.facebookConnectionManager = new FacebookConnectionManager(accessToken, userID);
	}
	
	public void init() throws IOException {
	}
	
	public boolean isLoggedToFB() throws IOException {
		FacebookAuthResponse authRes = facebookConnectionManager.sendAuthRequest();
		if (authRes.getId().equals(this.userID))
			return true;
		return false;
	}
	
	public User createUserByFB() {
		UUID newPassword = UUID.randomUUID();
		User newUser = new User(this.userID.toString(),newPassword.toString(),this.accessToken,1);
		return newUser;
	}

	public void saveUserCheckIns() throws IOException {
		FacebookCheckInData checkInData = facebookConnectionManager.sendCheckInRequest();
		List<FacebookPlaceEntry> entriesList = checkInData.getData();
		System.out.print(entriesList.toString());
	}
}
