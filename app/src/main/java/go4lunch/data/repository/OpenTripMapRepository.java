package go4lunch.data.repository;

import androidx.lifecycle.viewmodel.CreationExtras;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        // OpenTripMap bbox: lang is only en|ru (fr → 400). kinds use taxonomy codes, e.g. foods (not "restaurants").
        api.getRestaurants("en", lonMin, latMin, lonMax, latMax, "foods", "json")
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                        if (response.isSuccessful()) {
                            List<Restaurant> body = response.body();
                            callback.onSuccess(body != null ? body : Collections.emptyList());
                        } else {
                            String detail = httpErrorDetail(response);
                            callback.onError("Error: " + response.code() + detail);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                        String msg = t.getMessage() != null ? t.getMessage() : "Network error";
                        callback.onError(msg);
                    }
                });
    }

    private static String httpErrorDetail(Response<?> response) {
        if (response.errorBody() == null) {
            return "";
        }
        try {
            String body = response.errorBody().string();
            if (body == null || body.isEmpty()) {
                return "";
            }
            return " — " + (body.length() > 200 ? body.substring(0, 200) + "…" : body);
        } catch (IOException e) {
            return "";
        }
    }
}