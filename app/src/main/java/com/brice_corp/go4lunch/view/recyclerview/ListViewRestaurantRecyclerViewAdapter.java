package com.brice_corp.go4lunch.view.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.model.projo.Restaurant;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by <NIATEL Brice> on <11/05/2020>.
 */
public class ListViewRestaurantRecyclerViewAdapter extends RecyclerView.Adapter<ListViewRestaurantRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "ListViewRVAdapter";
    private ArrayList<Restaurant> itemRestaurants;
    private Context mContext;

    public ListViewRestaurantRecyclerViewAdapter(Context context) {
        itemRestaurants = new ArrayList<>();
        mContext = context;
    }

    @NonNull
    @Override
    public ListViewRestaurantRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recyclerview_listview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewRestaurantRecyclerViewAdapter.ViewHolder holder, int position) {
        //Name
        StringBuilder sb = new StringBuilder(itemRestaurants.get(position).getName());
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        holder.nameRestaurant.setText(sb.toString());

        //Address
        holder.addressRestaurant.setText(itemRestaurants.get(position).getAdrAddress());

        //PHOTO
        if (itemRestaurants.get(position).getPhotos() != null) {
            Glide.with(mContext)
                    .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=200&photoreference="
                            + itemRestaurants.get(position).getPhotos().get(0).getPhotoReference() + "&key="
                            + "AIzaSyAz_L90GbDp0Hzy_GHjnmxsqPjc1sARRYA")
                    .centerCrop()
                    .into(holder.imageRestaurant);
        } else {
            Glide.with(mContext)
                    .load(R.drawable.no_image_restaurant)
                    .centerCrop()
                    .into(holder.imageRestaurant);
        }

        //TODO Set HOUR
        holder.scheduleRestaurant.setText("A FAIRE HEURE");
    }

    public void addItems(Restaurant restaurant) {
        Log.i(TAG, "addItems: " + restaurant);
        itemRestaurants.add(restaurant);
        notifyItemInserted(getItemCount() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (itemRestaurants != null) {
            if (itemRestaurants.size() == 0) {
                return 0;
            } else {
                return itemRestaurants.size();
            }
        } else {
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameRestaurant;
        final TextView addressRestaurant;
        final TextView scheduleRestaurant;
        final CircleImageView imageRestaurant;
        final View itemList;

        ViewHolder(View view) {
            super(view);
            nameRestaurant = view.findViewById(R.id.nameRestaurant);
            addressRestaurant = view.findViewById(R.id.addressRestaurant);
            scheduleRestaurant = view.findViewById(R.id.scheduleRestaurant);
            imageRestaurant = view.findViewById(R.id.imageRestaurant);
            itemList = view.findViewById(R.id.item_list_listview);
        }
    }
}
