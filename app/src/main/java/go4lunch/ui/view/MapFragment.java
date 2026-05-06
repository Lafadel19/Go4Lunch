package go4lunch.ui.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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

import go4lunch.data.model.Restaurant;
import go4lunch.data.remote.RetrofitClient;
import go4lunch.data.repository.OpenTripMapRepository;
import go4lunch.ui.viewmodel.RestaurantViewModel;

public class MapFragment extends Fragment {
    private MapView map;
    private MyLocationNewOverlay myLocationOverlay;
    private ImageButton btnMyLocation;
    private RestaurantViewModel viewModel;

    private static final int LOCATION_PERMISSION_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Configuration.getInstance().load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()));

        View view = inflater.inflate(R.layout.fragment_map,container,false);

        OpenTripMapRepository repo = new OpenTripMapRepository(RetrofitClient.getOpenTripMapApi());
        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new RestaurantViewModel(repo);
            }
        }).get(RestaurantViewModel.class);

        viewModel.getRestaurants().observe(getViewLifecycleOwner(), restaurants -> {
            if (restaurants != null) {
                addMarkers(restaurants);
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show());

        map = view.findViewById(R.id.map);
        map.setMultiTouchControls(true);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.getController().setZoom(15.0);


        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(requireContext()), map);
        myLocationOverlay.enableMyLocation();
        map.getOverlays().add(myLocationOverlay);
        myLocationOverlay.runOnFirstFix(() -> requireActivity().runOnUiThread(() -> {
            GeoPoint myPos = myLocationOverlay.getMyLocation();
            if (myPos != null) {
                map.getController().animateTo(myPos);
                map.getController().setZoom(20.0);
                viewModel.loadRestaurants(myPos.getLatitude(), myPos.getLongitude(), 1000);
            }
        }));

        btnMyLocation = view.findViewById(R.id.btnMyLocation);
        btnMyLocation.setOnClickListener(v -> {
            GeoPoint myPos = myLocationOverlay.getMyLocation();
            if (myPos != null) {
                map.getController().animateTo(myPos);
                map.getController().setZoom(20.0);
            } else {
                Toast.makeText(requireContext(), "Position currently unavailable...", Toast.LENGTH_SHORT).show();
            }
        });
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        }
        return view;

    }

    private void addMarkers(List<Restaurant> restaurants) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            map.getOverlays().removeIf(overlay -> overlay instanceof Marker);
        }

        for (Restaurant restaurant : restaurants) {
            if (!restaurant.hasCoordinates()) {
                continue;
            }
            Marker marker = new Marker(map);
            marker.setPosition(new GeoPoint(restaurant.getLatitude(), restaurant.getLongitude()));
            marker.setTitle(restaurant.displayName());
            marker.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.outline_cooking_24));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setOnMarkerClickListener((marker1, mapView) -> {
                Toast.makeText(requireContext(), restaurant.displayName(), Toast.LENGTH_SHORT).show();
                return true;
            });
            map.getOverlays().add(marker);
        }
        map.invalidate();
    }






    @Override
    public void ActivityResultLauncher(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.ActivityResultLauncher(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            myLocationOverlay.enableMyLocation();
            GeoPoint myPos = myLocationOverlay.getMyLocation();
            if (myPos != null && viewModel != null) {
                viewModel.loadRestaurants(myPos.getLatitude(), myPos.getLongitude(), 1000);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
        myLocationOverlay.enableMyLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
        myLocationOverlay.disableMyLocation();
    }


}