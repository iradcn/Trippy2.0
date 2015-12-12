package server;

import java.sql.SQLException;

import model.EnumVoteValue;
import model.Place;
import model.VoteAnswer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import DAO.UserDAO;
import DAO.VoteDAO;
import protocol_model.QuestionAndResults;
import services.QuestionManagerService;
import services.QuestionsGeneratorService;

@RestController
public class VoteController {

    @Autowired
    QuestionsGeneratorService questionsGeneratorService;

    @Autowired
    QuestionManagerService questionManagerService;
    
    @RequestMapping(value="app/vote/answer", method=RequestMethod.POST)
    public QuestionAndResults answerVoteQuestion(@RequestBody VoteAnswer answer) throws SQLException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Save the votes
        for (int i = 0; i < answer.getAnswers().length; i++) {
            if (EnumVoteValue.isLegalValue(answer.getAnswers()[i])) {
            	questionManagerService.setQuestionAsDone(answer.getPlaceId(), answer.getProperty()[i].getId(), answer.getAnswers()[i], username, answer.getnPlaceId());
            }
        }
        UserDAO.IncrementUserNumSearches(username);
        QuestionAndResults questionAndResults = new QuestionAndResults();
        questionAndResults.setPlaces(Place.getPlacesByLocation(answer.getQuery()));
        return questionAndResults;
    }
}
