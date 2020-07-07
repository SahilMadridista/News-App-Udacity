package com.example.newsapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_second);
   }

   public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
      @RequiresApi(api = Build.VERSION_CODES.N)
      @Override
      public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         addPreferencesFromResource(R.xml.settings_main);

         Preference minimum_Date = findPreference(getString(R.string.minimum_date_key));
         bindPreferenceSummaryToValue(minimum_Date);

         Preference select_Section = findPreference(getString(R.string.select_section_key));
         bindPreferenceSummaryToValue(select_Section);
      }

      @RequiresApi(api = Build.VERSION_CODES.N)
      @Override
      public boolean onPreferenceChange(Preference preference, Object value) {
         String stringValue = value.toString();
         if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);
            if (index >= 0) {
               CharSequence[] labels = listPreference.getEntries();
               preference.setSummary(labels[index]);
            }
         } else {
            try {
               //Date date = new SimpleDateFormat("yyyy-MM-dd").parse(stringValue);
            } catch (Exception e) {
               Toast.makeText(getContext(), "Date format must be YYYY-MM-DD", Toast.LENGTH_LONG).show();
               return false;
            }
            preference.setSummary(stringValue);
         }
         return true;
      }

      @RequiresApi(api = Build.VERSION_CODES.N)
      private void bindPreferenceSummaryToValue(Preference preference) {
         preference.setOnPreferenceChangeListener(this);
         SharedPreferences preferences =
                 PreferenceManager.getDefaultSharedPreferences(preference.getContext());
         String preferenceString = preferences.getString(preference.getKey(), "");
         onPreferenceChange(preference, preferenceString);
      }
   }

}