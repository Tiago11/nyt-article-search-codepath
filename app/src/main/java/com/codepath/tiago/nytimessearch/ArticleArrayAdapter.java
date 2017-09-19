package com.codepath.tiago.nytimessearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tiago on 9/19/17.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for position.
        Article article = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);

            ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
            // Clear out recycled image from convertView from last time.
            ivImage.setImageResource(0);

            TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            tvTitle.setText(article.getHeadline());

            // Populate the image thumbnail.
            String thumbnail = article.getThumbnail();

            if (!TextUtils.isEmpty(thumbnail)) {
                Picasso.with(getContext()).load(thumbnail).into(ivImage);
            }
        }

        return convertView;
    }
}
