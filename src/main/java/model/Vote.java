package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.parser.ParseException;

public class Vote extends AbstractEntity {

	private String place;
	private String placeId;
	private String property;
	private String propertyId;

	public String getPlace() {
		return place;
	}

	public void setPlace (String place_name) {
		this.place = place_name;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId (String place_id) {
		this.placeId = place_id;
	}


	public String getProperty() {
		return property;
	}

	public void setProperty (String prop_name) {
		this.property = prop_name;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId (String property_id) {
		this.propertyId = property_id;
	}

}
