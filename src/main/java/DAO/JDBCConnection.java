package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import services.ConnectionConfig;

public class JDBCConnection {

	private static Lock lock = new ReentrantLock();
	private static List<Connection> connList = new LinkedList<Connection>(); // DB connection
	
	public static Connection getConnection() throws SQLException{
		Connection conn = null;
		try {
			if (lock.tryLock(10, TimeUnit.SECONDS)){
				if (connList.size()>0){
					conn = connList.remove(0);
					return conn;
				}
				conn = openConnection();
				if (conn == null) throw new SQLException();
			}
		}
		catch(InterruptedException e){
	           e.printStackTrace();
		}finally{
			lock.unlock();
		
		} 
		return conn;
	}
	
	private static Connection openConnection()  {
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

	public static void executeBatch(List<PreparedStatement> statements, Connection conn) throws SQLException{

		try {
			conn.setAutoCommit(false);

			for (PreparedStatement preparedStatement: statements){
				preparedStatement.executeBatch();
			}

			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			if (conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			throw e;
		} finally{
			JDBCConnection.closeConnection(conn);
			conn.setAutoCommit(true);
		}
	}

	public static void executeUpdate(List<PreparedStatement> statements, Connection conn) throws SQLException{

		try {
			conn.setAutoCommit(false);

			for (PreparedStatement preparedStatement: statements){
				preparedStatement.executeUpdate();
			}

			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			if (conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			throw e;
		} finally{
			JDBCConnection.closeConnection(conn);
			conn.setAutoCommit(true);
		}
	}

	public static ResultSet executeQuery(PreparedStatement statement, Connection conn) throws SQLException{

		ResultSet rs = null;
		try {
			rs = statement.executeQuery();
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally{
			JDBCConnection.closeConnection(conn);
		}
	}

	public static void executeStoredProcedures(CallableStatement sc, Connection conn) throws SQLException {

		try {
			sc.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally{
			JDBCConnection.closeConnection(conn);
		}
	}

	public static String preparePlaceHolders(int length) {
		StringBuilder builder = new StringBuilder(length * 2 - 1);
		for (int i = 0; i < length; i++) {
			if (i > 0) builder.append(',');
			builder.append('?');
		}
		return builder.toString();
	}

	public static void setValues(PreparedStatement preparedStatement, Object... values) throws SQLException {
		for (int i = 0; i < values.length; i++) {
			preparedStatement.setObject(i + 1, values[i]);
		}
	}
	
}
