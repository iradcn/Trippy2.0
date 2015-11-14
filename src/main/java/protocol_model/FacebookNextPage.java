package protocol_model;

import java.util.Map;

public class FacebookNextPage {
	
	public String getNext() {
		return next;
	}
	public void setNext(String next) {
		this.next = next;
	}
	public Map<String, String> getCursors() {
		return cursors;
	}
	public void setCursors(Map<String, String> cursors) {
		this.cursors = cursors;
	}
	String next;
	Map <String,String> cursors;
	
	
}
