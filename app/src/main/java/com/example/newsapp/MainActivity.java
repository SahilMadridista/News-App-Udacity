package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

   com.google.android.material.appbar.MaterialToolbar toolbar;
   private NewsAdapter newsAdapter;
   private static final String GUARDIAN_API_URL = "https://content.guardianapis.com/search";
   private static final int STORY_ID = 1;

   @BindView(R.id.empty_view)
   TextView emptyStateTextView;

   @BindView(R.id.loading_indicator)
   View loadingIndicatorView;

   @BindView(R.id.news_list)
   ListView listView;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      toolbar = findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      ButterKnife.bind(this);
      newsAdapter = new NewsAdapter(this, new ArrayList<News>());
      listView.setAdapter(newsAdapter);
      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            News currentNews = newsAdapter.getItem(i);
            Uri storyUri = Uri.parse(currentNews.getUrl());
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, storyUri);
            startActivity(websiteIntent);
         }
      });
      ConnectivityManager connectivityManager = (ConnectivityManager)
              getSystemService(Context.CONNECTIVITY_SERVICE);
      assert connectivityManager != null;

      NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
      if (networkInfo != null && networkInfo.isConnected()) {
         LoaderManager loaderManager = getLoaderManager();
         loaderManager.initLoader(STORY_ID, null, this);
      } else {
         loadingIndicatorView.setVisibility(View.GONE);
         emptyStateTextView.setText("Could not connect to network");
      }
   }

   public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
      Uri baseUri = Uri.parse(GUARDIAN_API_URL);
      Uri.Builder builder = baseUri.buildUpon();
      SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
      String minDate = sharedPrefs.getString(getString(R.string.minimum_date_key), getString(R.string.settings_date));
      String section = sharedPrefs.getString(getString(R.string.select_section_key), getString(R.string.select_section_default_value));
      builder.appendQueryParameter("api-key", getResources().getString(R.string.api_key));
      builder.appendQueryParameter("show-tags", "contributor");
      builder.appendQueryParameter("show-fields", "thumbnail");
      builder.appendQueryParameter("from-date", minDate);
      builder.appendQueryParameter("section", section);

      return new NewsClassGetter(this, builder.toString());
   }

   @Override
   public void onLoadFinished(Loader<List<News>> loader, List<News> stories) {
      loadingIndicatorView.setVisibility(View.GONE);
      emptyStateTextView.setText(R.string.no_news_found);
      newsAdapter.clear();

      if (stories != null && !stories.isEmpty()) {
         emptyStateTextView.setVisibility(View.GONE);
         newsAdapter.addAll(stories);
      }
      else
         emptyStateTextView.setVisibility(View.VISIBLE);
   }

   @Override
   public void onLoaderReset(Loader<List<News>> loader) {
      newsAdapter.clear();
   }


   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.menu_item, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      int id = item.getItemId();
      if (id == R.id.settings) {
         Intent settingsIntent = new Intent(this, SecondActivity.class);
         startActivity(settingsIntent);
         return true;
      }
      return super.onOptionsItemSelected(item);
   }

}