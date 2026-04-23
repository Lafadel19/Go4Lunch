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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.Go4Lunch.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_maps);
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            loadFragment(new MapFragment());
        }

        nav.setOnItemSelectedListener(item -> {

                    if (item.getItemId() == R.id.navigation_map) {
                        loadFragment(new MapFragment());
                        return true;
                    }

                    if (item.getItemId() == R.id.navigation_list) {
                        loadFragment(new ListFragment());
                        return true;
                    }

                    if (item.getItemId() == R.id.navigation_workmates) {
                        return true;
                    }

                    return false;
        });

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
    }

    private void loadFragment(Fragment fragment) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
    }


}





