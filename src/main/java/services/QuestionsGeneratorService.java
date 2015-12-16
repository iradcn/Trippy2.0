package services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import model.Place;
import model.Property;
import model.PropertyRank;
import model.Vote;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.stereotype.Service;

import DAO.PlaceDAO;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * Created by nimrodoron on 11/17/15.
 */
@Service
public class QuestionsGeneratorService {

    //@Autowired
    DataSource dataSource;

    private JDBCDataModel dataModel;
    private static boolean include;
    
    public QuestionsGeneratorService() {
        MysqlDataSource dataSource = new MysqlDataSource ();
        dataSource.setServerName("localhost");
        dataSource.setUser("trippy2");
        dataSource.setPassword("trippy2");
        dataSource.setDatabaseName("trippy2");

        dataModel = new MySQLJDBCDataModel(
                dataSource, "user_votes_agg_view", "placeId",
                "propId", "votesRank", "fTimestamp") {
        };

    };
    public Property generateCorrelatedProperty(Place place) {
		try {

			ReloadFromJDBCDataModel reloadFromJDBCDataModel = new ReloadFromJDBCDataModel(dataModel);
        	UserSimilarity similarity = new TanimotoCoefficientSimilarity(reloadFromJDBCDataModel);
       	 	UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.2, similarity, reloadFromJDBCDataModel);
        	UserBasedRecommender recommender = new GenericUserBasedRecommender(reloadFromJDBCDataModel, neighborhood, similarity);

			List<RecommendedItem> recommendations = recommender.recommend(place.getnId(), 5);
			for (RecommendedItem recommendation : recommendations) {
				if (recommendation.getItemID() >= 24)
					return new Property((int) recommendation.getItemID());
			}
		}
		catch (Exception ex) {

		}
		return this.getMockProperty();
    }

    private Property getMockProperty() {
		Property prop = new Property();
		prop.setId(1500);
		prop.setName("Hila Friendly");
		return prop;
	}
	public Vote generateThreeQuestions(Place place) {
    	//getMockStuff - getMockData()
    	Vote vote = new Vote();
    	Property[] propertyArr = new Property[3];
    	propertyArr[0] = generateNewPropertyVote(place);
    	propertyArr[1] = generatePopularProperty(place);
    	propertyArr[2] = generateCorrelatedProperty(place);
    	vote.setProperty(propertyArr);
    	return vote;
    }
    
    public Property generateNewPropertyVote(Place place) {    	
    	try {
    		int minVoteRank = 0;
    		include = true;
			List<PropertyRank> resultQuery = null;
			List<PropertyRank> subResult = null;
			
			resultQuery = PlaceDAO.getPlacesWithRanks(place.getGoogleId());
			for(int i=0 ; i< resultQuery.size();i++){
				if(minVoteRank > Math.abs(resultQuery.get(i).getVotesRank())){
					minVoteRank = Math.abs(resultQuery.get(i).getVotesRank());
				}
			}
			subResult = getSubListByRank(resultQuery, minVoteRank);
			if (subResult.size() == 0 ) {
	    		include = false;
				subResult = getSubListByRank(resultQuery, minVoteRank);
			}

			return getRandomProperty(subResult);
			
		} catch (SQLException e) {
			System.out.println("Error in getting properties form data base");//TODO: select random place to ask about?
			return null;
		}
    }
    
    public Property generatePopularProperty(Place place) {
    	try {
    		int minVoteRank = 0;
    		include = false;
			List<PropertyRank> resultQuery = null;
			List<PropertyRank> subResult = null;
			
			resultQuery = PlaceDAO.getPlacesWithRanks(place.getGoogleId());
			for(int i=0 ; i< resultQuery.size();i++){
				if(minVoteRank > Math.abs(resultQuery.get(i).getVotesRank())){
					minVoteRank = Math.abs(resultQuery.get(i).getVotesRank());
				}
			}
			subResult = getSubListByRank(resultQuery, minVoteRank);
			if (subResult.size() == 0 ) {
	    		include = true;
				subResult = getSubListByRank(resultQuery, minVoteRank);
			}
			return getRandomProperty(subResult);
			
		} catch (SQLException e) {
			System.out.println("Error in getting properties form data base"); //TODO: select random place to ask about?
			return null;
		}
    }
    
    public List<PropertyRank> getSubListByRank (List<PropertyRank> original, int minVoteRank){
    	List<PropertyRank> result = new ArrayList<>();
    	for(int i=0; i<original.size(); i++){
    		//get new properties.
    		//add to the list the properties with voteRank =  minVoteRank
    		if(Math.abs(original.get(i).getVotesRank()) == minVoteRank){
    			if(include){
    				result.add(original.get(i));
    			}
    		}
    		//get the popular properties.
    		//add to the list the properties with voteRank >  minVoteRank
    		else if(Math.abs(original.get(i).getVotesRank()) != minVoteRank){
    			if(!include){
    				result.add(original.get(i));
    			}
    		}
    	}
    	return result;
    }
    
    public Property getRandomProperty (List<PropertyRank> original){
    	PropertyRank propertyRank = null;
    	Random rand = new Random();
    	int index = rand.nextInt(original.size());
    	propertyRank = original.get(index);
    	return new Property(propertyRank.getId(),propertyRank.getName());
    }
    
    private void getMockData(){
    	/*
    	Property prop1 = new Property(26);
    	Property prop2 = new Property(27);
    	Property prop3 = new Property(28);
    	
    	prop1.setName("Dog Friendly");
    	prop2.setName("Cat Friendly");
    	prop3.setName("Hila Friendly");
    	Property[] propArr = {prop1,prop2,prop3};
    	Vote vote = new Vote();
    	vote.setProperty(propArr);
    	vote.setPlaceId("ChIJ8br0vm5nAhURLIwIAA7PV2A");
    	vote.setnPlaceId(1);
    	vote.setName("Shufersal");
    	
    	prop1.setName("Dog Friendly");
    	prop2.setName("Cat Friendly");
    	prop3.setName("Hila Friendly");
    	
    	//for checking the two question algorithems
    	//may be deleted
    	
//    	Set<Property> propArr = new HashSet<>();
//    	Place tmp = new Place("ChIJ8br0vm5nAhURLIwIAA7PV2A");
//    	Property prop1 = new Property(21);
//    	Property prop2 = new Property(22);
//    	Property prop3 = new Property(23);
//    	Property prop4 = new Property(24);
//    	Property prop5 = new Property(25);
//    	Property prop6 = new Property(26);
//    	propArr.add(prop1);
//    	propArr.add(prop2);
//    	propArr.add(prop3);
//    	propArr.add(prop4);
//    	propArr.add(prop5);
//    	propArr.add(prop6);
//    	
//    	tmp.setProperties(propArr);
//    	Property p1A = generateNewPropertyVote (tmp);
//    	Property p1B = generateNewPropertyVote (tmp);
//    	Property p1C = generateNewPropertyVote (tmp);
//    	Property p2A = generatePopularProperty (tmp);
//    	Property p2B = generatePopularProperty (tmp);
//    	Property p2C = generatePopularProperty (tmp);
    	*/
    }
}
