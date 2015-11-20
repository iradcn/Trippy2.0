package protocol_model;

public class FacebookPlaceData {
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public FacebookPlaceLocation getLocation() {
		return location;
	}
	public void setLocation(FacebookPlaceLocation location) {
		this.location = location;
	}
	private String id;
	private String name;
	private FacebookPlaceLocation location;
	
}
