package server;

import java.sql.SQLException;
import java.util.List;

import model.Place;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import protocol_model.QuestionAndResults;
import protocol_model.ResultMultipleSearch;
import protocol_model.SearchByLocation;
import protocol_model.SearchByLocationEditor;
import protocol_model.SearchByMultipleLocation;
import protocol_model.SearchByMultipleLocationEditor;
import business_layer.PlaceBusinessLayer;

@RestController
public class PlacesController {
	
	@Autowired
	PlaceBusinessLayer placeBusinessLayer;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(SearchByLocation.class, new SearchByLocationEditor());
		binder.registerCustomEditor(SearchByMultipleLocation.class, new SearchByMultipleLocationEditor());
	}

	@RequestMapping(value="app/get_all_places", method=RequestMethod.GET)
	public ResponseEntity<String> createProperty() {
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value="app/get_places_by_loc", method=RequestMethod.POST)
	public QuestionAndResults SearchPlacesByLocation(@RequestBody SearchByLocation searchQueryJson) throws SQLException {
		QuestionAndResults questionAndResults = placeBusinessLayer.getPlacesOrQuestion(searchQueryJson);
		return questionAndResults;
	}

	@RequestMapping(value="app/get_places_aggregation", method=RequestMethod.POST)
	public List<ResultMultipleSearch> SearchAggByMultipleLocation(@RequestBody SearchByMultipleLocation searchQueryJson) throws SQLException {
		return Place.gePlacesAggregation(searchQueryJson);
	}
}
