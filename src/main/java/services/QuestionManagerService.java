package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.Place;
import model.Vote;


@Service
public class QuestionManagerService {

	@Autowired
	QuestionsGeneratorService questionGeneratorService;
	
	public Vote getQuestion() {
		return null;
	}
	
	//ללכת לטבלה של vote וליוזר קיימת שאלה פתוחה 
	// 
	public Vote isExistsOpenQuestion() {
		return null;
	}
	
	public boolean isRequiredNewQuestion() {
		return false;
		
		
	}
	
	public Place getPlaceForQuestion () {
		return null;
	}
	
	
}
