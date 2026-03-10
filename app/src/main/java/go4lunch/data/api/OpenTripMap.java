package go4lunch.data.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import go4lunch.data.model.Restaurant;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OpenTripMap {

    public interface RestaurantCallback {
        void OnRestaurantLoaded(List<Restaurant> restaurants);
    }

    public static void getRestaurants(double lat,double lon, RestaurantCallback callback) {

        new Thread(() -> {
            List<Restaurant> restaurants = new ArrayList<>();

            try {

                String url =
                        OpenTripMapConfig.BASE_URL +
                                "?radius=" + OpenTripMapConfig.RADIUS +
                                "?lat=" + lat +
                                "&lon=" + lon +
                                "&kinds=restaurants" +
                                "&apikey=" + OpenTripMapConfig.API_KEY;

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();

                String json = response.body().string();

                JSONObject obj = new JSONObject(json);
                JSONArray features = obj.getJSONArray("features");

                for (int i = 0; i < features.length(); i++) {
                    JSONObject feature = features.getJSONObject(i);
                    JSONObject properties = feature.getJSONObject("properties");
                    JSONObject geometry = feature.getJSONObject("geometry");

                    String name = properties.optString("name", "Restaurant");

                    JSONArray coords = geometry.getJSONArray("coordinates");

                    double placeLon = coords.getDouble(0);
                    double placeLat = coords.getDouble(1);

                    restaurants.add(new Restaurant(name, placeLat, placeLon));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            callback.OnRestaurantLoaded(restaurants);
        }).start();
    }
}
