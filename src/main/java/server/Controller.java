package server;

import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import DAO.CategoryDAO;
import services.JDBCConnection;
import services.yagoImportService;

import java.io.IOException;

@RestController
public class Controller {
	@RequestMapping("/service")
	public String greeting() throws IOException, ParseException {
		
		try {
			yagoImportService.importData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
