package protocol_model;

import model.Category;
import model.Location;
import model.Place;
import model.Property;

/**
 * Created by nimrod on 6/6/15.
 */
public class ResultMultipleSearch {
    private Location loc;

    public int getCountPlaces() {
        return countPlaces;
    }

    public void setCountPlaces(int countPlaces) {
        this.countPlaces = countPlaces;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    private int countPlaces;
}
