package security;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationManager;

public interface LoginBusinessLayer {
		public void loginWithFacebook(String accessToken, String userID, HttpServletRequest request, AuthenticationManager authenticationManager) throws IOException, SQLException;
	}
