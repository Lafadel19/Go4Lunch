package data.model;

import com.google.android.gms.common.Feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OpenTripResponse {

    public List<Feature> features;

    public List<Restaurant> toRestaurants() {
        List<Restaurant> list = new ArrayList<>();

        for (Feature f : features) {
            Restaurant r = new Restaurant();
            r.name = f.properties.name;
            r.latitude = f.geometry.coordinates[1];
            r.longitude = f.geometry.coordinates[0];
            list.add(r);
        }
        return list;
    }
    public static class Feature {
        public Properties properties;
        public Geometry geometry;
    }
    public static class Geometry {
        public double[] coordinates;
    }
    public static class Properties {
        public String name;
    }
}
