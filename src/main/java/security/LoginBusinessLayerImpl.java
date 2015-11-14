package security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import services.FacebookService;
import DAO.UserDAO;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;

@Service
public class LoginBusinessLayerImpl implements LoginBusinessLayer {

	public boolean loginWithFacebook(String accessToken, String userID, HttpServletRequest request, AuthenticationManager authenticationManager) throws IOException, SQLException {
		
		FacebookService faceBookService = new FacebookService(accessToken, userID);
		faceBookService.init();
		boolean isAuthenticatedToFacebook = faceBookService.isLoggedToFB();

		if (isAuthenticatedToFacebook){
			User user = faceBookService.createUserByFB();
			UserDAO.createOrUpdate(user);
			faceBookService.saveUserCheckIns();

			this.authenticateUser(user, request, authenticationManager);
		}
		
		return isAuthenticatedToFacebook;
	}
	
	private void authenticateUser(User user, HttpServletRequest request, AuthenticationManager authenticationManager) {
	    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword());
	    token.setDetails(new WebAuthenticationDetails(request));
	    Authentication authentication = authenticationManager.authenticate(token);
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
