package services;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import DAO.PlaceDAO;
import DAO.UserDAO;
import DAO.VoteDAO;
import model.Place;
import model.Vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class QuestionManagerService {

	@Autowired
	QuestionsGeneratorService questionGeneratorService;
	
	public Vote getQuestions() {

		Vote question = isExistsOpenQuestion();
		if (question != null) {
			return question;
		}

		if (!isRequiredNewQuestion()) {
			return null;
		}

		Place placeForQuestion = getPlaceForQuestion();
		Vote newQuestions = questionGeneratorService.generateThreeQuestions(placeForQuestion);

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
			return (UserDAO.getSentDataCounter(userId)%3 ==0);
		} catch (SQLException e) {
			System.out.println("Error SQLException while checking if a new question is required");
			return false;
		}
		
		
		
	}
	
	private Place getPlaceForQuestion () {
		return null;
	}
	
	public void insertNewQuestions (Vote questions) {
		//TODO: insert new questions to db

	}


}
