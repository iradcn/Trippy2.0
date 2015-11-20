package model;

public enum EnumCategory {
	AIRPORT ("airport"),
    AMUSEMENT_PARK("amusement_park"),
    AQUARIUM("aquarium"),
    ART_GALLERY("art_gallery"),
    BAKERY("bakery"),
    BAR("bar"),
    CAFE("cafe"),
    CASINO("casino"),
    CLOTHING_STORE("clothing_store"),
    CONVENIENCE_STORE("convenience_store"),
    DEPARTMENT_STORE("department_store"),
    FOOD("food"),
    GROCERY_OR_SUPERMARKET("grocery_or_supermarket"),
    GYM("gym"),
    HEALTH("health"),
    MOVIE_THEATER("movie_theater"),
    MUSEUM("museum"),
    NIGHT_CLUB("night_club"),
    PARK("park"),
    RESTAURANT("restaurant"),
    SHOPPING_MALL("shopping_mall"),
    SPA("spa"),
    ZOO("zoo");
	
    private final String name;       

    private EnumCategory(String s) {
        name  = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    public String toString() {
       return this.name;
    }
}





