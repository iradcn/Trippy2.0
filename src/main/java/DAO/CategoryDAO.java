package DAO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

		ResultSet rs = getAllCatsHelper();

		while (rs.next() == true) {
			String yagoId = rs.getNString("yagoid");
			int id = rs.getInt("id");
			String name = rs.getString("name");
			categoryMap.put(yagoId, new Category(yagoId, id, name));

		}

		return categoryMap;
	}
	public static List<Category> getAllCategories() throws SQLException{

		List<Category> allCats = new ArrayList<Category>();
		ResultSet rs = getAllCatsHelper();
		
		while (rs.next() == true) {
			String yagoId = rs.getNString("yagoid");
			int id = rs.getInt("id");
			String name = rs.getString("name");
			allCats.add(new Category(yagoId, id, name));

		}
		
		return allCats;
	
	}
	public static ResultSet getAllCatsHelper() throws SQLException{
		Connection conn = JDBCConnection.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM categories");
		JDBCConnection.closeConnection(conn);
		return rs;

	}

}