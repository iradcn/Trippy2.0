package model;

/**
 * Created by nimrod on 6/6/15.
 */
public class Location {
    private double lat;
    private int radius;
    private double lon;

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
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

    public Location(Double lat, Double lon){
    	this.lat = lat;
    	this.lon = lon;
    }
    
    public Location(){
    }



}
