package server;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import services.Progress;
import services.YagoImportService;

@RestController
public class Controller {
	@RequestMapping("/service")
	public ResponseEntity<String> yagoImport() {
    	boolean status = Progress.startTrackingLoading();
    	if (status == true){
    		YagoImportService yagoIS = new YagoImportService();
    		new Thread(yagoIS).start();
			return new ResponseEntity<String>(HttpStatus.OK);
    	}
		return new ResponseEntity<String>(HttpStatus.PRECONDITION_FAILED);
	}
	@RequestMapping("/status")
	public Progress sendStatusJSON() throws IOException, ParseException {
		Progress progrs = Progress.getStatus();
		return progrs;
	}
}
