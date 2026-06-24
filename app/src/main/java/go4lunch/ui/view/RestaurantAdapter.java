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

        holder.txtName.setText(restaurant.getName());
        holder.txtAddress.setText(restaurant.address.getAddress());
        holder.txtSchedule.setText(restaurant.getSchedule());
       // holder.txtIntention.setText(String.valueOf(restaurant.getIntention()));
       // holder.txtDistance.setText(restaurant.getDistance());
        holder.rating.setRating(Float.parseFloat(restaurant.rate));
        holder.rating.setNumStars(Integer.parseInt(restaurant.rate));


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