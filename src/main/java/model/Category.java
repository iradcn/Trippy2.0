package model;

import java.util.List;

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
    public static List<Category> loadAll(){
		CategoryDAO catDao = new CategoryDAO();
		List<Category> categories = catDao.selectAll();
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
