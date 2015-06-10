package server;

import model.Place;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import protocol_model.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class PlacesController {
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(SearchByLocation.class, new SearchByLocationEditor());
		binder.registerCustomEditor(SearchByMultipleLocation.class, new SearchByMultipleLocationEditor());
	}

	@RequestMapping(value="/get_all_places", method=RequestMethod.GET)
	public ResponseEntity<String> createProperty() {//TODO:
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value="/get_places_by_loc", method=RequestMethod.POST)
	public List<Place> SearchPlacesByLocation(@RequestParam("searchQuery") SearchByLocation searchQueryJson) throws SQLException {
		return Place.getPlacesByLocation(searchQueryJson);
	}

	@RequestMapping(value="/get_places_aggregation", method=RequestMethod.POST)
	public ResultMultipleSearch SearchAggByMultipleLocation(@RequestParam("searchQuery") SearchByMultipleLocation searchQueryJson) throws SQLException {
		return null;
	}
}
