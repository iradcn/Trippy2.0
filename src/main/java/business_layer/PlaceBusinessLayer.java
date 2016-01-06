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
import services.PlaceRecommendationService;
import services.QuestionManagerService;
import DAO.CategoryDAO;
import DAO.PlaceDAO;
import DAO.PropertyDAO;
import DAO.UserDAO;

@Service
public class PlaceBusinessLayer {
	
	@Autowired
	QuestionManagerService newQuestionManager;
	
	@Autowired
	PlaceRecommendationService placeRecommendationService;
	
	public QuestionAndResults getPlacesOrQuestion(SearchByLocation searchQueryJson) {
		
		QuestionAndResults questionOrResult = new QuestionAndResults();
		List<Place> places;
		String userId;
		
		Vote newQuestion;
		newQuestion = newQuestionManager.getOpenQuestions();
		
		if (newQuestion != null) {
			questionOrResult.setQuestion(newQuestion);
			return questionOrResult;
		}
		newQuestion = newQuestionManager.getQuestions();
		
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

	public Place getPlace(String placeId) {
		Vote openQuestion = newQuestionManager.getOpenQuestions();
		Place place;
		if (openQuestion != null) {
			return null;
		}
		try {
			place = PlaceDAO.getPlace(placeId);
			place.setProperties(PropertyDAO.getPropertyListByPlace(placeId));
			place.setCategories(CategoryDAO.getPlaceCategories(placeId));
			return place;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Place> getPlacesRecommendation() {
		
		List<Place> foundPlaces = placeRecommendationService.generatePlaceRecommendation(SecurityContextHolder.getContext().getAuthentication().getName());
		if (foundPlaces!=null)
			return foundPlaces;
		
		return null;
	
	}
}
