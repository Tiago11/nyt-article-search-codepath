package com.codepath.tiago.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.tiago.nytimessearch.R;
import com.codepath.tiago.nytimessearch.adapters.ArticleArrayAdapter;
import com.codepath.tiago.nytimessearch.models.Article;
import com.codepath.tiago.nytimessearch.models.Filter;
import com.codepath.tiago.nytimessearch.network.ArticleClient;
import com.codepath.tiago.nytimessearch.utils.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    EditText etQuery;
    GridView gvResults;
    Button btnSearch;

    List<Article> mArticles;
    ArticleArrayAdapter mAdapter;
    Filter mFilter;

    private final int REQUEST_CODE_FILTER_ACTIVITY = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Get the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find references to the views in the layout.
        setupViews();

        // Set the adapter for the GridView.
        setupAdapter();

        // Set the listeners.
        setupListeners();
    }

    /*
     * Get the references for the different views in the layout.
     */
    private void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        mFilter = null;
    }

    /*
     * Initialize and set the Adapter for the |mArticles| collection and the |gvResults| GridView.
     */
    private void setupAdapter() {
        mArticles = new ArrayList<>();
        mAdapter = new ArticleArrayAdapter(this, mArticles);
        gvResults.setAdapter(mAdapter);
    }

    /*
     * Set the on item click listener for the |gvResults| GridView. It creates the intent for the
     * ArticleActivity, passes the article clicked to it and calls the new activity.
     */
    private void setupListeners() {
        // Hook up the listener for endless scrolling.
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list.
                // Add whatever code is needed to append new items to your AdapterView.
                // TODO remove Toast under this.
                Toast.makeText(SearchActivity.this, "Page: " + page, Toast.LENGTH_LONG).show();
                loadNextDataFromApi(page);
                // or loadNextDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });

        // Hook up the listener for grid click.
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Create an intent to display the article.
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);

                // Get the article to display.
                Article article = mArticles.get(i);

                // Pass in that article intent.
                intent.putExtra("article", article);

                // Launch the activity.
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_filter:
                showFilters();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_FILTER_ACTIVITY) {
            // Set the filter.
            mFilter = (Filter) data.getSerializableExtra("filter");
        }
    }

    /*
     * Launches the FilterActivity, where the user can set the filter for the search.
     */
    private void showFilters() {
        Intent i = new Intent(SearchActivity.this, FilterActivity.class);
        i.putExtra("filter", mFilter);
        startActivityForResult(i, REQUEST_CODE_FILTER_ACTIVITY);
    }

    /*
     * Event fired when the user provides a query for the search.
     */
    public void onArticleSearch(View view) {

        // Get the query made by the user.
        String query = etQuery.getText().toString();

        // Clear the results of the previous search.
        mArticles.clear();
        mAdapter.notifyDataSetChanged();

        // Make the API call to get the articles for this query..
        fetchArticles(query);

    }

    /*
     * Append the next page of data into the adapter.
     * This method sends out a network request and appends new data items to your adapter.
     */
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        String query = etQuery.getText().toString();
        ArticleClient articleClient = new ArticleClient();
        articleClient.getArticlesNextPage(query, offset, mFilter, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");

                    List<Article> arr = Article.fromJsonArray(articleJsonResults);

                    Log.d("HOLA", "PRIMER ART: " + arr.get(0).getHeadline());
                    Log.d("HOLA", "SIZE: " + arr.size());

                    mArticles.addAll(arr);

                    Log.d("HOLA", "Size: " + mArticles.size());


                    mAdapter.notifyDataSetChanged();



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("ERROR", "status: " + statusCode + " response: " + responseString);
            }
            //  --> Deserialize and construct new model objects from the API response
            //  --> Append the new data objects to the existing set of items inside the array of items
            //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
        });
    }

    /*
     * Fetch the articles from the API using the query |query| and the current filters |mFilter|,
     * and set the results into the collection, notifying the adapter.
     */
    private void fetchArticles(String query) {
        // Create an article client.
        ArticleClient articleClient = new ArticleClient();
        articleClient.getArticles(query, mFilter, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");

                    mArticles.addAll(Article.fromJsonArray(articleJsonResults));
                    mAdapter.notifyDataSetChanged();
                    Log.d("DEBUG", mArticles.toString());
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(SearchActivity.this, "Error in the API call", Toast.LENGTH_LONG).show();
            }
        });
    }
}
