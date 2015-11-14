package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.parser.ParseException;

import DAO.PlaceDAO;
import protocol_model.ResultMultipleSearch;
import protocol_model.SearchByLocation;
import protocol_model.SearchByMultipleLocation;

/**
 * Created by nimrod on 5/24/15.
 */
public class Place extends AbstractEntity {

    private Location loc;
    private Set<Category> categories;
    private Set<Property> properties;

    public Place(String googleId) {
        super.setGoogleId(googleId);
        super.myType = typeOf.Place;
        categories = new HashSet<Category>();
        properties = new HashSet<>();
        loc = new Location();
    }

    public Place(String googleId, String name, Location loc, HashSet<Category> categories) {
        super.setGoogleId(googleId);
        if (categories != null) {
            this.categories = categories;
        }
        else {
            this.categories = new HashSet<>();
        }
        if (loc != null) {
            this.loc = loc;
        }
        else {
            this.loc = new Location();
        }
        this.setName(name);
    }

    public Set<Category> getCategories() {
        return this.categories;
    }

    public void addCategory(Category ca) {
        this.categories.add(ca);
    }

    public static void saveAll(List<Place> places) throws FileNotFoundException, IOException, ParseException, SQLException{
    	PlaceDAO.SavePlacesAndPlaceCats(places);
    	
    }

    public static List<Place> getPlacesByLocation(SearchByLocation query) throws SQLException {
        return (PlaceDAO.getPlacesByLocation(query));

    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public void setProperties(Set<Property> properties) {
        this.properties = properties;
    }


    public static List<ResultMultipleSearch> gePlacesAggregation(SearchByMultipleLocation searchQueryJson) throws SQLException {
        return (PlaceDAO.getPlacesAggregation(searchQueryJson));
    }
}
