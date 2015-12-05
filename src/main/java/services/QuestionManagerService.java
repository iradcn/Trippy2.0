package services;

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
	
	//ללכת לטבלה של vote וליוזר קיימת שאלה פתוחה 
	// 
	private Vote isExistsOpenQuestion() {
		return null;
	}
	
	private boolean isRequiredNewQuestion() {
		return false;
		
		
	}
	
	private Place getPlaceForQuestion () {
		return null;
	}
	
	public void insertNewQuestions (Vote questions) {
		//TODO: insert new questions to db
		
	}
	
	
}
