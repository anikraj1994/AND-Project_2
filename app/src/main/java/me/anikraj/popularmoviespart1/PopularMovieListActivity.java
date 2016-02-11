package me.anikraj.popularmoviespart1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.anikraj.popularmoviespart1.dummy.DummyContent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PopularMovieListActivity extends AppCompatActivity {
    GridView gridview;
    GridAdapter gridAdapter;
    CoordinatorLayout coordinatorLayout;
    SwipeRefreshLayout swipeLayout;
    SharedPreferences settings;
    ArrayList<GridItem> itemscopy = new ArrayList<>();
    int index;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popularmovie_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        settings = getSharedPreferences("Preff", 0);


        if (findViewById(R.id.popularmovie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetMoviesSorted(null);
            }
        });


        gridview = (GridView) findViewById(R.id.gridview);
        gridAdapter =  new GridAdapter(this,R.layout.grid_item,null);
        // gridview.setAdapter(gridAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if(false)Snackbar.make(coordinatorLayout, "Internet not available.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                else {
                    GridItem item = gridAdapter.getItem(position);

                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString("id",item.getId());
                        arguments.putString("title",item.getTitle());
                        arguments.putString("image", item.getImage());
                        arguments.putString("synopsis", item.getSynopsys());
                        arguments.putString("backdrop",item.getBackdrop());
                        arguments.putDouble("vote_avg", item.getVote_avg());
                        arguments.putInt("vote_count", item.getVote_count());
                        arguments.putString("date", item.getDate());
                        arguments.putInt("cordlay", -1);

                        PopularMovieDetailFragment fragment = new PopularMovieDetailFragment();
                        fragment.setArguments(arguments);
                       // if(getSupportFragmentManager().getFragments()!=null)
                       // getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().getFragments().get(0)).commit();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.popularmovie_detail_container, fragment)
                                .commit();

                    }

                    else{
                        Intent i=new Intent(PopularMovieListActivity.this,PopularMovieDetailActivity.class);
                        i.putExtra("id",item.getId());
                        i.putExtra("title", item.getTitle());
                        i.putExtra("image",item.getImage());
                        i.putExtra("synopsis",item.getSynopsys());
                        i.putExtra("vote_avg",item.getVote_avg());
                        i.putExtra("backdrop",item.getBackdrop());
                        i.putExtra("vote_count",item.getVote_count());
                        i.putExtra("date",item.getDate());
                        i.putExtra("cordlay",R.id.coordinatorLayout);

                        startActivity(i);
                    }


                }
            }
        });



        GetMoviesSorted(savedInstanceState);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sort_popular) {
            SortPopular();
            GetMoviesSorted(null);
            return true;
        }
        else if (id == R.id.sort_mostrated) {
            SortMostRated();
            GetMoviesSorted(null);
            return true;
        }
        else if(id== R.id.sort_favorite){
            GetMoviesFav();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void SortPopular(){

        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("sort_popular", 1);
        editor.commit();
    }

    private void SortMostRated(){
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("sort_popular", 2);
        editor.commit();
    }

    private void GetMoviesSorted(Bundle savedstate){
        if(settings.getInt("sort_popular", 0)==0){
            SortPopular();
            GetMovies(settings.getInt("sort_popular",1));
        }
        else if(settings.getInt("sort_popular", 0)==1){
            if(savedstate == null || !savedstate.containsKey("popular")) {
                GetMovies(settings.getInt("sort_popular",1));}
            else{
                GetRetainedMovies(savedstate,settings.getInt("sort_popular",1));
            }
        }
        else if(settings.getInt("sort_popular", 0)==2){
            if(savedstate == null || !savedstate.containsKey("mostrated")) {
                GetMovies(settings.getInt("sort_popular",1));}
            else{
                GetRetainedMovies(savedstate,settings.getInt("sort_popular",1));
            }
        }
    }

    private void GetRetainedMovies(Bundle savedstate, final int type){
        swipeLayout.setRefreshing(true);
        ArrayList<GridItem> items = new ArrayList<>();
        items=(type==1)?(ArrayList<GridItem>)savedstate.get("popular"):(ArrayList<GridItem>)savedstate.get("mostrated");
        itemscopy=items;
        gridAdapter.setGridData(items);
        gridview.setAdapter(gridAdapter);
        swipeLayout.setRefreshing(false);
    }

    private void GetMoviesFav(){
        swipeLayout.setRefreshing(true);
        ArrayList<GridItem> items = FavoriteHelper.getall(getContentResolver());
    if(items!=null){
        itemscopy=items;
        gridAdapter.setGridData(items);
        gridview.setAdapter(gridAdapter);}
        else Snackbar.make(coordinatorLayout, "No Favorites.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        swipeLayout.setRefreshing(false);
    }

    private void GetMovies(final int type){
        swipeLayout.setRefreshing(true);
        final ArrayList<GridItem> items = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(PopularMovieListActivity.this);
        String url ="http://api.themoviedb.org/3/discover/movie?sort_by="+(type==1?Constants.SORT_POPULAR:Constants.SORT_MOSTRATED)+"&api_key="+Constants.API_KEY;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonElement jelement = new JsonParser().parse(response);
                        JsonObject jobject = jelement.getAsJsonObject();
                        JsonArray jarray = jobject.getAsJsonArray("results");
                        for(int i=0;i<jarray.size();i++){
                            jobject = jarray.get(i).getAsJsonObject();
                            items.add(new GridItem(jobject.get("id").toString(),jobject.get("poster_path").toString(),jobject.get("poster_path").toString(),jobject.get("original_title").toString(),jobject.get("overview").toString(),Double.parseDouble(jobject.get("vote_average").toString()),Integer.parseInt(jobject.get("vote_count").toString()),jobject.get("backdrop_path").toString(),jobject.get("release_date").toString()));
                        }
                        itemscopy=items;
                        gridAdapter.setGridData(items);
                        gridview.setAdapter(gridAdapter);
                        if(isOnline())
                            Snackbar.make(coordinatorLayout, type==1?"Showing most popular.":"Showing highest rated.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        else
                            Snackbar.make(coordinatorLayout, "Internet not available.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        swipeLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(coordinatorLayout, "Something went wrong :( Check Internet.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        queue.add(stringRequest);
    }





    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        index = state.getInt("scroll_pos");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (index != 0)
            gridview.setSelection(index);
        index = 0;
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        index = gridview.getFirstVisiblePosition();
        state.putInt("scroll_pos", index);
        if(settings.getInt("sort_popular", 0)==1)
            state.putParcelableArrayList("popular", (ArrayList<? extends Parcelable>) itemscopy);
        else if(settings.getInt("sort_popular", 0)==2)
            state.putParcelableArrayList("mostrated", (ArrayList<? extends Parcelable>) itemscopy);
    }


    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }



}