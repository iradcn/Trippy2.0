package contentprovider;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import model.GoogleSearchableEntity;
import model.Place;

import org.springframework.stereotype.Service;

import protocol_model.FacebookPlaceData;
import protocol_model.FacebookPlaceLocation;

@Service
public class GoogleService {
	
	
	private GoogleConnectionManager googleConnectionManager = new GoogleConnectionManager();

	public List<Place> getGooglePlaces(List<FacebookPlaceData> facebookData) {
		
		List<Place> foundPlaces = new ArrayList<>();
		for (FacebookPlaceData facebookPlace:facebookData) {
			
			String placeName = facebookPlace.getName();
			StringBuilder sb = new StringBuilder();
			
			if (placeName == null || placeName.equals(""))
				continue;
			
			//minimal search is by name
			GoogleSearchableEntity googleSearchableEntity;
			FacebookPlaceLocation fbLocation = facebookPlace.getLocation();
			
			if (fbLocation != null) {
				
				if (fbLocation.getLatitude() == null || fbLocation.getLatitude().equals("") || 
						fbLocation.getLongtitude() == null || fbLocation.getLongtitude().equals("")) {
					
					if (fbLocation.getStreet() != null && !fbLocation.getStreet().equals("")) {
						sb.append(fbLocation.getStreet());
					}
					
					if (fbLocation.getCity() != null && !fbLocation.getCity().equals("")) {
						sb.append("+").append(fbLocation.getCity());
					}

					if (fbLocation.getCountry() != null && !fbLocation.getCountry().equals("")) {
						sb.append("+").append(fbLocation.getCountry());
					}
					
					googleSearchableEntity = new GoogleSearchableEntity(placeName,sb.toString());					
				} else {
					
					googleSearchableEntity = new GoogleSearchableEntity(placeName, facebookPlace.getLocation().getLocationEntity());			
				}

			} else { //no location - cannot query google
				System.out.println("No valid Location found for"+placeName+" ,skipping.");
				continue;		
			}
			
			
			try {
				Place newPlace = googleConnectionManager.queryPlace(googleSearchableEntity);
				if (newPlace != null) {
					foundPlaces.add(newPlace);
				}
			} catch (IOException e) {
				System.out.println("Couldnt query "+googleSearchableEntity.getName());
			} catch (URISyntaxException e) {
				System.out.println("Couldnt query "+googleSearchableEntity.getName());

			}
			
		}
		return foundPlaces;
		
	}
}
