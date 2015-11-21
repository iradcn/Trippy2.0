package contentprovider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.User;
import protocol_model.FacebookAuthResponse;
import protocol_model.FacebookCheckInData;
import protocol_model.FacebookPlaceData;
import protocol_model.FacebookPlaceEntry;

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

	public List<FacebookPlaceData> getUserCheckIns() throws IOException {
		FacebookCheckInData checkInData = facebookConnectionManager.sendCheckInRequest();
		List<FacebookPlaceEntry> facebookPlaceEntry = checkInData.getData();
		
		List<FacebookPlaceData> facebookPlaceData = new ArrayList<>();

		for (FacebookPlaceEntry placeEntry:facebookPlaceEntry) {
			facebookPlaceData.add(placeEntry.getPlace());
		}
		
		return facebookPlaceData;
	}
}
