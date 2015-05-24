package model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by nimrod on 5/24/15.
 */
public class Place extends AbstractGeoEntity {
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


    private double lat;
    private double lon;
    private Set<Category> categories;

}
