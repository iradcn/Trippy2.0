package DAO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.ParseException;

import services.JDBCConnection;
import model.Category;

/**
 * Created by nimrod on 5/24/15.
 */
public class CategoryDAO {
	public List<Category> selectAll() {
		List<Category> categoryList = new ArrayList<Category>();
		try{
			Connection conn = JDBCConnection.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM categories");
			
			while (rs.next() == true){
				String yagoId = rs.getNString("yagoid");
				int id = rs.getInt("id");
				String name = rs.getString("name");
				categoryList.add(new Category(yagoId,id,name));
				
			}
		}catch(Exception e){
			System.out.println("ERROR executeQuery - " + e.getMessage());
		}

		return categoryList;
	}
	
	
	
}
/*



	public void demoExecuteQuery() {

		try (Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM country");) {

			while (rs.next() == true) {
				System.out.print(rs.getInt("COUNTRY_ID"));
				System.out.print("\t");
				System.out.print(rs.getString("COUNTRY"));
				System.out.print("\t");
				System.out.print(rs.getString(3));
				System.out.println();
			}
		} catch (SQLException e) {
			System.out.println("ERROR executeQuery - " + e.getMessage());
		}
	}
	
		public void demoExecuteUpdate() {
		int result;

		try (Statement stmt = conn.createStatement();) {
			// result = stmt
			// .executeUpdate("INSERT INTO demo(fname, lname) VALUES('Rubi','Boim')");
			// result = stmt
			// .executeUpdate("INSERT INTO demo(fname, lname) VALUES('Rubi','Boim2')");
			result = stmt.executeUpdate("DELETE FROM demo");
			System.out.println("Success - executeUpdate, result = " + result);

		} catch (SQLException e) {
			System.out.println("ERROR executeUpdate - " + e.getMessage());
		}
	}
		public void demoTransactions() {

		try (Statement stmt = conn.createStatement();) {
			conn.setAutoCommit(false);
			stmt.executeUpdate("INSERT INTO demo(fname, lname) VALUES('Transaction','Demo')");
			stmt.executeUpdate("INSERT INTO demo(fname, lname) VALUES('Transaction - should fail')");

			// committing
			System.out.println("Commiting transaction..");
			conn.commit();

			System.out
					.println("Error, transaction should fail and not reach this code");

		} catch (SQLException e) {
			System.out
					.println("We have an exception, transaction is not complete: Exception: "
							+ e.getMessage());
			try {
				conn.rollback();
				System.out.println("Rollback Successfully :)");
			} catch (SQLException e2) {
				System.out
						.println("ERROR demoTransactions (when rollbacking) - "
								+ e.getMessage());
			}
		} finally {
			safelySetAutoCommit();
		}
	}

	/**
	 * Attempts to set the connection back to auto-commit, ignoring errors.
	
	private void safelySetAutoCommit() {
		try {
			conn.setAutoCommit(true);
		} catch (Exception e) {
		}
	}

	/**
	 * Shows long insert
	 
	public void demoWithoutPreparedStatement() {
		long time = System.currentTimeMillis();
		try (Statement stmt = conn.createStatement();) {

			int i = 0;
			while (i < 20000) {
				stmt.executeUpdate("INSERT INTO demo(fname, lname) VALUES('Rubi-"
						+ i + "','Boim-" + i + "')");

				if (i % 2000 == 0)
					System.out.println("Statement - current record: " + i);

				i++;
			}

			System.out.println("Success - demoWithoutPreparedStatement");

			printTimeDiff(time);
		} catch (SQLException e) {
			System.out.println("ERROR demoWithoutPreparedStatement - "
					+ e.getMessage());
		}
	}

	/**
	 * Shows the PreparedStatement()
	 
	public void demoWithPreparedStatement() {
		long time = System.currentTimeMillis();
		try (PreparedStatement pstmt = conn
				.prepareStatement("INSERT INTO demo(fname,lname) VALUES(?,?)");) {

			int i = 0;
			while (i < 20000) {
				pstmt.setString(1, "Rubi-" + i);
				pstmt.setString(2, "Boim-" + i);
				pstmt.executeUpdate();

				if (i % 2000 == 0)
					System.out.println("PreparedStatement - current record: "
							+ i);

				i++;
			}

			System.out.println("Success - demoWithPreparedStatement");

			printTimeDiff(time);
		} catch (SQLException e) {
			System.out.println("ERROR demoWithPreparedStatement - "
					+ e.getMessage());
		}
	}

	/**
	 * Shows the Batch PreparedStatement()
	 
	public void demoBatchPreparedStatement() {
		long time = System.currentTimeMillis();
		try (PreparedStatement pstmt = conn
				.prepareStatement("INSERT INTO demo(fname,lname) VALUES(?,?)");) {

			int i = 0;
			while (i < 20000) {
				pstmt.setString(1, "Rubi-" + i);
				pstmt.setString(2, "Boim-" + i);
				pstmt.addBatch();

				if (i % 2000 == 0)
					System.out.println("Batch - current record: " + i);

				i++;
			}

			pstmt.executeBatch();

			System.out.println("Success - demoWithPreparedStatement");

			printTimeDiff(time);
		} catch (SQLException e) {
			System.out.println("ERROR demoWithPreparedStatement - "
					+ e.getMessage());
		}
	}

	/**
	 * Shows the Batch PreparedStatementTransaction()
	 
	public void demoBatchPreparedStatementTransaction() {
		long time = System.currentTimeMillis();
		try (PreparedStatement pstmt = conn
				.prepareStatement("INSERT INTO demo(fname,lname) VALUES(?,?)");) {

			conn.setAutoCommit(false);

			int i = 0;
			while (i < 20000) {
				pstmt.setString(1, "Rubi-" + i);
				pstmt.setString(2, "Boim-" + i);
				pstmt.addBatch();

				if (i % 2000 == 0)
					System.out.println("Batch - current record: " + i);

				i++;
			}

			pstmt.executeBatch();
			conn.commit();

			System.out
					.println("Success - demoWithPreparedStatement + transaction");

			printTimeDiff(time);
		} catch (SQLException e) {
			System.out.println("ERROR demoWithPreparedStatement - "
					+ e.getMessage());
		} finally {
			safelySetAutoCommit();
		}
	}

	/**
	 * Shows how to retrieve the generated ID (by "auto incremental")
	 
	public void demoGetGeneratedID() {
		Statement stmt = null;
		ResultSet rs = null;
		int id;

		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(
					"INSERT INTO demo(fname, lname) VALUES('Rubi','Boim')",
					new String[] { "ID" });
			rs = stmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);

			System.out
					.println("Success - GetGeneratedID, the generated ID is: "
							+ id);

		} catch (SQLException e) {
			System.out.println("ERROR executeUpdate - " + e.getMessage());
		} finally {
			safelyClose(rs, stmt);
		}
	}

	/**
	 * Attempts to close all the given resources, ignoring errors
	 * 
	 * @param resources
	 
	private void safelyClose(AutoCloseable... resources) {
		for (AutoCloseable resource : resources) {
			try {
				resource.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Prints the time difference from now to the input time.
	 
	private void printTimeDiff(long time) {
		time = (System.currentTimeMillis() - time) / 1000;
		System.out.println("Took " + time + " seconds");
	}



*/