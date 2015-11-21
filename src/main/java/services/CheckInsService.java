package services;

import java.io.IOException;
import java.util.List;

import model.Place;

import org.springframework.beans.factory.annotation.Autowired;

import protocol_model.FacebookPlaceData;
import contentprovider.FacebookService;
import contentprovider.GoogleService;

public class CheckInsService implements Runnable {
	private Thread t;
	private FacebookService faceBookService;
	
	@Autowired
	GoogleService googleService = new GoogleService();
	
	public CheckInsService(FacebookService facebookService) {
		this.faceBookService = facebookService;
	}
	@Override
	public void run() {
		try {
			List<FacebookPlaceData> faceBookPlaces = faceBookService.getUserCheckIns();
			List<Place> foundPlaces = googleService.getGooglePlaces(faceBookPlaces);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startThread() {
		this.t = new Thread(this);
		t.start();
	}

}
