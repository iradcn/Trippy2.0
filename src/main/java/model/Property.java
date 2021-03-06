package model;

import java.sql.SQLException;
import java.util.List;

import DAO.PropertyDAO;
import DAO.VoteDAO;

/**
 * Created by nimrod on 5/29/15.
 */
public class Property  extends AbstractEntity {
    public Property (int id) {
		super.setId(id);
		super.setGoogleId("");
    }
	public Property () {
	}
	public Property (int id, String name) {
		
		super.setId(id);
		super.setName(name);
		
	}
	public void Save() throws SQLException {

		if (this.getId()<=0){
			PropertyDAO.Insert(this);
		}
		else{
			PropertyDAO.Update(this);
		}
	}
	public boolean Delete() throws SQLException{
		
		if (this.getId() <= 0) {
			return false;
		} else {
			PropertyDAO.Delete(this);
			return true;
		}
	}
	public static List<Property> getAll() throws SQLException {
		return PropertyDAO.getAll();
	}

	public static void RemovePropFromPlace(String placeId,int propId) throws SQLException
	{
		Place p = new Place(placeId);
		p.getProperties().add(new Property(propId));
		PropertyDAO.RemovePropFromPlace(p);

	}

	public static void AddPropToPlace(String placeId,int propId, int vote, String username, long nId) throws SQLException {
		VoteDAO.insertNewQuestion(propId,placeId,vote, username, nId);
	}

}
