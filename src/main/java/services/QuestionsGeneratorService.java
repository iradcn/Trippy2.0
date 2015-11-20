package services;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

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
                "propId", "votesRank", "fTimestamp") {
        };

    };
    public String[] generate(String placeId) throws TasteException {
        UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, dataModel);
        UserBasedRecommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);

        List<RecommendedItem> recommendations = recommender.recommend(2, 3);
        for (RecommendedItem recommendation : recommendations) {
            System.out.println(recommendation);
        }

        return null;

    }


}
