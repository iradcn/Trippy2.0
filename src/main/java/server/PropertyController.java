package server;

import java.sql.SQLException;

import model.Property;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PropertyController {
	@RequestMapping(value="/create_property", method=RequestMethod.POST)
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
	@RequestMapping(value="/modify_property", method=RequestMethod.POST)
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
	
	@RequestMapping(value="/delete_property", method=RequestMethod.POST)
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
}
