package services;

import java.util.List;

import javax.sql.DataSource;

import model.Place;
import model.Property;
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

/**
 * Created by nimrodoron on 11/17/15.
 */
@Service
public class QuestionsGeneratorService {

    //@Autowired
    DataSource dataSource;

    private JDBCDataModel dataModel;

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
    	return null;
    }
    
    public Property generatePopularProperty(Place place) {
    	return null;
    }
}
