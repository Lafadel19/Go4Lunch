package go4lunch.data.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import go4lunch.data.api.OpenTripMapApi;
import go4lunch.data.model.Restaurant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpenTripMapRepository {
    private final OpenTripMapApi api;

    public OpenTripMapRepository(OpenTripMapApi api) {
        this.api = api;
    }

    public interface RestaurantsResultCallback {
        void onSuccess(List<Restaurant> restaurants);
        void onError(String message);
    }

    public void fetchRestaurants(double lat, double lon, double radiusMeters, RestaurantsResultCallback callback) {
        double deltaLat = radiusMeters / 111000.0;
        double deltaLon = deltaLat / Math.cos(Math.toRadians(lat));

        double latMin = lat - deltaLat;
        double latMax = lat + deltaLat;
        double lonMin = lon - deltaLon;
        double lonMax = lon + deltaLon;

        api.getRestaurants("en", lonMin, latMin, lonMax, latMax, 10)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Restaurant> restaurants = response.body();
                            if (restaurants.isEmpty()) {
                                callback.onSuccess(Collections.emptyList());
                                return;
                            }
                            fetchDetailsForRestaurants(restaurants, callback);
                        } else {
                            callback.onError("Error: " + response.code() + httpErrorDetail(response));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                        callback.onError(t.getMessage() != null ? t.getMessage() : "Network error");
                    }
                });
    }

    private void fetchDetailsForRestaurants(List<Restaurant> restaurants, RestaurantsResultCallback callback) {
        final List<Restaurant> detailedList = Collections.synchronizedList(new ArrayList<>());
        final int total = restaurants.size();
        final AtomicInteger count = new AtomicInteger(0);

        for (Restaurant r : restaurants) {
            api.getRestaurantDetails("en", r.xid).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        detailedList.add(response.body());
                    }
                    notifyIfComplete(count, total, detailedList, callback);
                }

                @Override
                public void onFailure(Call<Restaurant> call, Throwable t) {
                    notifyIfComplete(count, total, detailedList, callback);
                }
            });
        }
    }

    private void notifyIfComplete(AtomicInteger count, int total, List<Restaurant> detailed, RestaurantsResultCallback callback) {
        if (count.incrementAndGet() == total) {
            callback.onSuccess(detailed);
        }
    }

    private static String httpErrorDetail(Response<?> response) {
        if (response.errorBody() == null) return "";
        try {
            String body = response.errorBody().string();
            if (body == null || body.isEmpty()) return "";
            return " — " + (body.length() > 200 ? body.substring(0, 200) + "…" : body);
        } catch (IOException e) {
            return "";
        }
    }
}