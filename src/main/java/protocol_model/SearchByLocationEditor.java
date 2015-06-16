package protocol_model;

import com.google.gson.Gson;
//import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.beans.PropertyEditorSupport;

/**
 * Created by nimrod on 6/6/15.
 */
public class SearchByLocationEditor extends PropertyEditorSupport {

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            //String text = Base64.decode(text);
            //Object obj = JSONValue.parse(text);
            Gson gson = new Gson();
            SearchByLocation sc = gson.fromJson(text, SearchByLocation.class);
            setValue(sc);
        }
    }


