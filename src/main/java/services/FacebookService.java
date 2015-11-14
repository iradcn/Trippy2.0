package services;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

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
		Map jsonMap = facebookConnectionManager.sendFacebookRequest(RequestType.AUTH);
		if (jsonMap.get("id").equals(this.userID))
			return true;
		return false;
	}
	
	public User createUserByFB() {
		UUID newPassword = UUID.randomUUID();
		User newUser = new User(this.userID.toString(),newPassword.toString(),this.accessToken,1);
		return newUser;
	}

	public void saveUserCheckIns() throws IOException {
		Map jsonMap = facebookConnectionManager.sendFacebookRequest(RequestType.CHECK_INS);
		System.out.print(jsonMap.toString());
	}
}
