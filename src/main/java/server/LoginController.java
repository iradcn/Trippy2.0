package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import DAO.UserDAO;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;

@RestController
public class LoginController {
	@Autowired
	AuthenticationManager authenticationManager;
	
	@RequestMapping(value="/login/facebook", method=RequestMethod.GET)
	public void loginWithFacebook(
			HttpServletRequest request,
			@RequestParam(value="accessToken", required=true) String accessToken,
			@RequestParam(value="signedRequest", required=true) String signedRequest,
			@RequestParam(value="userID", required=true) String userID) throws IOException, SQLException {
		
		String url = "https://graph.facebook.com/me?access_token=%s";
		url = String.format(url, accessToken);
		URL obj = new URL(url);
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
		UUID idOne = UUID.randomUUID();
		if (jsonMap.get("id").equals(userID)){
			User user = new User(jsonMap.get("id").toString(),idOne.toString(),accessToken,1);
			try{
				UserDAO.Insert(user);
			}
			catch (Exception e) {
				UserDAO.Update(user);
			}
		    try {
		        // Must be called from request filtered by Spring Security, otherwise SecurityContextHolder is not updated
		        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword());
		        token.setDetails(new WebAuthenticationDetails(request));
		        Authentication authentication = this.authenticationManager.authenticate(token);
		        SecurityContextHolder.getContext().setAuthentication(authentication);
		    } catch (Exception e) {
		        SecurityContextHolder.getContext().setAuthentication(null);
		    }
		    	
		} else {
			//response 403
		}
	}
	
	public void login(HttpServletRequest request, String userName, String password)
	{

	    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userName, password);

	    // Authenticate the user
	    Authentication authentication = authenticationManager.authenticate(authRequest);
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    securityContext.setAuthentication(authentication);

	    // Create a new session and add the security context.
	    HttpSession session = request.getSession(true);
	    session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	}

}
