package model;

public class PropertyRank extends AbstractEntity {

	private int votesRank;
	
	public PropertyRank (String placeId, int propId, int votesRank){
		super.setGoogleId(placeId);
		super.setId(propId);
		this.votesRank = votesRank;
	}

	public int getVotesRank() {
		return votesRank;
	}

	public void setVotesRank(int votesRank) {
		this.votesRank = votesRank;
	}
}
