package model;

public class UserPlace {

	private User user;
	private Place place;
	
	public UserPlace (User user, Place place) {
		this.user = user;
		this.place = place;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	
}
