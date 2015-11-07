package model;

public class User {
	private String userId;
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}

	private String password;
	private String accessToken;
	private int isEnabled;

	public User(String userId, String password, String accessToken, int isEnabled) {
		this.userId = userId;
		this.password = password;
		this.accessToken = accessToken;
		this.isEnabled = isEnabled;
	}
}
