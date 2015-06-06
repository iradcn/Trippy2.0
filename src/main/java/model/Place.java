package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sun.java.swing.plaf.windows.TMSchema;
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

    public Set<Category> getCaegories() {
        return this.categories;
    }

    public void addCategory(Category ca) {
        this.categories.add(ca);
    }

    public static void saveAll(List<Place> places) throws FileNotFoundException, IOException, ParseException, SQLException{
    	PlaceDAO.SavePlacesAndPlaceCats(places);
    	
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

    private Location loc;
    private Set<Category> categories;
    private Set<Property> properties;

}
