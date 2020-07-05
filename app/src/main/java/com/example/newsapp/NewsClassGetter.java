package com.example.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsClassGetter extends AsyncTaskLoader<List<News>> {

   private String mUrl;
   public NewsClassGetter(Context context, String url) {
      super(context);
      mUrl = url;
   }
   @Override
   protected void onStartLoading() {
      forceLoad();
   }
   @Override
   public List<News> loadInBackground() {
      if (mUrl == null) {
         return null;
      }
      List<News> stories = NewsClassInfo.fetchStoryData(mUrl);
      return stories;
   }
}
