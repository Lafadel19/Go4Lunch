package network;

import data.model.OpenTripResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenTripMapApi {

    @GET("0.1/en/places/radius")
    Call<OpenTripResponse> getRestaurants(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("radius") int radius,
            @Query("kinds") String kinds,
            @Query("apikey") String apiKey
    );
}
