package go4lunch.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.osmdroid.util.GeoPoint;

import java.util.List;

import go4lunch.data.model.Restaurant;
import go4lunch.data.repository.OpenTripMapRepository;

public class RestaurantViewModel extends ViewModel {
    private final OpenTripMapRepository repository;
    private final MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private GeoPoint lastPosition;
    private double lastZoom = 18.0;
    private GeoPoint currentUserLocation;

    public RestaurantViewModel(OpenTripMapRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Restaurant>> getRestaurants() {
        return restaurantsLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public GeoPoint getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(GeoPoint lastPosition) {
        this.lastPosition = lastPosition;
    }

    public GeoPoint getCurrentUserLocation() {
        return currentUserLocation;
    }

    public void setCurrentUserLocation(GeoPoint currentUserLocation) {
        this.currentUserLocation = currentUserLocation;
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
                GeoPoint userLocation = new GeoPoint(lat, lon);
                for (Restaurant restaurant : restaurants) {
                    calculateDistance(restaurant, userLocation);
                }
                restaurantsLiveData.postValue(restaurants);
            }

            @Override
            public void onError(String message) {
                errorLiveData.postValue(message);
            }
        });
    }

    private void calculateDistance(Restaurant restaurant, GeoPoint userLoc) {
        if (restaurant.hasCoordinates()) {
            GeoPoint restLoc = new GeoPoint(restaurant.getLatitude(), restaurant.getLongitude());
            restaurant.distance = Math.round(userLoc.distanceToAsDouble(restLoc)) + "m";
        } else {
            restaurant.distance = "N/A";
        }
    }
}

