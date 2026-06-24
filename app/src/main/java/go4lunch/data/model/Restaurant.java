package go4lunch.data.model;

import java.util.List;

public class Restaurant {
    public String xid;
    public String name;
    public String kinds;
    public Double lon;
    public Double lat;
    public Point point;
    public Geometry geometry;
    public String rate;
    public String image;
    public Address address;

    public static final class Point {
        public double lon;
        public double lat;
    }

    public static final class Geometry {
        public List<Double> coordinates;
    }

    public boolean hasCoordinates() {
        return !Double.isNaN(getLatitude()) && !Double.isNaN(getLongitude());
    }

    public double getLatitude() {
        if (lat != null) {
            return lat;
        }
        if (point != null) {
            return point.lat;
        }
        if (geometry != null && geometry.coordinates != null && geometry.coordinates.size() >= 2) {
            return geometry.coordinates.get(1);
        }
        return Double.NaN;
    }

    public double getLongitude() {
        if (lon != null) {
            return lon;
        }
        if (point != null) {
            return point.lon;
        }
        if (geometry != null && geometry.coordinates != null && geometry.coordinates.size() >= 2) {
            return geometry.coordinates.get(0);
        }
        return Double.NaN;
    }

    public String getName() {
        return name;
    }

    public String displayName() {
        return name != null && !name.isEmpty() ? name : "Unknown";
    }

    public String getSchedule() { return "11h30-22h30";}


}
