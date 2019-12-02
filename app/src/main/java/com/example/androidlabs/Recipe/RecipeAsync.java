package com.example.androidlabs.Recipe;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidlabs.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class RecipeAsync extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "RecipeAsyncTask";
    protected static final String TABLE_NAME = "resultTable";
    protected static SQLiteDatabase db = null;
    protected static RecipeDatabaseHelper dbHelper = null;
    protected static ProgressBar progressBar = null;
    protected static Button searchBtn = null;
    protected static EditText editText = null;
    protected static boolean chicken;

    /**
     * This Method is the onCreate() for the class RecipeAsync. It sets up access to the database,
     * saves last search in SharedPrefrences and implements a progressBar for use with the RecipeQuery which
     * pulls the data from the weburl on the search. It also shows Toasts to confirm your search.
     *
     * @param savedInstanceState Same as param in @see AppCompatActivity.onCreate()
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.recipe_activity);

        progressBar = (ProgressBar) findViewById(R.id.recipeSearchProgressBar);
        searchBtn =  findViewById(R.id.recipeSearchButton);
        editText = findViewById(R.id.searchEditText);


        super.onCreate(savedInstanceState);
        dbHelper = new RecipeDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        pref.getBoolean("chicken", chicken);

        RecipeQuery theQuery = new RecipeQuery();
        theQuery.execute();





        if(chicken) {
            Toast toast = Toast.makeText(this,
                    "Hmm pretty sure you just asked to search chicken.",
                    Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(this,
                    "Oh looking for lasagna? Well most certainly one second please.",
                    Toast.LENGTH_SHORT);
            toast.show();
        }



    }

    /**
     * This just makes it go back to the Calling Activity when it is finished passing the normal information
     *
     * @param requestCode Same as on @See AppCompatActivity.onActivityResult()
     * @param resultCode Same as on @See AppCompatActivity.onActivityResult()
     * @param data Same as on @See AppCompatActivity.onActivityResult()
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();

    }


    /**
     * This Class extends AsyncTask, it is used to pull data from JSON link.
     *
     *
     */
    private class RecipeQuery extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        /**
         * This Overrides AsyncTask.doInBackGround()
         *
         * It pulls the search results based on what the droids want you to think.
         *
         * Basically it switches between searching Chicken and Lasagna
         *
         * @param @See AsyncTask.doInBackground()
         * @return @See AsyncTask.doInBackground()
         */
        @Override
        protected String doInBackground(String... strings) {


            String jsonUrl;

            if(chicken) {
                jsonUrl = "http://torunski.ca/FinalProjectChickenBreast.json";
            } else {
                jsonUrl = "http://torunski.ca/FinalProjectLasagna.json";
            }

            dbHelper.dropTable(db, TABLE_NAME);
            dbHelper.createTable(db, TABLE_NAME);


            try {       // Connect to the server:

                URL url = new URL(jsonUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();


                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                JSONObject jsonObj = new JSONObject(sb.toString());

                JSONArray recipes = jsonObj.getJSONArray("recipes");

                for (int j = 0; j <recipes.length(); j++){

                    publishProgress(j*(100/recipes.length()));

                    JSONObject r = recipes.getJSONObject(j);
                    String title = r.getString("title");
                    String publisherURL = r.getString("publisher_url");
                    String recipeID = r.getString("source_url");
                    String imageURL = r.getString("image_url");

                    dbHelper.insertItem(TABLE_NAME, title, publisherURL, recipeID, imageURL);

                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(chicken) {
                chicken = false;
            } else {
                chicken = true;
            }
            return null;
        }


        /**This method Overrides the super class's onPostExecute.
         * It calls the super method and turns back on the visibility for the search button and text
         * It saves the value of the boolean that keeps track of what was last searched sends us back to our listView of the results
         *
         * @param results  @See AsyncTask.onPostExecute()
         */
        @Override                   //Type 3 of Inner Created Class
        protected void onPostExecute(String results) {
            super.onPostExecute(results);

            searchBtn.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();

            editor.putBoolean("chicken", chicken);
            editor.commit();

            Intent intent = new Intent(RecipeAsync.this, RecipeSearch.class);
            startActivityForResult(intent, 30);

        }

        /**This method takes the param values to update our progress bar.
         * it keeps the search button and text field out of view while the progress bar is used.
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer ... values) {
            super.onProgressUpdate(values);
            editText.setVisibility(View.INVISIBLE);
            searchBtn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);

        }
    }


}