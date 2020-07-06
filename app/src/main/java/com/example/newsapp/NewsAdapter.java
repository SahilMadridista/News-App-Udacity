package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends ArrayAdapter<News> {

   public NewsAdapter(@NonNull Context context, @NonNull List<News> objects) {
      super(context, -1, objects);
   }

   @NonNull
   @Override
   public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

      ViewHolder viewHolder;
      View listviewofnews = convertView;
      News news = getItem(position);
      if (listviewofnews == null){
         listviewofnews = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
         viewHolder = new ViewHolder(listviewofnews);
         listviewofnews.setTag(viewHolder);
      }
      else
         viewHolder = (ViewHolder) listviewofnews.getTag();
      viewHolder.titleTextView.setText(news.getTitle());
      viewHolder.categoryTextView.setText(news.getcategoryLine());
      Date date = news.getDate();
      if (date != null)
         viewHolder.dateTextView.setText(formatDate(date));
      viewHolder.sectionTextView.setText(news.getSection());
      String imageUrl = news.getImageUrl();
      if (!imageUrl.isEmpty())
         Glide
                 .with(getContext())
                 .load(imageUrl)
                 .into(viewHolder.thumbnailImageView);
      else
         viewHolder.thumbnailImageView.setVisibility(View.GONE);

      return listviewofnews;
   }

   static class ViewHolder{
      @BindView(R.id.news_head)
      TextView titleTextView;
      @BindView(R.id.news_category)
      TextView categoryTextView;
      @BindView(R.id.news_date)
      TextView dateTextView;
      @BindView(R.id.news_section)
      TextView sectionTextView;
      @BindView(R.id.imageview)
      ImageView thumbnailImageView;

      public ViewHolder(View view){
         ButterKnife.bind(this, view);

      }

   }

   private String formatDate(Date dateObject) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
      return dateFormat.format(dateObject);
   }
}
