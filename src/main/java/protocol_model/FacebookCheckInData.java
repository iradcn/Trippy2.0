package protocol_model;

import java.util.List;

public class FacebookCheckInData {
	
	public List<FacebookPlaceEntry> getData() {
		return data;
	}
	public void setData(List<FacebookPlaceEntry> data) {
		this.data = data;
	}
	public FacebookNextPage getPaging() {
		return paging;
	}
	public void setPaging(FacebookNextPage paging) {
		this.paging = paging;
	}
	List<FacebookPlaceEntry> data;
	FacebookNextPage paging;

}
