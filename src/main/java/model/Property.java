package model;

import java.sql.SQLException;

import DAO.PropertyDAO;

/**
 * Created by nimrod on 5/29/15.
 */
public class Property  extends AbstractEntity {
    public Property() {
    }

	public void Save() throws SQLException {

		if (this.getId()>0){
			PropertyDAO.Insert(this);
		}
		else{
			PropertyDAO.Update(this);
		}
	}
	public boolean Delete() throws SQLException{
		
		if (this.getId() <= 0)
			return false;
		
		if (this.Delete())
			return true;
		return false;
		
	}
	

}
