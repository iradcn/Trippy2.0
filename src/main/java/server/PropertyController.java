package server;

import java.sql.SQLException;
import java.util.List;

import model.Property;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PropertyController {
	@RequestMapping(value="app/create_property", method=RequestMethod.GET)
	public ResponseEntity<String> createProperty(@RequestParam("name") String name) {
		Property property = new Property();
		property.setName(name);
		try{
			property.Save();
			return new ResponseEntity<String>(HttpStatus.OK);
		}catch(SQLException e){
			System.out.println(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value="app/modify_property", method=RequestMethod.GET)
	public ResponseEntity<String> modifyProperty(@RequestParam("name") String name,@RequestParam("id") int id) {
		Property property = new Property();
		property.setName(name);
		property.setId(id);

		try{
			property.Save();
			return new ResponseEntity<String>(HttpStatus.OK);
		}catch(SQLException e){
			System.out.println(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="app/delete_property", method=RequestMethod.GET)
	public ResponseEntity<String> deleteProperty(@RequestParam("name") String name,@RequestParam("id") int id) {
		Property property = new Property();
		property.setName(name);
		property.setId(id);
		try{
			property.Delete();
			return new ResponseEntity<String>(HttpStatus.OK);
		}catch(SQLException e){
			System.out.println(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value="app/get_all_properties", method=RequestMethod.GET)
	public List<Property> getAllProperties() throws SQLException {
			Property.getAll();
			return Property.getAll();
	}

	@RequestMapping(value="app/AddPropToPlace", method=RequestMethod.GET)
	public void AddPropertyToPlace(@RequestParam("propId") int propId,@RequestParam("placeId") String placeId) {
		try {
			Property.AddPropToPlace(placeId, propId);
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@RequestMapping(value="app/DelPropFromPlace", method=RequestMethod.GET)
	public void RemovePropertyFromPlace(@RequestParam("propId") int propId,@RequestParam("placeId") String placeId) {
		try {
			Property.RemovePropFromPlace(placeId, propId);
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
