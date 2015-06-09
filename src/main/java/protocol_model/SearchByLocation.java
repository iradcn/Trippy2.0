package protocol_model;

import model.Category;
import model.Location;
import model.Property;

import java.util.List;

/**
 * Created by nimrod on 6/6/15.
 */
public class SearchByLocation {
    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    Location loc;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    private List<Category> categories;
    private List<Property> properties;
}
