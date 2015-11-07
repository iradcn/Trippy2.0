package protocol_model;

import java.util.ArrayList;
import java.util.List;

import model.Location;

/**
 * Created by nimrod on 6/6/15.
 */
public class SearchByMultipleLocation  {
    private List<Location> locs;
    private String category;

    public int getProperty() {
        return property;
    }

    public void setProperty(int property) {
        this.property = property;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Location> getLocs() {
        return locs;
    }

    public void setLocs(List<Location> locs) {
        this.locs = locs;
    }

    private int property;

    public SearchByMultipleLocation() {
        locs = new ArrayList<>();
    }

}
