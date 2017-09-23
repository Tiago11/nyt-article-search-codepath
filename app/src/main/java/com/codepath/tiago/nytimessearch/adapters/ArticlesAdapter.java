package com.codepath.tiago.nytimessearch.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.tiago.nytimessearch.R;
import com.codepath.tiago.nytimessearch.models.Article;
import com.codepath.tiago.nytimessearch.utils.DynamicHeightImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by tiago on 9/22/17.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    // Tag for logging.
    private final String TAG = ArticlesAdapter.class.toString();

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

    // Inflates a layout from XML and returns it in the ViewHolder.
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

    // Populates data into the viewHolder.
    @Override
    public void onBindViewHolder(ArticlesAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position.
        Article article = mArticles.get(position);
        // Bind the data to the viewHolder.
        viewHolder.bind(article);
    }

    // Returns the total count of items in the list.
    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    // Provide a direct reference to each of the views within a data item.
    // Used to cache the views within the item layout for fast access.
    public class ViewHolder extends RecyclerView.ViewHolder implements Target {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row.
        public TextView tvTitle;
        public TextView tvSnippet;
        //public ImageView ivImage;
        public DynamicHeightImageView ivImage;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview.
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvSnippet = (TextView) itemView.findViewById(R.id.tvSnippet);
            ivImage = (DynamicHeightImageView) itemView.findViewById(R.id.ivImage);
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            // Calculate the image ratio of the loaded bitmap.
            float ratio = (float) bitmap.getHeight() / (float) bitmap.getWidth();
            // Set the ration for the image.
            ivImage.setHeightRatio(ratio);
            // Load the image into the view.
            ivImage.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.e(TAG, "Error loading a thumbnail image.");
            Bitmap errorImage = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.error);
            ivImage.setImageBitmap(errorImage);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Bitmap placeholderImage = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.placeholder2);
            ivImage.setImageBitmap(placeholderImage);
        }


        /*
         * Sets the article information into their respective views.
         */
        public void bind(Article article) {

            // Set item views based on your views and data model.
            this.tvTitle.setText(article.getHeadline());
            this.tvSnippet.setText(article.getmSnippet());
            // Populate the image thumbnail.
            String thumbnail = article.getThumbnail();

            if (!TextUtils.isEmpty(thumbnail)) {
                Picasso.with(getContext()).load(thumbnail).into(this.ivImage);
            }
        }
    }
}
