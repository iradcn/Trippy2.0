package services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import model.Place;
import protocol_model.FacebookPlaceData;
import DAO.PlaceDAO;
import contentprovider.FacebookService;
import contentprovider.GoogleService;

public class CheckInsService implements Runnable {
	private Thread t;
	private FacebookService faceBookService;
	
	GoogleService googleService = new GoogleService();
	
	public CheckInsService(FacebookService facebookService) {
		this.faceBookService = facebookService;
	}
	@Override
	public void run() {
		try {
			List<FacebookPlaceData> faceBookPlaces = faceBookService.getUserCheckIns();
			System.out.println("Found:"+faceBookPlaces.size()+" Checkins. Analyzing..");
			List<Place> foundCandidatePlaces = googleService.getGooglePlaces(faceBookPlaces);
			System.out.println("Found:"+foundCandidatePlaces.size()+" Google Matches.. Fetching DB");
			for (Place placeCandidate:foundCandidatePlaces) {
				try {
					Place foundPlace = PlaceDAO.getPlace(placeCandidate.getGoogleId());
					if (foundPlace != null) {
						System.out.println("Place "+placeCandidate.getName()+ " found in DB");
					} else {
						System.out.println("Place "+placeCandidate.getName()+ " is not in DB");
					}
				} catch (SQLException e) {
					System.out.println("Couldnt query DB for "+placeCandidate.getName());
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
