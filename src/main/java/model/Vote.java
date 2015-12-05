package model;

public class Vote extends AbstractEntity {

	private String placeId;
	private Property[] property = new Property[3];
	private long nPlaceId;
	
	public Vote(String placeId, String placeName,  Property[] property, long nPlaceId) {
		this.placeId = placeId; 
		super.setName(placeName);
		this.property = property;
		this.nPlaceId = nPlaceId;
	}

	public Vote() {}

	public Property[] getProperty() {
		return property;
	}
	

	public long getnPlaceId() {
		return nPlaceId;
	}

	public void setnPlaceId(long nPlaceId) {
		this.nPlaceId = nPlaceId;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String place_name) {
		this.placeId = place_name;
	}

	public void setProperty (Property[] prop_name) {
		this.property = prop_name;
	}

}
