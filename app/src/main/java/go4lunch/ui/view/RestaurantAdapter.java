package go4lunch.ui.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Go4Lunch.R;

import java.util.List;

import go4lunch.data.model.Restaurant;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private List<Restaurant> data;

    public RestaurantAdapter(List<Restaurant> data) {
        this.data = data;
    }

    // Création de la vue
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list, parent, false);
        return new ViewHolder(view);
    }

    // Remplir la vue
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant item = data.get(position);
        holder.name.setText(item.displayName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
        }
    }
}