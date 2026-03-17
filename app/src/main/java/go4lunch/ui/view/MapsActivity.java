package go4lunch.ui.view;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.Go4Lunch.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;

import go4lunch.data.api.OpenTripMapApi;
import go4lunch.data.model.Restaurant;
import go4lunch.data.remote.RetrofitClient;
import go4lunch.data.repository.OpenTripMapRepository;
import go4lunch.ui.viewmodel.RestaurantViewModel;

public class MapsActivity extends AppCompatActivity {
    private MapView map;
    private MyLocationNewOverlay myLocationOverlay;
    private ImageButton btnMyLocation;
    private IMapController mapController;
    private RestaurantViewModel viewModel;

    private static final int LOCATION_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_maps);

        map = findViewById(R.id.map);
        map.setMultiTouchControls(true);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.getController().setZoom(15.0);
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        myLocationOverlay.enableMyLocation();
        map.getOverlays().add(myLocationOverlay);
        myLocationOverlay.runOnFirstFix(() -> runOnUiThread(() -> {
            if (myLocationOverlay.getMyLocation() != null) {
                map.getController().animateTo(myLocationOverlay.getMyLocation());
                map.getController().setZoom(20.0);
            }
        }));
        btnMyLocation = findViewById(R.id.btnMyLocation);
        btnMyLocation.setOnClickListener(v -> {
            GeoPoint myPos = myLocationOverlay.getMyLocation();
            if (myPos != null) {
                map.getController().animateTo(myPos);
                map.getController().setZoom(20.0);
            } else {
                Toast.makeText(this, "Position currently unavailable...", Toast.LENGTH_SHORT).show();
            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        }
        OpenTripMapApi api = RetrofitClient.getInstance().create(OpenTripMapApi.class);

        OpenTripMapRepository repo = new OpenTripMapRepository(RetrofitClient.getOpenTripMapApi());
        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new RestaurantViewModel(repo);
            }
        }).get(RestaurantViewModel.class);

        viewModel.getRestaurants().observe(this, restaurants -> {
            if (restaurants != null) {
                addMarkers(restaurants);
            }
        });

        viewModel.getError().observe(this, error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show());

    }

    private void addMarkers(List<Restaurant> restaurants) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            map.getOverlays().removeIf(overlay -> overlay instanceof Marker);
        }

        for (Restaurant restaurant : restaurants) {
            Marker marker = new Marker(map);
            marker.setPosition(new GeoPoint(restaurant.lat, restaurant.lon));
            marker.setTitle(restaurant.name != null ? restaurant.name : "No name");
            marker.setIcon(getResources().getDrawable(R.drawable.outline_cooking_24));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setOnMarkerClickListener((marker1, mapView) -> {
                Toast.makeText(this, restaurant.name, Toast.LENGTH_SHORT).show();
                return true;
            });
            map.getOverlays().add(marker);
        }
        map.invalidate();
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            myLocationOverlay.enableMyLocation();
        }
    }

    @Override
    protected void onResume() {
     //   viewModel.loadRestaurants(myLocationOverlay.getMyLocation().getLatitude(), myLocationOverlay.getMyLocation().getLongitude(), 1000);
        super.onResume();
        map.onResume();
        myLocationOverlay.enableMyLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
        myLocationOverlay.disableMyLocation();
    }
}




