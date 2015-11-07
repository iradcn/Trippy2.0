package protocol_model;

import java.beans.PropertyEditorSupport;

import com.google.gson.Gson;
//import com.sun.org.apache.xml.internal.security.utils.Base64;

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


