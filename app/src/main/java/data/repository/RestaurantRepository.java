package data.repository;

import java.util.List;

import data.model.OpenTripResponse;
import data.model.Restaurant;
import network.OpenTripMapApi;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantRepository {

    private static final String BASE_URL = "https://api.opentripmap.com/";
    private static final String API_KEY = "YOUR_API_KEY";

    private OpenTripMapApi api;

    public RestaurantRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(OpenTripMapApi.class);
    }

    public void getNearby(double latitude, double longitude, Callback callback) {
        api.getRestaurants(latitude, longitude, 1000, "restaurant", API_KEY)
                .enqueue(new retrofit2.Callback<OpenTripResponse>() {

                    @Override
                    public void onResponse(Callback<OpenTripResponse> call,
                                           Response<OpenTripResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onResult(response.body().toRestaurants());

                        }
                    }
                    @Override
                    public void onFailure(Callback<OpenTripResponse> call, Throwable t) {
                        t.printStackTrace();

                    }
                });
    }

    public interface Callback {
        void onResult(List<Restaurant> restaurants);
    }
}
