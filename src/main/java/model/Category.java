package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.json.simple.parser.ParseException;

import DAO.CategoryDAO;

/**
 * Created by nimrod on 5/24/15.
 */
public class Category extends AbstractEntity {
	public String getRepresentationName() {
		return representationName;
	}

	public void setRepresentationName(String representationName) {
		this.representationName = representationName;
	}
	private String representationName;
	
	public Category() {
		
	}

    public Category(String name, String representationName) {
        this.setName(name);
        super.myType = typeOf.Category;
        this.setGoogleId("");
        this.setRepresentationName(representationName);
    }
	
    public Category(int id, String name) {
        super.myType = typeOf.Category;
        super.setId(id);
        super.setName(name);
        this.setGoogleId("");
    }
    
    public Category(int id, String name, String representationName) {
        super.myType = typeOf.Category;
        super.setId(id);
        super.setName(name);
        this.setGoogleId("");
        this.setRepresentationName(representationName);
    }
    public static Map<Integer,Category> loadAll() throws FileNotFoundException, IOException, ParseException, SQLException{
		CategoryDAO catDao = new CategoryDAO();
		Map<Integer,Category> categories = catDao.selectAll();
    	return categories;
    }
}
