package server;

import model.Progress;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import DAO.CategoryDAO;
import services.JDBCConnection;
import services.yagoImportService;

import java.io.IOException;

@RestController
public class Controller {
	@RequestMapping("/service")
	public String greeting() {
		
		//run import on another thread
		yagoImportService yagoIS = new yagoImportService();
		new Thread(yagoIS).start();
		
		return null;
	}
	@RequestMapping("/status")
	public @ResponseBody JSONObject sendStatusJSON() throws IOException, ParseException {
		JSONObject obj = Progress.Load();
		return obj;
	}
}
