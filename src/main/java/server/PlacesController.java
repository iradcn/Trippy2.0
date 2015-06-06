package server;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlacesController {
	@RequestMapping(value="/get_all_places", method=RequestMethod.GET)
	public ResponseEntity<String> createProperty() {//TODO:
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
