package protocol_model;

import model.Location;

public class FacebookPlaceLocation {
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	
	public Location getLocationEntity() {
		Double lat = Double.parseDouble(this.getLatitude());
		Double lon = Double.parseDouble(this.getLongtitude());
		Location loc = new Location(lat,lon);
		return loc;
	}
	
	private String city;
	private String country;
	private String latitude;
	private String longtitude;
	private String street;
	
	


}
