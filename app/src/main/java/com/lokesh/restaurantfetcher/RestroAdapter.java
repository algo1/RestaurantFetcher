package com.lokesh.restaurantfetcher;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.lokesh.restaurantfetcher.Utils.getBudgetString;
import static com.lokesh.restaurantfetcher.Utils.getETADelivery;
import static com.lokesh.restaurantfetcher.Utils.getSpannableString;

/**
 * Created by lokeshponnada on 10/18/16.
 */

public class RestroAdapter extends RecyclerView.Adapter<RestroAdapter.ViewHolder> {


    private List<RestaurantsResponse.Restaurant> restroDataset;
    private Activity context;


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView restroName, cuisine, restroBudget, restroRating, etaDelivery, nextOpeningMsg, outletInfo;
        private ImageView restroImg;
        private int dataPos;
        private ArrayList<RestaurantsResponse.Chain> restroChain;

        public ViewHolder(View v) {
            super(v);
            restroImg = (ImageView) v.findViewById(R.id.restroImg);
            restroName = (TextView) v.findViewById(R.id.restroName);
            cuisine = (TextView) v.findViewById(R.id.cuisine);
            restroBudget = (TextView) v.findViewById(R.id.restroBudget);
            restroRating = (TextView) v.findViewById(R.id.restroRating);
            etaDelivery = (TextView) v.findViewById(R.id.etaDelivery);
            nextOpeningMsg = (TextView) v.findViewById(R.id.nextOpeningMsg);
            outletInfo = (TextView) v.findViewById(R.id.outletInfo);

            outletInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (restroChain == null || restroChain.size() == 0) {
                        return;
                    } else {
                        DialogFragment d = ChainDetailsDialog.newInstance(restroChain, restroDataset.get(dataPos).getName());
                        d.show(((MainActivity) context).getSupportFragmentManager(), "");
                    }
                }
            });
        }
    }


    public RestroAdapter(List<RestaurantsResponse.Restaurant> restroDataset, Activity context) {
        this.restroDataset = restroDataset;
        this.context = context;
    }


    @Override
    public RestroAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        RestaurantsResponse.Restaurant restaurant = restroDataset.get(position);
        // Img url missing in api
        holder.restroImg.setImageResource(R.drawable.swiggy);
        holder.restroName.setText(restaurant.getName());
        holder.cuisine.setText(android.text.TextUtils.join(",", restaurant.getCuisine()));
        holder.restroBudget.setText(getBudgetString(restaurant.getCostForTwo()));
        holder.restroRating.setText(restaurant.getAvgRating() + Html.fromHtml(" &#9734"));

        if (!restaurant.isClosed()) {
            holder.etaDelivery.setText(getETADelivery(restaurant.getDeliveryTime()));
        } else {
            holder.etaDelivery.setText("N/A");
            // Default symbol for now
        }

        if (restaurant.isClosed()) {
            holder.nextOpeningMsg.setVisibility(View.VISIBLE);
            // No data in provided api
//            holder.nextOpeningMsg.setText("");
        } else {
            holder.nextOpeningMsg.setVisibility(View.GONE);
        }

        holder.dataPos = position;

        if (restaurant.getChain() != null && restaurant.getChain().size() > 1) {
            holder.outletInfo.setVisibility(View.VISIBLE);
            holder.outletInfo.setText(getSpannableString(restaurant.getChain().size() + " outlets open around you", "#F29C37"));
            holder.restroChain = (ArrayList<RestaurantsResponse.Chain>) restaurant.getChain();
        } else {
            holder.outletInfo.setVisibility(View.GONE);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return restroDataset.size();
    }


    public static class ChainDetailsDialog extends DialogFragment {


        public ChainDetailsDialog() {

        }


        public static final ChainDetailsDialog newInstance(ArrayList<RestaurantsResponse.Chain> restroData, String restroName) {
            ChainDetailsDialog fragment = new ChainDetailsDialog();
            Bundle bundle = new Bundle();
            bundle.putString("restroName", restroName);
            bundle.putParcelableArrayList("chainList", restroData);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.chain_list, null);
            builder.setView(view);

            TextView chainName = (TextView) view.findViewById(R.id.chainName);
            chainName.setText(getArguments().getString("restroName"));

            RecyclerView chainList = (RecyclerView) view.findViewById(R.id.chainList);
            chainList.setHasFixedSize(true);

            RecyclerView.LayoutManager chainLytMgr = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            chainList.setLayoutManager(chainLytMgr);

            ArrayList<RestaurantsResponse.Chain> chainArrayList = getArguments().getParcelableArrayList("chainList");
            ChainAdapter chainAdapter = new ChainAdapter(chainArrayList, getActivity());
            chainList.setAdapter(chainAdapter);


            return builder.create();
        }


    }

}



