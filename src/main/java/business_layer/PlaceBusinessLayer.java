package business_layer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Place;
import model.Vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import protocol_model.QuestionAndResults;
import protocol_model.SearchByLocation;
import services.QuestionManagerService;
import DAO.UserDAO;
import DAO.VoteDAO;

@Service
public class PlaceBusinessLayer {
	
	@Autowired
	QuestionManagerService newQuestionManager;

	@Autowired
	VoteDAO voteDAO;
	
	public QuestionAndResults getPlacesOrQuestion(SearchByLocation searchQueryJson) {
		
		QuestionAndResults questionOrResult = new QuestionAndResults();
		List<Place> places;
		String userId;
		Vote newQuestion = newQuestionManager.getQuestions();
		if (newQuestion == null) {
			try {
				places = Place.getPlacesByLocation(searchQueryJson);
			} catch (SQLException e) {
				places = new ArrayList<>();
				System.out.println("Error Fetching places, return empty results.");
			}
			questionOrResult.setPlaces(places);
			userId = SecurityContextHolder.getContext().getAuthentication().getName();
			try {
				UserDAO.IncrementUserNumSearches(userId);
			} catch (SQLException e) {
				System.out.println("Error Incrementing User Search Counter.");
			}
		} else {
			questionOrResult.setQuestion(newQuestion);
			newQuestionManager.insertNewQuestions(newQuestion);
		}

		return questionOrResult;
		
		
	}

	
}
