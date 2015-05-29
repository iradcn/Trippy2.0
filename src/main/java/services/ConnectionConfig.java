package services;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConnectionConfig{
	
	public static String getUserName() {
		return userName;
	}
	public static String getPassword() {
		return password;
	}
	private static String hostName;
	private static String port;
	private static String dbName;
	private static String protocol;
	private static String userName;
	private static String password;


	public static void init() throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader("Config/local.json"));
		JSONObject configJSON = (JSONObject) obj;
		hostName = (String) configJSON.get("hostName");
		port = (String) configJSON.get("port");
		dbName = (String) configJSON.get("dbName");
		protocol = (String) configJSON.get("protocol");
		password = (String) configJSON.get("password");
		userName = (String) configJSON.get("userName");
	}
	public static String getConnectionURL(){
		
		StringBuilder sb = new StringBuilder();
		sb.append(protocol).append("://").append(hostName).append(":").append(port).append("/").append(dbName);
		return sb.toString();
	}
	
	
}
