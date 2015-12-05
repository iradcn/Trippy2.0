package services;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import DAO.VoteDAO;
import model.Place;
import model.Vote;


@Service
public class QuestionManagerService {

	@Autowired
	QuestionsGeneratorService questionGeneratorService;
	
	public Vote getQuestion() {
		return null;
	}

	public Vote isExistsOpenQuestion() {
		try {
			return VoteDAO.getOpenQuestion();
		} catch (SQLException e) {
			return null;
		}
	}
	
	public boolean isRequiredNewQuestion() {
		return false;
		
		
	}
	
	public Place getPlaceForQuestion () {
		return null;
	}
	
	
}
