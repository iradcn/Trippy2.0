package model;

public class GoogleSearchableEntity {

	private Location location;
	private String name;
	private String address;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public GoogleSearchableEntity (String name, Location location) {
		this.name = name;
		this.location = location;
	}
	
	public GoogleSearchableEntity (String name, String address) {
		this.name = name;
		this.address = address;
		this.location = null;
	}

	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
