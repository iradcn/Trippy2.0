package model;

/**
 * Created by nimrod on 6/6/15.
 */
public class Location {
    private double lat;
    private double radius;
    private double lon;

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Location(double lat, double lon){
    	this.lat = lat;
    	this.lon = lon;
    }
    
    public Location(){
    }



}
