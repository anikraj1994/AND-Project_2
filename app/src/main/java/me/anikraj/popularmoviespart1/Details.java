package me.anikraj.popularmoviespart1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Details extends AppCompatActivity implements RecyclerViewClickListener{
    String idmov;
    ArrayList<TrailerItem> items;
    RecyclerView recyclerView;
    TrailerAdapter ta;
    CoordinatorLayout coordinatorLayout;
    RecyclerView recyclerreview;
    ReviewAdapter ra;
    Boolean favo=false;
    ImageButton fav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.cordinatordetails);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        idmov=i.getStringExtra("id");
        String title=i.getStringExtra("title");
        String image=i.getStringExtra("image");
        String synopsis= i.getStringExtra("synopsis");
        String backdrop=i.getStringExtra("backdrop");
        double vote_avg=i.getDoubleExtra("vote_avg",-1);
        int vote_count=i.getIntExtra("vote_count",-1);
        String date=i.getStringExtra("date");

        getSupportActionBar().setTitle(title.replace("\"", ""));

        Picasso.with(this).load("http://image.tmdb.org/t/p/w342" + backdrop.substring(1)).into((ImageView) findViewById(R.id.backdrop));
        Picasso.with(this).load("http://image.tmdb.org/t/p/w342" + image.substring(1)).placeholder(R.drawable.load).into((ImageView) findViewById(R.id.image));
        ((TextView)findViewById(R.id.title)).setText(title.replace("\"", ""));
        ((TextView)findViewById(R.id.date)).setText(date.substring(1, 5));
        ((TextView)findViewById(R.id.synopsis)).setText(synopsis);
        ((TextView)findViewById(R.id.rating)).setText(vote_avg + "");
        ((TextView)findViewById(R.id.votecount)).setText(vote_count + "");

        recyclerreview= (RecyclerView) findViewById(R.id.listreview);
        recyclerreview.setLayoutManager(new MyLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        ra=new ReviewAdapter(this,null,null);
        recyclerreview.setAdapter(ra);

        recyclerView = (RecyclerView) findViewById(R.id.trailerlist);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));

        fav=(ImageButton)findViewById(R.id.favorite);
        //items = new ArrayList<>();
        //items.add(new TrailerItem("ZIM1HydF9UA"));
        ta=new TrailerAdapter(this,null,this);
        //ta.setGridData(items);
        //recyclerView.setAdapter(ta);
       // items.add(new TrailerItem("9vN6DHB6bJc"));items.add(new TrailerItem("ZIM1HydF9UA"));
        //ta.setGridData(items);
        recyclerView.setAdapter(ta);

        GetFav(idmov);
        GetTrailers();
        GetReviews();

    }

    @Override
    public void recyclerViewListClicked(View v, int position){
       // Toast.makeText(this,"http://www.youtube.com/watch?v=" + items.get(position).getUrl(),Toast.LENGTH_LONG).show();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + items.get(position).getUrl())));
    }

    public void GetFav(String id){
        //SetFav();
        SetNotFav();
    }

    public void fav(View v){

        if(favo)SetNotFav();
        else {
            Snackbar.make(coordinatorLayout, "Added to Favorite.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            SetFav();
        }


    }

    public void SetFav(){
        favo=true;
        fav.setImageResource(R.drawable.ic_favorite_white_36dp);
        //nackbar.make(coordinatorLayout, "Added to Favorite.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
    public void SetNotFav(){
        favo=false;
        fav.setImageResource(R.drawable.ic_favorite_border_white_36dp);
        //Snackbar.make(coordinatorLayout, "", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share) {
            //Toast.makeText(this,idmov,Toast.LENGTH_LONG).show();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v="+items.get(0).getUrl());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    private void GetTrailers(){
        final ArrayList<TrailerItem> itemss = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(Details.this);
        String url ="http://api.themoviedb.org/3/movie/"+idmov+"/videos?api_key="+Constants.API_KEY;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonElement jelement = new JsonParser().parse(response);
                        JsonObject jobject = jelement.getAsJsonObject();
                        JsonArray jarray = jobject.getAsJsonArray("results");
                        for(int i=0;i<jarray.size();i++){
                            jobject = jarray.get(i).getAsJsonObject();
                            itemss.add(new TrailerItem(jobject.get("key").toString().substring(1, jobject.get("key").toString().length()-1)));
                            //Toast.makeText(getApplicationContext(),jobject.get("key").toString(),Toast.LENGTH_SHORT ).show();

                        }
                        items=itemss;
                        ta.setGridData(itemss);
                        recyclerView.setAdapter(ta);

                        //recyclerView.setAdapter(ta);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
    private void GetReviews(){
        final ArrayList<ReviewItem> itemss = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(Details.this);
        String url ="http://api.themoviedb.org/3/movie/"+idmov+"/reviews?api_key="+Constants.API_KEY;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonElement jelement = new JsonParser().parse(response);
                        JsonObject jobject = jelement.getAsJsonObject();
                        JsonArray jarray = jobject.getAsJsonArray("results");
                        for(int i=0;i<jarray.size();i++){
                            jobject = jarray.get(i).getAsJsonObject();
                            itemss.add(new ReviewItem(jobject.get("author").toString().substring(1, jobject.get("author").toString().length() - 1), jobject.get("content").toString().substring(1, jobject.get("content").toString().length() - 1)));
                            //Toast.makeText(getApplicationContext(),jobject.get("author").toString(),Toast.LENGTH_SHORT ).show();

                        }
                        //items=itemss;
                        ra.setGridData(itemss);
                        recyclerreview.setAdapter(ra);

                        //recyclerView.setAdapter(ta);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
}
