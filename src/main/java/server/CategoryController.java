package server;

import java.util.List;

import model.Category;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import DAO.CategoryDAO;

@RestController
public class CategoryController {
	@RequestMapping(value="app/get_all_categories", method=RequestMethod.GET)
	public List<Category> getAllCats() throws Exception {//TODO:
		
		List<Category> allCats = CategoryDAO.getAllCategories();
		return allCats;
	}
}
