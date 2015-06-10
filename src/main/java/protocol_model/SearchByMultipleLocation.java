package protocol_model;

import model.Category;
import model.Location;
import model.Property;

import java.beans.PropertyEditor;
import java.util.List;

/**
 * Created by nimrod on 6/6/15.
 */
public class SearchByMultipleLocation  {
    private List<Location> locs;
    private Category cat;

    public List<Location> getLocs() {
        return locs;
    }

    public void setLocs(List<Location> locs) {
        this.locs = locs;
    }

    public Category getCat() {
        return cat;
    }

    public void setCat(Category cat) {
        this.cat = cat;
    }

}
