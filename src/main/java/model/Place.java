package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.parser.ParseException;

import DAO.PlaceDAO;

/**
 * Created by nimrod on 5/24/15.
 */
public class Place extends AbstractEntity {
    public Place(String yagoId) {
        super.setYagoId(yagoId);
        super.myType = typeOf.Place;
        categories = new HashSet<Category>();
    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public Set<Category> getCaegories() {
        return this.categories;
    }

    public void addCategory(Category ca) {
        this.categories.add(ca);
    }

    public static void saveAll(List<Place> places) throws FileNotFoundException, IOException, ParseException, SQLException{
    	PlaceDAO.SavePlacesAndPlaceCats(places);
    	
    }
    private double lat;
    private double lon;
    private Set<Category> categories;

}
