package me.anikraj.popularmoviespart1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * An activity representing a single Popular Movie detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link PopularMovieListActivity}.
 */
public class PopularMovieDetailActivity extends AppCompatActivity {

    String idmov;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popularmovie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.cordinatordetails);
        Intent i = getIntent();
        idmov=i.getStringExtra("id");
        String title=i.getStringExtra("title");
        String image=i.getStringExtra("image");
        String synopsis= i.getStringExtra("synopsis");
        String backdrop=i.getStringExtra("backdrop");
        double vote_avg=i.getDoubleExtra("vote_avg",-1);
        int vote_count=i.getIntExtra("vote_count",-1);
        String date=i.getStringExtra("date");
        int cord=i.getIntExtra("cordlay",-1);
        Picasso.with(this).load("http://image.tmdb.org/t/p/w342" + backdrop.substring(1)).into((ImageView) findViewById(R.id.backdrop));

        if (actionBar != null) {
            actionBar.setTitle(title.replace("\"", ""));
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString("id",idmov);
            arguments.putString("title",title);
            arguments.putString("image",image);
            arguments.putString("backdrop",backdrop);
            arguments.putString("synopsis",synopsis);
            arguments.putDouble("vote_avg",vote_avg);
            arguments.putInt("vote_count",vote_count);
            arguments.putString("date",date);
            arguments.putInt("cordlay",cord);

            PopularMovieDetailFragment fragment = new PopularMovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.popularmovie_detail_container, fragment)
                    .commit();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, PopularMovieListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
