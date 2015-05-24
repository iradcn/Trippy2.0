package server;

import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import services.JDBCConnection;
import services.yagoImportService;

import java.io.IOException;

@RestController
public class Controller {
	@RequestMapping("/service")
	public String greeting(@RequestParam(value="name") String name) throws IOException, ParseException {
		
		/*if (name.equals("Shiri") || name.equals("Irad") || name.equals("Nimrod"))
			return "Hi "+name+"!"+"\n"+"Good to have you on the team!";
		return "Unknown name";*/
		try {
			JDBCConnection.getConnection();
			//yagoImportService.importData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

}
