package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;

import DAO.CategoryDAO;

/**
 * Created by nimrod on 5/24/15.
 */
public class Category extends AbstractEntity {

	public Category() {
		
	}

    public Category(String name) {
        this.setName(name);
        super.myType = typeOf.Category;
        this.setGoogleId("");
    }
	
    public Category(int id, String name) {
        super.myType = typeOf.Category;
        super.setId(id);
        super.setName(name);
        this.setGoogleId("");
    }
    public static Map<Integer,Category> loadAll() throws FileNotFoundException, IOException, ParseException, SQLException{
		CategoryDAO catDao = new CategoryDAO();
		Map<Integer,Category> categories = catDao.selectAll();
    	return categories;
    }
}
