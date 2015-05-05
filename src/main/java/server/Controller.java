package server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
	@RequestMapping("/service")
	public String greeting(@RequestParam(value="name") String name) {
		
		if (name.equals("Shiri") || name.equals("Irad") || name.equals("Nimrod"))
			return "Hi "+name+"!"+"\n"+"Good to have you on the team!";
		return "Unknown name";
	}

}
