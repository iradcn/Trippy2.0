package server;

import model.Place;
import model.Vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import business_layer.PlaceBusinessLayer;
import DAO.UserDAO;
import protocol_model.*;
import services.QuestionManagerService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	public ResponseEntity<String> createProperty() {//TODO:
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
