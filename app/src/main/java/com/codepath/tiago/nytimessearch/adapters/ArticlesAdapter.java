package com.codepath.tiago.nytimessearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.tiago.nytimessearch.R;
import com.codepath.tiago.nytimessearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tiago on 9/22/17.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item.
    // Used to cache the views within the item layout for fast access.
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row.
        public TextView tvTitle;
        public ImageView ivImage;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview.
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        }
    }

    // Store a member variable for the articles.
    private List<Article> mArticles;
    // Store the context for easy access.
    private Context mContext;

    // Pass in the article array into the constructor.
    public ArticlesAdapter(Context context, List<Article> articles) {
        this.mArticles = articles;
        this.mContext = context;
    }

    // Easy access to the context object in the recyclerview.
    private Context getContext() {
        return this.mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder.
    @Override
    public ArticlesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout.
        View contactView = inflater.inflate(R.layout.item_article_result, parent, false);

        // Return a new holder instance.
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder.
    @Override
    public void onBindViewHolder(ArticlesAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position.
        Article article = mArticles.get(position);

        // Set item views based on your views and data model.
        viewHolder.tvTitle.setText(article.getHeadline());
        // Populate the image thumbnail.
        String thumbnail = article.getThumbnail();

        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).into(viewHolder.ivImage);
        }
    }

    // Returns the total count of items in the list.
    @Override
    public int getItemCount() {
        return mArticles.size();
    }

}
