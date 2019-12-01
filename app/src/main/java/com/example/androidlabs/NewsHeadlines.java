package com.example.androidlabs;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * The main activity of news headlines
 * @author Kaikai Mao
 * @since 11/25/2019
 */
public class NewsHeadlines extends AppCompatActivity {
    /**
     * myListAdapter
     */
    private MyListAdapter myListAdapater;
    /**
     * ArrayList to add item to listview
     */
    private ArrayList<News_item> newsList;
    /**
     * progressbar
     */
    private ProgressBar progressBar;
    /**
     * URL for internet connection
     */
    private String URL;

    /**
     * main method of NewsHeadlines
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_headlines_main);

        Toast.makeText(this, getString(R.string.news_main_toast), Toast.LENGTH_LONG).show();
        Toolbar toolbar = findViewById(R.id.news_headlines_toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        boolean isTablet = findViewById(R.id.fragmentLocation)!=null;
        newsList = new ArrayList<>();

        EditText searchText = findViewById(R.id.search_editText);
        Button searchBtn = findViewById(R.id.searchbutton);

        ListView newsListView = findViewById(R.id.articlesListView);
        myListAdapater = new MyListAdapter(newsList,this);
        myListAdapater.notifyDataSetChanged();
        newsListView.setAdapter(myListAdapater);

        SharedPreferences prefs = getSharedPreferences("news",MODE_PRIVATE);
        searchText.setText(prefs.getString("search",""));


        searchBtn.setOnClickListener(v->{
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("search", searchText.getText().toString());
            editor.commit();

            URL = "https://newsapi.org/v2/everything?apiKey=09224c0a58c8423e8059115b529cbadf&q=" + searchText.getText().toString() ;
            new AsyncHttpTask().execute(URL);

            myListAdapater.notifyDataSetChanged();
        });

        Button favorList = findViewById(R.id.go_to_favorite);
        favorList.setOnClickListener(favorites->{
            Intent newIntent = new Intent(NewsHeadlines.this, News_favouriteList.class);
            startActivity(newIntent);
        });

        newsListView.setOnItemClickListener(((parent, view, position, id) -> {
            News_item item = (News_item)parent.getItemAtPosition(position);
            Bundle dataToPass = new Bundle();
            dataToPass.putSerializable("Article",item);

            if (isTablet) {
                News_DetailedFragment dFragment = new News_DetailedFragment(); //add a DetailFragment
                dFragment.setArguments(dataToPass); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            } else //isPhone
            {
                Intent nextActivity = new Intent(NewsHeadlines.this, News_EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }

        }));


    }


    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.news_menu, menu);
        return true;
    }

    /**
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.gobacktomainActivity:
                Intent intent = new Intent(NewsHeadlines.this,MainActivity.class);
                startActivity(intent);
                break;

            case R.id.overflow_help:
                helpDialog();
                break;
        }
        return true;
    }

    /**
     * instruction of the app, and version number , author
     */
    public void helpDialog()
    {
        View middle = getLayoutInflater().inflate(R.layout.activity_news_help_dialog_box, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Help")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);

        builder.create().show();
    }

    /**
     * Internet connection funcationality
     */
    public class AsyncHttpTask extends AsyncTask<String,Integer,String>{
        /**
         *
         * @param urls
         * @return
         */
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            java.net.URL url;
            HttpsURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpsURLConnection) url.openConnection();

                if (result != null) {

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line= null;
                    while((line = bufferedReader.readLine())!=null){
                        result += line;
                    }
                    getJSONObject(result);
                    return result;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {

            if (result != null) {
                myListAdapater.notifyDataSetChanged();
                Toast.makeText(NewsHeadlines.this, "Data successfully Loaded", LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            } else {
                Toast.makeText(NewsHeadlines.this, "Failed to load data!", LENGTH_SHORT).show();
            }

        }

        /**
         *
         * @param values
         */
        protected void onProgressUpate(Integer... values){
            ProgressBar progressBar = findViewById(R.id.progress_bar);
            progressBar.setProgress(values[0]);
        }

        /**
         *
         * @param result
         */
        private void getJSONObject(String result) {
            try {
                JSONObject response = new JSONObject(result);
                JSONArray posts = response.optJSONArray("articles");
                News_item item;
                Float progress;
                for (int i = 0; i < posts.length(); i++) {

                    JSONObject post = posts.optJSONObject(i);
                    String title = post.optString("title");
                    String image = post.optString("urlToImage");
                    String description = post.optString("description");
                    String url = post.optString("url");
                    item = new News_item();
                    item.setNews_title(title);
                    item.setNews_url(image);
                    item.setNews_url(url);
                    item.setNews_description(description);

                    newsList.add(item);

                    progress = ((i + 1) * 100f) / posts.length();
                    Log.d("load percent :", progress.toString());
                    publishProgress(progress.intValue());

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }



}
