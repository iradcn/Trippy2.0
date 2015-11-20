package model;

public class Vote extends AbstractEntity {

	private String placeId;

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	private String placeName;
	private String[] property = new String[3];

	public byte[] getPlaceImage() {
		return placeImage;
	}

	public void setPlaceImage(byte[] placeImage) {
		this.placeImage = placeImage;
	}

	private byte[] placeImage;

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String place_name) {
		this.placeId = place_name;
	}

	public String[] getProperty() {
		return property;
	}

	public void setProperty (String[] prop_name) {
		this.property = prop_name;
	}
}
