package se.walkercrou.places;

import java.util.ArrayList;
import java.util.List;

/**
 * Google Place API official "types"
 */
public class Types {
	
	private static List<String> types = new ArrayList<>();
	
    public String TYPE_AIRPORT = "airport";
    public String TYPE_AMUSEMENT_PARK = "amusement_park";
    public String TYPE_AQUARIUM = "aquarium";
    public String TYPE_ART_GALLERY = "art_gallery";
    public String TYPE_BAKERY = "bakery";
    public String TYPE_BAR = "bar";
    public String TYPE_CAFE = "cafe";
    public String TYPE_CASINO = "casino";
    public String TYPE_CLOTHING_STORE = "clothing_store";
    public String TYPE_CONVENIENCE_STORE = "convenience_store";
    public String TYPE_DEPARTMENT_STORE = "department_store";
    public String TYPE_FOOD = "food";
    public String TYPE_GROCERY_OR_SUPERMARKET = "grocery_or_supermarket";
    public String TYPE_GYM = "gym";
    public String TYPE_HEALTH = "health";
    public String TYPE_MOVIE_THEATER = "movie_theater";
    public String TYPE_MUSEUM = "museum";
    public String TYPE_NIGHT_CLUB = "night_club";
    public String TYPE_PARK = "park";
    public String TYPE_RESTAURANT = "restaurant";
    public String TYPE_SHOPPING_MALL = "shopping_mall";
    public String TYPE_SPA = "spa";
    public String TYPE_ZOO = "zoo";
    
    //public String TYPE_ACCOUNTING = "accounting";
    //public String TYPE_ATM = "atm";
    // public String TYPE_BANK = "bank";
    // public String TYPE_BEAUTY_SALON = "beauty_salon";
    // public String TYPE_BICYCLE_STORE = "bicycle_store";
    // public String TYPE_BOOK_STORE = "book_store";
    // public String TYPE_BOWLING_ALLEY = "bowling_alley";
    // public String TYPE_BUS_STATION = "bus_station";
    //public String TYPE_CAMPGROUND = "campground";
   // public String TYPE_CAR_DEALER = "car_dealer";
  //  public String TYPE_CAR_RENTAL = "car_rental";
   // public String TYPE_CAR_REPAIR = "car_repair";
  //  public String TYPE_CAR_WASH = "car_wash";
    // public String TYPE_CEMETERY = "cemetery";
    //public String TYPE_CHURCH = "church";
    //public String TYPE_CITY_HALL = "city_hall";
    // public String TYPE_COURTHOUSE = "courthouse";
    // public String TYPE_DENTIST = "dentist";
    // public String TYPE_DOCTOR = "doctor";
    //public String TYPE_ELECTRICIAN = "electrician";
   // public String TYPE_ELECTRONICS_STORE = "electronics_store";
  //  public String TYPE_EMBASSY = "embassy";
  //  public String TYPE_ESTABLISHMENT = "establishment";
   // public String TYPE_FINANCE = "finance";
  //  public String TYPE_FIRE_STATION = "fire_station";
  //  public String TYPE_FLORIST = "florist";
    //  public String TYPE_FUNERAL_HOME = "funeral_home";
    // public String TYPE_FURNITURE_STORE = "furniture_store";
   //  public String TYPE_GAS_STATION = "gas_station";
   //  public String TYPE_GENERAL_CONTRACTOR = "general_contractor";
    //   public String TYPE_HAIR_CARE = "hair_care";
    //  public String TYPE_HARDWARE_STORE = "hardware_store";
    // public String TYPE_HINDU_TEMPLE = "hindu_temple";
    //public String TYPE_HOME_GOODS_STORE = "home_goods_store";
   // public String TYPE_HOSPITAL = "hospital";
   // public String TYPE_INSURANCE_AGENCY = "insurance_agency";
   // public String TYPE_JEWELRY_STORE = "jewelry_store";
//    public String TYPE_LAUNDRY = "laundry";
//    public String TYPE_LAWYER = "lawyer";
//    public String TYPE_LIBRARY = "library";
//    public String TYPE_LIQUOR_STORE = "liquor_store";
//    public String TYPE_LOCAL_GOVERNMENT_OFFICE = "local_government_office";
//    public String TYPE_LOCKSMITH = "locksmith";
//    public String TYPE_LODGING = "lodging";
//    public String TYPE_MEAL_DELIVERY = "meal_delivery";
//    public String TYPE_MEAL_TAKEAWAY = "meal_takeaway";
//    public String TYPE_MOSQUE = "mosque";
//    public String TYPE_MOVIE_RENTAL = "movie_rental";
//  public String TYPE_MOVING_COMPANY = "moving_company";
    // public String TYPE_PAINTER = "painter";
    // public String TYPE_PARKING = "parking";
//  public String TYPE_PET_STORE = "pet_store";
//  public String TYPE_PHARMACY = "pharmacy";
//  public String TYPE_PHYSIOTHERAPIST = "physiotherapist";
//  public String TYPE_PLACE_OF_WORSHIP = "place_of_worship";
//  public String TYPE_PLUMBER = "plumber";
//  public String TYPE_POLICE = "police";
//  public String TYPE_POST_OFFICE = "post_office";
//  public String TYPE_REAL_ESTATE_AGENCY = "real_estate_agency";
//  public String TYPE_ROOFING_CONTRACTOR = "roofing_contractor";
//  public String TYPE_RV_PARK = "rv_park";
//  public String TYPE_SCHOOL = "school";
//  public String TYPE_SHOE_STORE = "shoe_store";
//    public String TYPE_STADIUM = "stadium";
//    public String TYPE_STORAGE = "storage";
//    public String TYPE_STORE = "store";
//    public String TYPE_SUBWAY_STATION = "subway_station";
//    public String TYPE_SYNAGOGUE = "synagogue";
//    public String TYPE_TAXI_STAND = "taxi_stand";
//    public String TYPE_TRAIN_STATION = "train_station";
//    public String TYPE_TRAVEL_AGENCY = "travel_agency";
//    public String TYPE_UNIVERSITY = "university";
//    public String TYPE_VETERINARY_CARE = "veterinary_care";

    
    public Types() {
    	types.add("airport");
    	types.add("amusement_park");
    	types.add("zoo");
    	types.add("spa");
    	types.add("shopping_mall");
    	types.add("restaurant");
    	types.add("park");
    	types.add("night_club");
    	types.add( "museum");
    	types.add("movie_theater");
    	types.add("health");
    	types.add("gym");
    	types.add("grocery_or_supermarket");
    	types.add("food");
    	types.add("department_store");
    	types.add("convenience_store");
    	types.add("casino");
    	types.add( "cafe");
    	types.add("bar");
    	types.add("bakery");
    	types.add("art_gallery");
    	types.add("aquarium");
    }

	public static List<String> getTypes() {
		return types;
	}

	public static void setTypes(List<String> types) {
		Types.types = types;
	}
}
