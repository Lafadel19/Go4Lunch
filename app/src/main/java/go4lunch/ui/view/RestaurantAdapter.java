package go4lunch.ui.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lucas.Go4Lunch.R;

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
                .inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    // Remplir la vue
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = data.get(position);

        holder.txtName.setText(restaurant.displayName());
        if (restaurant.address != null) {
            holder.txtAddress.setText(restaurant.address.getAddress());
        } else {
            holder.txtAddress.setText("Unknown Address");
        }
        holder.txtSchedule.setText(restaurant.getSchedule());
        holder.txtDistance.setText(restaurant.distance);
        holder.txtIntention.setText("0"); // Placeholder

        String rate = restaurant.getRate();
        try {
            holder.rating.setRating(Float.parseFloat(rate));
        } catch (NumberFormatException e) {
            holder.rating.setRating(0);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtAddress;
        TextView txtSchedule;
        TextView txtIntention;
        TextView txtDistance;
        RatingBar rating;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtSchedule = itemView.findViewById(R.id.txtSchedule);
            txtIntention = itemView.findViewById(R.id.txtIntention);
            txtDistance = itemView.findViewById(R.id.distance);
            rating = itemView.findViewById(R.id.rating);
            image = itemView.findViewById(R.id.image);
        }
    }
}