package security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import DAO.UserDAO;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;

public class LoginBusinessLayer {

	public void loginWithFacebook(String accessToken, String userID, HttpServletRequest request, AuthenticationManager authenticationManager) throws IOException, SQLException {
		
		Map jsonMap = getFacebookAuthObject(accessToken);
		boolean isAuthenticatedToFacebook = verifyAccessToken(jsonMap, userID);

		if (isAuthenticatedToFacebook){
			UUID newPassword = UUID.randomUUID();
			User user = new User(jsonMap.get("id").toString(),newPassword.toString(),accessToken,1);
			UserDAO.createOrUpdate(user);
			this.authenticateUser(user, request, authenticationManager);
		}
	}
	
	public void authenticateUser(User user, HttpServletRequest request, AuthenticationManager authenticationManager) {
	    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword());
	    token.setDetails(new WebAuthenticationDetails(request));
	    Authentication authentication = authenticationManager.authenticate(token);
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	public boolean verifyAccessToken(Map jsonMap, String userID) {
		if (jsonMap.get("id").equals(userID))
			return true;
		return false;
	}
	
	public Map getFacebookAuthObject(String accessToken) throws IOException {
		String url = "https://graph.facebook.com/me?access_token=%s";
		url = String.format(url, accessToken);
		URL obj;
		obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		JsonParserFactory factory=JsonParserFactory.getInstance();
		JSONParser parser=factory.newJsonParser();
		Map jsonMap=parser.parseJson(response.toString());
		return jsonMap;
	}
}
