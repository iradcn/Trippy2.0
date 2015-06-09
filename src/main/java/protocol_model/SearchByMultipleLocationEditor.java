package protocol_model;

import com.google.gson.Gson;

import java.beans.PropertyEditorSupport;

/**
 * Created by nimrod on 6/9/15.
 */
public class SearchByMultipleLocationEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        //String text = Base64.decode(text);
        //Object obj = JSONValue.parse(text);
        Gson gson = new Gson();
        SearchByMultipleLocationEditor sc = gson.fromJson(text, SearchByMultipleLocationEditor.class);
        setValue(sc);
    }
}
