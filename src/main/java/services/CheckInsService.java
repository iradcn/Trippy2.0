package services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Place;
import model.User;
import protocol_model.FacebookPlaceData;
import DAO.PlaceDAO;
import DAO.UserDAO;
import contentprovider.FacebookService;
import contentprovider.GoogleService;

public class CheckInsService implements Runnable {
	
	private Thread t;
	private FacebookService faceBookService;
	private GoogleService googleService = new GoogleService();
	private User user;
	
	public CheckInsService(FacebookService facebookService, User user) {
		this.faceBookService = facebookService;
		this.user = user;
	}
	
	@Override
	public void run() {
		try {
			//TODO: Check last update time
			//TODO: Pass filter existing checkins by date param
			List<FacebookPlaceData> faceBookPlaces = faceBookService.getUserCheckIns();

			
			System.out.println("Found:"+faceBookPlaces.size()+" Checkins. Analyzing..");
			List<Place> foundCandidatePlaces = googleService.getGooglePlaces(faceBookPlaces);
			System.out.println("Found:"+foundCandidatePlaces.size()+" Google Matches.. Fetching DB");
			List<Place> confirmedCandidates = new ArrayList<>();
			for (Place placeCandidate:foundCandidatePlaces) {
				try {
					Place foundPlace = PlaceDAO.getPlace(placeCandidate.getGoogleId());
					if (foundPlace != null) {
						System.out.println("Place "+placeCandidate.getName()+ " found in DB");
						confirmedCandidates.add(foundPlace);
					} else {
						//System.out.println("Place "+placeCandidate.getName()+ " is not in DB");
					}
				} catch (SQLException e) {
					System.out.println("Couldnt query DB for "+placeCandidate.getName());
				}
			}
			if (confirmedCandidates.size()>0) {
				try {
					UserDAO.InsertUserPlace(confirmedCandidates, this.user);
				} catch (SQLException e) {
					System.out.println("Error Inserting confirmed candidated CheckIns to DB - maybe exists");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startThread() {
		this.t = new Thread(this);
		t.start();
	}

}
