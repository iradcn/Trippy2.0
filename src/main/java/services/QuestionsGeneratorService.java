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
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import DAO.PlaceDAO;

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
                "propId", "votesRankBin", "fTimestamp") {
        };

    };
    public Property generateCorrelatedProperty(long placeId) throws TasteException {/*
        UserSimilarity similarity = new TanimotoCoefficientSimilarity(dataModel);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, dataModel);
        UserBasedRecommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);

        List<RecommendedItem> recommendations = recommender.recommend(placeId, 1);
        for (RecommendedItem recommendation : recommendations) {
            return recommendation.getItemID();
        }

        return -1;*/
    	return null;

    }

    public Vote generateThreeQuestions(Place place) {
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
    	return vote;
    }
    
    public Property generateNewPropertyVote(Place place) {
    	try {
    		int minVoteRank = 0;
    		include = true;
			List<PropertyRank> resultQuery = null;
			List<PropertyRank> subResult = null;
			
			resultQuery = PlaceDAO.getPlacesWithRanks(place.getGoogleId());
			minVoteRank  = resultQuery.get(0).getVotesRank();
			subResult = getSubListByRank(resultQuery, minVoteRank);
			return getRandomProperty(subResult);
			
		} catch (SQLException e) {
			System.out.println("Error in getting properties form data base");
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
			minVoteRank  = resultQuery.get(0).getVotesRank();
			subResult = getSubListByRank(resultQuery, minVoteRank);
			return getRandomProperty(subResult);
			
		} catch (SQLException e) {
			System.out.println("Error in getting properties form data base");
			return null;
		}
    }
    
    public List<PropertyRank> getSubListByRank (List<PropertyRank> original, int minVoteRank){
    	List<PropertyRank> result = new ArrayList<>();
    	for(int i=0; i<original.size(); i++){
    		//get new properties.
    		//add to the list the properties with voteRank =  minVoteRank
    		if(original.get(i).getVotesRank() == minVoteRank){
    			if(include){
    				result.add(original.get(i));
    			}
    		}
    		//get the popular properties.
    		//add to the list the properties with voteRank >  minVoteRank
    		else if(original.get(i).getVotesRank() != minVoteRank){
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
    	return new Property(propertyRank.getId());
    }
}
