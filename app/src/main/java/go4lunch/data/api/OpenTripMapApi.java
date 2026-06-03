package go4lunch.data.api;

import java.util.List;

import go4lunch.data.model.Restaurant;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OpenTripMapApi {
    @GET("{lang}/places/bbox")
    Call<List<Restaurant>> getRestaurants(
            @Path("lang") String lang,
            @Query("lon_min") double lonMin,
            @Query("lat_min") double latMin,
            @Query("lon_max") double lonMax,
            @Query("lat_max") double latMax,
            @Query("kinds") String kinds,
            @Query("format") String format
    );
}