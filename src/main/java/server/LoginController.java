package server;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import security.LoginBusinessLayer;

@RestController
public class LoginController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	LoginBusinessLayer loginBusinessLayer;
	
	@RequestMapping(value="/login/facebook", method=RequestMethod.GET)
	public void loginWithFacebook(
			HttpServletRequest request,
			@RequestParam(value="accessToken", required=true) String accessToken,
			@RequestParam(value="signedRequest", required=true) String signedRequest,
			@RequestParam(value="userID", required=true) String userID) throws IOException, SQLException {
		
		
		loginBusinessLayer.loginWithFacebook(accessToken, userID, request, authenticationManager);
		
	}
	


}
