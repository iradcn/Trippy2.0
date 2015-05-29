package DAO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import model.Category;

import org.json.simple.parser.ParseException;

/**
 * Created by nimrod on 5/24/15.
 */
public class CategoryDAO {
	public Map<String, Category> selectAll() throws FileNotFoundException,
			IOException, ParseException, SQLException {
		Map<String, Category> categoryMap = new HashMap<String, Category>();
		Connection conn = JDBCConnection.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM categories");

		while (rs.next() == true) {
			String yagoId = rs.getNString("yagoid");
			int id = rs.getInt("id");
			String name = rs.getString("name");
			categoryMap.put(yagoId, new Category(yagoId, id, name));

		}
		JDBCConnection.closeConnection(conn);
		return categoryMap;
	}

}
/*	 
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

*/