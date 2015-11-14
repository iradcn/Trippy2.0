package protocol_model;

import java.util.List;

public class FacebookPlaceEntry {
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreated_time() {
		return created_time;
	}
	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}

	private String id;
	private String created_time;
	public FacebookPlaceData getPlace() {
		return place;
	}
	public void setPlace(FacebookPlaceData place) {
		this.place = place;
	}

	private FacebookPlaceData place;
	
}
