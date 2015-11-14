package model;

import java.util.HashMap;
import java.util.Map;

public class CategoryMap{
private static final Map<String, Integer> map;
static
{
    map = new HashMap<String, Integer>();
    map.put ("airport",1);
    map.put("amusement_park",2);
    map.put("aquarium",3);
    map.put("art_gallery",4);
    map.put("bakery",5);
    map.put("bar",6);
    map.put("cafe",7);
    map.put("casino",8);
    map.put("clothing_store",9);
    map.put("convenience_store",10);
    map.put("department_store",11);
    map.put("food",12);
    map.put("grocery_or_supermarket",13);
    map.put("gym",14);
    map.put("health",15);
    map.put("movie_theater",16);
    map.put("museum",17);
    map.put("night_club",18);
    map.put("park",19);
    map.put("restaurant",20);
    map.put("shopping_mall",21);
    map.put("spa",22);
    map.put("zoo",22);
}
public static Map<String, Integer> getMap() {
	return map;
}
}
