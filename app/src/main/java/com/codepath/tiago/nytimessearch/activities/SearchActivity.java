package com.codepath.tiago.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.tiago.nytimessearch.R;
import com.codepath.tiago.nytimessearch.adapters.ArticlesAdapter;
import com.codepath.tiago.nytimessearch.models.Article;
import com.codepath.tiago.nytimessearch.models.Filter;
import com.codepath.tiago.nytimessearch.network.ArticleClient;
import com.codepath.tiago.nytimessearch.utils.EndlessRecyclerViewScrollListener;
import com.codepath.tiago.nytimessearch.utils.ItemClickSupport;
import com.codepath.tiago.nytimessearch.utils.SpacesItemDecoration;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    // Views.
    EditText etQuery;
    RecyclerView rvResults;
    Button btnSearch;

    // Models and adapter.
    List<Article> mArticles;
    ArticlesAdapter mAdapter;
    Filter mFilter;

    // Reference to the scroll listener.
    private EndlessRecyclerViewScrollListener mScrollListener;

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

        // Set the ArticlesAdapter and the RecyclerView.
        setupAdapterAndRecyclerView();
    }

    /*
     * Get the references for the different views in the layout.
     */
    private void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        rvResults = (RecyclerView) findViewById(R.id.rvResults);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        mFilter = null;
    }

    /*
     * Initialize and set listeners for the ArticlesAdapter and for the RecyclerView.
     */
    private void setupAdapterAndRecyclerView() {
        mArticles = new ArrayList<>();

        mAdapter = new ArticlesAdapter(this, mArticles);
        rvResults.setAdapter(mAdapter);

        // Create and set a StaggeredGridLayout for the RecyclerView.
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(gridLayoutManager);

        // Create and set an endless scroll listener for the RecyclerView.
        mScrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list.
                loadNextDataFromApi(page);
            }
        };
        rvResults.addOnScrollListener(mScrollListener);

        // Add dividers to the staggered grid.
        SpacesItemDecoration decoration = new SpacesItemDecoration(20);
        rvResults.addItemDecoration(decoration);

        // Add onItemClickListener to the recyclerView.
        ItemClickSupport.addTo(rvResults).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        // Create an intent to display the article.
                        Intent intent = new Intent(SearchActivity.this, ArticleActivity.class);
                        // Get the article to display.
                        Article article = mArticles.get(position);
                        // Pass in that article intent.
                        intent.putExtra("article", article);

                        // Launch the activity.
                        startActivity(intent);
                    }
                }
        );
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
                // Start the filterActivity.
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
     * Makes the API call with that query and the filters (if any), and populates the row views.
     */
    public void onArticleSearch(View view) {

        // Get the query made by the user.
        String query = etQuery.getText().toString();

        // Clear the results of the previous search.
        mArticles.clear();
        mAdapter.notifyDataSetChanged();
        // Reset endless scroll listener because we are performing a new search.
        mScrollListener.resetState();

        // Make the API call to get the articles for this query..
        fetchArticles(query);
    }

    /*
     * Fetch the articles from the API using the query |query| and the current filters |mFilter|,
     * and set the results into the collection, notifying the adapter.
     */
    private void fetchArticles(String query) {
        // Create an article client with the given NYT API KEY.
        ArticleClient articleClient = new ArticleClient(getString(R.string.nyt_article_search_api_key));
        articleClient.getArticles(query, mFilter, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                onApiCallSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(SearchActivity.this, "Error in the API call", Toast.LENGTH_LONG).show();
                Log.d("ERROR", "status: " + statusCode + " response: " + responseString);
            }
        });
    }

    /*
     * Append the next page of data into the adapter.
     * This method sends out a network request and appends new data items to your adapter.
     */
    public void loadNextDataFromApi(int offset) {
        String query = etQuery.getText().toString();
        ArticleClient articleClient = new ArticleClient(getString(R.string.nyt_article_search_api_key));
        articleClient.getArticlesNextPage(query, offset, mFilter, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                onApiCallSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("ERROR", "status: " + statusCode + " response: " + responseString);
            }
        });
    }

    /*
     * Parses the JSON response, creates model objects from it and
     * notifies the adapter to update the views.
     */
    private void onApiCallSuccess(int statusCode, Header[] headers, JSONObject response) {
        JSONArray articleJsonResults = null;

        try {
            articleJsonResults = response.getJSONObject("response").getJSONArray("docs");

            // record this value before making any changes to the existing list.
            int curSize = mAdapter.getItemCount();

            // replace this line with wherever you get new records.
            // update the existing list.
            List<Article> newArticles = Article.fromJsonArray(articleJsonResults);
            mArticles.addAll(newArticles);

            // curSize should represent the first element that got added.
            // newItems.size() represents the itemCount.
            mAdapter.notifyItemRangeInserted(curSize, newArticles.size());

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
}
