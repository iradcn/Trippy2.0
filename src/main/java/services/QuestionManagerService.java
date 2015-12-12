package services;

import java.sql.SQLException;
import java.util.Set;

import DAO.PlaceDAO;
import model.Place;
import model.Vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import DAO.UserDAO;
import DAO.VoteDAO;


@Service
public class QuestionManagerService {

	@Autowired
	QuestionsGeneratorService questionGeneratorService;
	
	public Vote getOpenQuestions () {
		Vote question = isExistsOpenQuestion();
		if (question != null) {
			return question;
		}
		return null;
	}
	public Vote getQuestions() {


		if (!isRequiredNewQuestion()) {
			return null;
		}

		Place placeForQuestion = getPlaceForQuestion();
		Vote newQuestions = questionGeneratorService.generateThreeQuestions(placeForQuestion);
		newQuestions.setPlaceId(placeForQuestion.getGoogleId());
		newQuestions.setName(placeForQuestion.getName());
		return newQuestions;
	}

	public Vote isExistsOpenQuestion() {
		try {
			return VoteDAO.getOpenQuestion();
		} catch (SQLException e) {
			return null;
		}
	}
	
	
	private boolean isRequiredNewQuestion()  {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			int numOfSentRqsts = UserDAO.getSentDataCounter(userId);
			if (numOfSentRqsts == -1)
				throw new SQLException();
			
			return (numOfSentRqsts%3 ==0);
		} catch (SQLException e) {
			System.out.println("Error SQLException while checking if a new question is required");
			return false;
		}
	}
	
	public Place getPlaceForQuestion () {
		try {
			String userId = SecurityContextHolder.getContext().getAuthentication().getName();
			Set<String> candidateCheckInPlaces = UserDAO.getUserCheckInPlaces(userId);
			Set<String> questionedPlaces = VoteDAO.getPlacesUserVotes(userId);
			for (String cPlace : candidateCheckInPlaces) {
				if (!questionedPlaces.contains(cPlace)) {
					return (PlaceDAO.getPlace(cPlace));
				}
			}

			// Todo - Find random place or place in the area

		}
		catch (Exception Ex)
		{

		}
		//TODO: Change to random place
		//if no place in checkins, ask for dizingoff club
		return new Place("ChIJUZozUX9MHRURRh_1kxgz3bQ","dizingoff club",null,null);
	}
	
	public void insertNewQuestions (Vote questions) {
		for(int i=0;i<questions.getProperty().length;i++){
			try {
				String userId = SecurityContextHolder.getContext().getAuthentication().getName();
				VoteDAO.insertNewQuestion(questions.getProperty()[i].getId(), questions.getPlaceId(), 0, userId, questions.getnPlaceId());
			} catch (SQLException e) {
				System.out.println("Error inserting new question to data base");
			}
		}

	}

	public void setQuestionAsDone(String placeId, int propertyId, int answer, String userId, long intPlaceId) {
		try {
			VoteDAO.setQuestionAsAnswered(propertyId, placeId, answer, userId, intPlaceId);
		} catch (SQLException e) {
			System.out.println("Error setting question answer.");
		}
	}
}
