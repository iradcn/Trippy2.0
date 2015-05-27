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

    public Category(String yagoId, int id, String name) {
        super.setYagoId(yagoId);
        super.myType = typeOf.Category;
        super.setId(id);
    }
    public static Map<String,Category> loadAll() throws FileNotFoundException, IOException, ParseException, SQLException{
		CategoryDAO catDao = new CategoryDAO();
		Map<String,Category> categories = catDao.selectAll();
    	return categories;
    }
    public static Category isYagoIdInCatList(List<Category> categories,String yagoId){
    	for (Category cat : categories){
    		if (cat.getYagoId().equals(yagoId))
    			return cat;
    	}
    	return null;
    }
}
