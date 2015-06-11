package protocol_model;

//import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Location;

import java.beans.PropertyEditorSupport;

/**
 * Created by nimrod on 6/9/15.
 */
public class SearchByMultipleLocationEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        //String text = Base64.decode(text);
        //Object obj = JSONValue.parse(text);
        SearchByMultipleLocation sm = new SearchByMultipleLocation();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(text);
        if (element.isJsonObject()) {
            JsonObject query = element.getAsJsonObject();
            sm.setCategory(query.get("category").getAsString());
            if (query.get("property") != null)
                sm.setProperty(query.get("property").getAsInt());
            else
                sm.setProperty(0);
            JsonArray datasets = query.getAsJsonArray("locs");
            for (int i = 0; i < datasets.size(); i++) {
                Location loc = new Location();
                JsonObject dataset = datasets.get(i).getAsJsonObject();
                JsonObject locObj = dataset.get("loc").getAsJsonObject();
                loc.setLat(locObj.get("lat").getAsDouble());
                loc.setLon(locObj.get("lon").getAsDouble());
                loc.setRadius(locObj.get("radius").getAsInt());
                sm.getLocs().add(loc);
            }
        }

//        SearchByMultipleLocation sc = gson.fromJson(text, SearchByMultipleLocation.class);
        setValue(sm);
    }
}
