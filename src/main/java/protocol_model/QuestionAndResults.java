package protocol_model;

import java.util.List;

import model.Place;
import model.Vote;

public class QuestionAndResults {

	private Vote question;
	private List<Place> places;
	
	public Vote getQuestion() {
		return question;
	}
	public void setQuestion(Vote question) {
		this.question = question;
	}
	public List<Place> getPlaces() {
		return places;
	}
	public void setPlaces(List<Place> places) {
		this.places = places;
	}
	
	
	
}
