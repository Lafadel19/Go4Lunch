package go4lunch.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Go4Lunch.R;

import java.util.ArrayList;

import go4lunch.ui.viewmodel.RestaurantViewModel;

public class ListFragment extends Fragment {

    public ListFragment() {
        super(R.layout.fragment_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        RecyclerView recycler = view.findViewById(R.id.recycler_view);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        RestaurantViewModel vm = new ViewModelProvider(requireActivity()).get(RestaurantViewModel.class);

        vm.getRestaurants().observe(getViewLifecycleOwner(), data -> {
            recycler.setAdapter(new RestaurantAdapter(data));
        });
    }
}