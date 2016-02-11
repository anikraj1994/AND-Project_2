package me.anikraj.popularmoviespart1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> implements View.OnClickListener {
    private List<ReviewItem> itemsData;
    private Context mContext;
    private static RecyclerViewClickListener itemListener;


    public ReviewAdapter(Context mContext, List<ReviewItem> itemsData, RecyclerViewClickListener itemListener) {
        this.itemsData = itemsData;
        this.mContext = mContext;
        this.itemListener = itemListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, null);

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
       // Picasso.with(mContext).load("http://img.youtube.com/vi/"+itemsData.get(position).getUrl()+"/0.jpg").placeholder(R.drawable.load).into(viewHolder.imgViewIcon);
        viewHolder.author.setText(itemsData.get(position).getAuthor());
        viewHolder.content.setText(itemsData.get(position).getContent());


    }


    public void setGridData(List<ReviewItem> mGridData) {
        this.itemsData = mGridData;
        notifyDataSetChanged();
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView author,content;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            author = (TextView) itemLayoutView.findViewById(R.id.author);
            content = (TextView) itemLayoutView.findViewById(R.id.content);
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