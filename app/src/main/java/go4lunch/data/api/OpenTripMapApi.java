package go4lunch.data.api;

import java.util.List;

import go4lunch.data.model.Restaurant;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OpenTripMapApi {
    @GET("{lang}/places/bbox?kinds=foods&format=json&limit=10")
    Call<List<Restaurant>> getRestaurants(
            @Path("lang") String lang,
            @Query("lon_min") double lonMin,
            @Query("lat_min") double latMin,
            @Query("lon_max") double lonMax,
            @Query("lat_max") double latMax,
            @Query("rate") String rate,
            @Query("image") String image
    );
    @GET("{lang}/places/xid/{xid}")
    Call<Restaurant> getRestaurants(
            @Path("xid") String xid

    );


}