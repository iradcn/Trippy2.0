package DAO;

import model.Place;

import java.util.List;

/**
 * Created by nimrod on 5/24/15.
 */
public class PlaceDAO {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/EMP";

    //  Database credentials
    static final String USER = "username";
    static final String PASS = "password";

    public void SavePlaces(List<Place> places) {

    }
}
