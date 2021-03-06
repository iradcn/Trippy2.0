package server;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import services.GoogleImportService;

@RestController
public class GoogleImportController {
	
	@RequestMapping("app/import")
	public ResponseEntity<String> yagoImport() {
    		GoogleImportService googleImportService = new GoogleImportService();
		googleImportService.importData();
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
