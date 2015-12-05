package model;

public class Vote extends AbstractEntity {

	private String placeId;
	private String placeName;
	private String[] property = new String[3];
	
	public Vote(String placeId, String placeName,  String[] property){
		this.placeId = placeId; 
		this.placeName = placeName;
		this.property = property; 
				 
	}
	public String[] getProperty() {
		return property;
	}
	private long nPlaceId;

	public long getnPlaceId() {
		return nPlaceId;
	}

	public void setnPlaceId(long nPlaceId) {
		this.nPlaceId = nPlaceId;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String place_name) {
		this.placeId = place_name;
	}

	public void setProperty (String[] prop_name) {
		this.property = prop_name;
	}

}
