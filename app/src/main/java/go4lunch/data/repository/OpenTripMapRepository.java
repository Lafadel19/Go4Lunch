package go4lunch.data.repository;

import androidx.lifecycle.viewmodel.CreationExtras;

import java.util.ArrayList;
import java.util.List;

import go4lunch.data.api.OpenTripMapApi;
import go4lunch.data.model.Restaurant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        Call<List<Restaurant>> call = api.getRestaurants("fr", lonMin, latMin, lonMax, latMax, "restaurants", "json");
        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                List<Restaurant> restaurants = response.body();
                callback.onResponse(call, Response.success(restaurants));

            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                callback.onFailure(call, t);

            }
        });



    }
}
