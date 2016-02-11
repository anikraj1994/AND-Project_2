package me.anikraj.popularmoviespart1;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> implements View.OnClickListener {
    private List<TrailerItem> itemsData;
    private Context mContext;
    private static RecyclerViewClickListener itemListener;


    public TrailerAdapter(Context mContext, List<TrailerItem> itemsData,RecyclerViewClickListener itemListener) {
        this.itemsData = itemsData;
        this.mContext = mContext;
        this.itemListener = itemListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

       // viewHolder.imgViewIcon.setImageResource();
        Picasso.with(mContext).load("http://img.youtube.com/vi/"+itemsData.get(position).getUrl()+"/0.jpg").placeholder(R.drawable.load).into(viewHolder.imgViewIcon);


    }


    public void setGridData(List<TrailerItem> mGridData) {
        this.itemsData = mGridData;
        notifyDataSetChanged();
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imgViewIcon;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.trailer_item_image);

            ImageButton click=(ImageButton)itemLayoutView.findViewById(R.id.clicktrailer);
            click.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            itemListener.recyclerViewListClicked(v, this.getAdapterPosition());

        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(itemsData!=null)return itemsData.size();
        else return 0;}


    @Override
    public void onClick(View v) {

    }


}