package protocol_model;

import model.Category;
import model.Location;
import model.Property;

import java.util.List;

/**
 * Created by nimrod on 6/6/15.
 */
public class SearchByLocation {
    public Location getLot() {
        return lot;
    }

    public void setLot(Location lot) {
        this.lot = lot;
    }

    Location lot;

    public int getRadius() {
        return Radius;
    }

    public void setRadius(int radius) {
        Radius = radius;
    }

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

    private double lat;
    private double Lon;
    private int Radius;
    private List<Category> categories;
    private List<Property> properties;
}
