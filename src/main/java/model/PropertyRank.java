package model;

public class PropertyRank extends AbstractEntity {

	private int votesRank;
	
	public PropertyRank (String placeId, int propId, int votesRank, String name){
		super.setGoogleId(placeId);
		super.setId(propId);
		super.setName(name);
		this.votesRank = votesRank;
	}

	public int getVotesRank() {
		return votesRank;
	}

	public void setVotesRank(int votesRank) {
		this.votesRank = votesRank;
	}
}
