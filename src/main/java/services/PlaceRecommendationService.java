package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import model.Place;
import model.Property;
import model.User;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import DAO.PlaceDAO;
import DAO.PropertyDAO;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

@Service
public class PlaceRecommendationService {
    DataSource dataSource;
    private final JDBCDataModel dataModel;

    public PlaceRecommendationService() {
        MysqlDataSource dataSource = new MysqlDataSource ();
        dataSource.setServerName("localhost");
        dataSource.setUser("trippy2");
        dataSource.setPassword("trippy2");
        dataSource.setDatabaseName("trippy2");

        this.dataModel = new MySQLJDBCDataModel(
                dataSource, "user_checkins_agg_view", "user_id",
                "n_id", "hasVisited", "fTimestamp") {
        };
    }
    public List<Place> generatePlaceRecommendation(String userId) {
		try {

			// Get property from similar places
			ReloadFromJDBCDataModel reloadFromJDBCDataModel = new ReloadFromJDBCDataModel(dataModel);
			UserSimilarity similarity = new TanimotoCoefficientSimilarity(reloadFromJDBCDataModel);
			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.2, similarity, reloadFromJDBCDataModel);
			UserBasedRecommender recommender = new GenericUserBasedRecommender(reloadFromJDBCDataModel, neighborhood, similarity);

			List<RecommendedItem> recommendations = recommender.recommend(Long.parseLong(userId), 3);
			List<Place> foundPlaces = new ArrayList<>();
			for (RecommendedItem recommendation : recommendations) {
				if (recommendation.getValue() > 0) {
					int nPlaceId = (int) recommendation.getItemID();
					Place foundPlace = PlaceDAO.getPlaceByNplaceId(nPlaceId);
					foundPlaces.add(foundPlace);
					System.out.println("Got Place by similarity");
				}
			}
			System.out.println(String.format("Found %d places by simillarity", foundPlaces.size()));
			if (foundPlaces != null)
				return foundPlaces;
		}

		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		System.out.println("returning null");
		return null;
    }
}
