package services;

import java.sql.SQLException;

import DAO.PlaceDAO;
import DAO.UserDAO;

public class ReliableManagerService {
	
	public void SetUserRelCounter(String placeId, int propertyId, int answer, String username) throws SQLException{
		int crowdRank = PlaceDAO.getPlaceRank(placeId, propertyId);
		//there wasn't data in the usersVote table then do dot increment or decrement the rel_counter
		if(crowdRank == 0){
			return;
		}
		else if (crowdRank > 0){
			if( answer > 0 ){
				//both think positive - the user is reliable
				UserDAO.IncrementUserRelCounter(username);
			}
			else {
				//the crowd think positive and the user negative - the user isn't reliable
				UserDAO.DecrementUserRelCounter(username);
			}
		}
		else if(crowdRank < 0){
			if( answer < 0 ){
				//both think negative - the user is reliable
				UserDAO.IncrementUserRelCounter(username);
			}
			else {
				//the crowd think negative and the user positive - the user isn't reliable
				UserDAO.DecrementUserRelCounter(username);
			}
		}
	}
	
}
