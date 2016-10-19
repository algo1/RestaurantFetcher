package com.lokesh.restaurantfetcher;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView restroList;
    private RecyclerView.Adapter restroAdapter;
    private RecyclerView.LayoutManager restroLytMgr;

    private List<RestaurantsResponse.Restaurant> restroDatum;
    ProgressDialog pd;

    private int pageNumber = 0;
    private boolean loadInProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restroList = (RecyclerView) findViewById(R.id.restroList);


        pd = ProgressDialog.show(this, "Fetching ", "Restaurants in your area", true, false);

        fetchandShowList();


        restroList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = restroList.getLayoutManager().getItemCount();
                int lastVisibleItem = ((LinearLayoutManager) restroList.getLayoutManager()).findLastVisibleItemPosition();

                if (!loadInProgress
                        && totalItemCount <= (lastVisibleItem + 5)) {
                    loadInProgress = true;
                    fetchandShowList();
                }
            }
        });
    }


    public void passDataToListView(List<RestaurantsResponse.Restaurant> datum) {

        if (pageNumber == 0) {

            restroDatum = datum;

            restroList.setHasFixedSize(true);

            restroLytMgr = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            restroList.setLayoutManager(restroLytMgr);

            restroAdapter = new RestroAdapter(restroDatum, this);
            restroList.setAdapter(restroAdapter);

        } else {
            restroDatum.addAll(datum);
            restroList.invalidate();
            restroAdapter.notifyDataSetChanged();
        }


    }

    public void fetchandShowList() {

        String url = "https://api.myjson.com/bins/ngcc?page=" + String.valueOf(pageNumber);


        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Log.d("Lokesh", response);
                RestaurantsResponse data = gson.fromJson(response, RestaurantsResponse.class);
                dismissPD();
                passDataToListView(data.getRestaurants());
                pageNumber++;
                loadInProgress = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissPD();
                Toast.makeText(getApplicationContext(), "Error Occured , Please try later", Toast.LENGTH_LONG).show();
                loadInProgress = false;
                Log.d("Lokesh", "Error  : " + error.toString());

            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(req);

    }

    public void dismissPD() {
        if (pd != null) {
            pd.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        dismissPD();
    }


}
