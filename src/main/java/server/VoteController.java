package server;

import java.util.List;

import model.Vote;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

import DAO.CategoryDAO;

@RestController
public class VoteController {
	@RequestMapping(value="app/vote/request", method=RequestMethod.GET)
	public @ResponseBody Vote getSomeStuff() throws Exception {
    Vote vote = new Vote();
    vote.setPlace("place1");
    vote.setProperty("prop1");
    return vote;
	}
}
