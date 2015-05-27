package services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.json.simple.parser.ParseException;

public class JDBCConnection {

	private static Lock lock = new ReentrantLock();
	private static List<Connection> connList = new LinkedList<Connection>(); // DB connection
	
	public static Connection getConnection() throws FileNotFoundException, IOException, ParseException{
		Connection conn = null;
		try {
			if (lock.tryLock(10, TimeUnit.SECONDS)){
				if (connList.size()>0){
					conn = connList.remove(0);
					return conn;
				}
				conn = openConnection();
			}
		}
		catch(InterruptedException e){
	           e.printStackTrace();
		}finally{
			lock.unlock();
		
		} 
		return conn;
	}
	
	private static Connection openConnection() throws FileNotFoundException, IOException, ParseException {
		Connection conn;
		// loading the driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Unable to load the MySQL JDBC driver..");
			return null;
		}
		System.out.println("Driver loaded successfully");

		// creating the connection
		System.out.print("Trying to connect... ");
		
		try {
			
			ConnectionConfig.init();
			conn = DriverManager.getConnection(
					ConnectionConfig.getConnectionURL(), ConnectionConfig.getUserName(),ConnectionConfig.getPassword()
					);
			System.out.println("Connected!");
		} catch (SQLException e) {
			System.out.println("Unable to connect - " + e.getMessage());
			conn = null;
		}
		return conn;
	}
	
	public static void closeConnection(Connection conn) {
		// closing the connection
		try {
			if (lock.tryLock(10, TimeUnit.SECONDS)){
				if (conn != null){
					connList.add(conn);
				}
			}
		}
		catch(InterruptedException e){
	           e.printStackTrace();
		}finally{
			lock.unlock();
		
		} 
	}
	
}
