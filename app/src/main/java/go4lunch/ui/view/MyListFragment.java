package go4lunch.ui.view;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lucas.Go4Lunch.R;

import org.osmdroid.util.GeoPoint;

import go4lunch.data.remote.RetrofitClient;
import go4lunch.data.repository.OpenTripMapRepository;
import go4lunch.ui.viewmodel.RestaurantViewModel;

public class MyListFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_list_my, container, false);
        RecyclerView recycler = view.findViewById(R.id.recycler_view);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        OpenTripMapRepository repo = new OpenTripMapRepository(RetrofitClient.getOpenTripMapApi());

        RestaurantViewModel vm = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new RestaurantViewModel(repo);
            }
        }).get(RestaurantViewModel.class);

        vm.getRestaurants().observe(getViewLifecycleOwner(), data -> {
            recycler.setAdapter(new RestaurantAdapter(data));
        });
        vm.loadRestaurants(
                 48.85852716675161 , 2.346457830056134,
                1000
        );
        return view ;
    }

}