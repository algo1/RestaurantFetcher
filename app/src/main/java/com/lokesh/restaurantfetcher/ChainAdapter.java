package com.lokesh.restaurantfetcher;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import static com.lokesh.restaurantfetcher.Utils.getETADelivery;

/**
 * Created by lokeshponnada on 10/19/16.
 */

public class ChainAdapter extends RecyclerView.Adapter<ChainAdapter.ViewHolder> {

    private ArrayList<RestaurantsResponse.Chain> restroDataset;


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView area, restroRating, etaDelivery;

        public ViewHolder(View v) {
            super(v);
            area = (TextView) v.findViewById(R.id.area);
            restroRating = (TextView) v.findViewById(R.id.restroRating);
            etaDelivery = (TextView) v.findViewById(R.id.etaDelivery);

        }

    }


    public ChainAdapter(ArrayList<RestaurantsResponse.Chain> restroDataset, Activity context) {
        this.restroDataset = restroDataset;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chain_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        RestaurantsResponse.Chain restaurant = restroDataset.get(position);
        holder.area.setText(restaurant.getArea());
        holder.restroRating.setText(restaurant.getAvgRating() + Html.fromHtml(" &#9734"));
        if (!restaurant.isClosed()) {
            holder.etaDelivery.setText(getETADelivery(restaurant.getDeliveryTime()));
        } else {
            // Keep the default val for closed
            holder.etaDelivery.setText("N/A");
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return restroDataset.size();
    }


}