package go4lunch.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

    public void loadRestaurants(double lat, double lon, double radiusMeters) {
        repository.getRestaurants(lat, lon, radiusMeters, new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if (response.isSuccessful()) {
                    restaurantsLiveData.setValue(response.body());
                } else {
                    errorLiveData.setValue("Error: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                errorLiveData.setValue(t.getMessage());
            }


        });

    }
}

