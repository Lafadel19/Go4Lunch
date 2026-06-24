package go4lunch.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.osmdroid.util.GeoPoint;

import java.util.List;

import go4lunch.data.model.Restaurant;
import go4lunch.data.repository.OpenTripMapRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantViewModel extends ViewModel {
    private final OpenTripMapRepository repository;
    private final MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public RestaurantViewModel(OpenTripMapRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Restaurant>> getRestaurants() {
        return restaurantsLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    private GeoPoint lastPosition;
    private double lastZoom = 18.0;

    public GeoPoint getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(GeoPoint lastPosition) {
        this.lastPosition = lastPosition;
    }

    public double getLastZoom() {
        return lastZoom;
    }

    public void setLastZoom(double lastZoom) {
        this.lastZoom = lastZoom;
    }



    public void loadRestaurants(double lat, double lon, double radiusMeters) {
        repository.fetchRestaurants(lat, lon, radiusMeters, new OpenTripMapRepository.RestaurantsResultCallback() {
            @Override
            public void onSuccess(List<Restaurant> restaurants) {
                for (Restaurant restaurant : restaurants) {


                }
                restaurantsLiveData.postValue(restaurants);
            }

            @Override
            public void onError(String message) {
                errorLiveData.postValue(message);
            }
        });
    }
}

