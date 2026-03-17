package go4lunch.data.repository;

import java.util.List;

import go4lunch.data.api.OpenTripMapApi;
import go4lunch.data.model.Restaurant;
import retrofit2.Callback;

public class OpenTripMapRepository {private final OpenTripMapApi api;
    private final String apiKey = "API_Key";

    public OpenTripMapRepository(OpenTripMapApi api) {
        this.api = api;
    }

    public void getRestaurants(double lat, double lon, double radiusMeters,
                               Callback<List<Restaurant>> callback) {
        // Calcul bbox 1 km
        double deltaLat = radiusMeters / 111000.0;           // ~1 km = 0.009°
        double deltaLon = deltaLat / Math.cos(Math.toRadians(lat));

        double latMin = lat - deltaLat;
        double latMax = lat + deltaLat;
        double lonMin = lon - deltaLon;
        double lonMax = lon + deltaLon;

        api.getRestaurantsAround("fr", lonMin, latMin, lonMax, latMax,
                        "restaurants", "json", apiKey)
                .enqueue(callback);
    }
}
