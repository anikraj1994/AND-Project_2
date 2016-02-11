package me.anikraj.popularmoviespart1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import me.anikraj.popularmoviespart1.dummy.DummyContent;

public class PopularMovieDetailFragment extends Fragment implements RecyclerViewClickListener {
    String idmov,title,image,synopsis,backdrop,date;
    double vote_avg;
    int vote_count,cord;

    ArrayList<TrailerItem> items;
    RecyclerView recyclerView;
    TrailerAdapter ta;
    CoordinatorLayout coordinatorLayout;
    RecyclerView recyclerreview;
    ReviewAdapter ra;
    Boolean favo=false;
    ImageButton fav;


   // private DummyContent.DummyItem mItem;


    public PopularMovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
         //   mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

        idmov=getArguments().getString("id");
        title=getArguments().getString("title");
         image=getArguments().getString("image");
         synopsis= getArguments().getString("synopsis");
         backdrop=getArguments().getString("backdrop");
         vote_avg=getArguments().getDouble("vote_avg", -1);
         vote_count=getArguments().getInt("vote_count", -1);
         date=getArguments().getString("date");
        cord=getArguments().getInt("cordlay");

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(title.replace("\"", ""));
            }




     //   }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.popularmovie_detail, container, false);

        if(cord!=-1)coordinatorLayout=(CoordinatorLayout)rootView.findViewById(cord);
        else coordinatorLayout=(CoordinatorLayout)rootView.findViewById(R.id.cordinatordetails);

        Picasso.with(rootView.getContext()).load("http://image.tmdb.org/t/p/w342" + image.substring(1)).placeholder(R.drawable.load).into((ImageView) rootView.findViewById(R.id.image));

        ((TextView)rootView.findViewById(R.id.title)).setText(title.replace("\"", ""));
        ((TextView)rootView.findViewById(R.id.date)).setText(date.substring(1, 5));
        ((TextView)rootView.findViewById(R.id.synopsis)).setText(synopsis);
        ((TextView)rootView.findViewById(R.id.rating)).setText(vote_avg + "");
        ((TextView)rootView.findViewById(R.id.votecount)).setText(vote_count + "");

        recyclerreview= (RecyclerView) rootView.findViewById(R.id.listreview);
        recyclerreview.setLayoutManager(new MyLinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL,false));
        ra=new ReviewAdapter(rootView.getContext(),null,null);
        recyclerreview.setAdapter(ra);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.trailerlist);
        recyclerView.setLayoutManager(new GridLayoutManager(rootView.getContext(), 1, GridLayoutManager.HORIZONTAL, false));

        fav=(ImageButton)rootView.findViewById(R.id.favorite);

        //FavoriteObject temp=FavoriteHelper.getTodo(getActivity().getContentResolver(), Integer.parseInt(idmov));
      /*  if(temp!=null) {
            SetFav();
            Toast.makeText(getContext(), temp.title, Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(getContext(), "temp null", Toast.LENGTH_SHORT).show();*/

        FavoriteObject tempr=FavoriteHelper.getTodo(getActivity().getContentResolver(), title);
        if(tempr!=null) {
            SetFav();
            //Toast.makeText(getContext(), tempr.title, Toast.LENGTH_SHORT).show();
        }
        //else Toast.makeText(getContext(), "tempr null", Toast.LENGTH_SHORT).show();

        //FavoriteHelper.listall(getActivity().getContentResolver());
       // FavoriteHelper.listall2(getActivity().getContentResolver());
        fav.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something
                if(favo){
                    FavoriteHelper.deleteTodo2(getActivity().getContentResolver(),title);
                    SetNotFav();
                }
                else {

                    FavoriteObject f=new FavoriteObject(idmov,title,image,synopsis,backdrop,vote_avg,vote_count,date,true);
                    FavoriteHelper.createTodo( getActivity().getContentResolver(),f);

                  //  Toast.makeText(getContext(),"Added to Favorite.",Toast.LENGTH_SHORT).show();
                    //Snackbar.make(coordinatorLayout, "Added to Favorite.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    SetFav();
                }
            }
        });
        // items = new ArrayList<>();
        //items.add(new TrailerItem("ZIM1HydF9UA"));
        ta=new TrailerAdapter(rootView.getContext(),null,this);
        //ta.setGridData(items);
        //recyclerView.setAdapter(ta);
        // items.add(new TrailerItem("9vN6DHB6bJc"));items.add(new TrailerItem("ZIM1HydF9UA"));
        //ta.setGridData(items);
        recyclerView.setAdapter(ta);

      //  GetFav(idmov);
        GetTrailers();
       GetReviews();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_details, menu);
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

    @Override
    public void recyclerViewListClicked(View v, int position) {
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

    private void GetTrailers(){
        final ArrayList<TrailerItem> itemss = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
    private void GetReviews(){
        final ArrayList<ReviewItem> itemss = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }

}
