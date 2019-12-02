package com.example.androidlabs.Recipe;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidlabs.R;

/**
 * Activity to hold our fragments, extends AppCompatActivity
 */
public class RecipeEmptyActivity extends AppCompatActivity {

    /**onCreate for RecipeEmptyActivity to hold our fragment
     * @param savedInstanceState  @see AppCompatActivity.onCreate()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_empty);

        Bundle dataToPass = getIntent().getExtras();

        RecipeDetailFragment dFragment = new RecipeDetailFragment();
        dFragment.setArguments(dataToPass); //pass data to the the fragment
        dFragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.recipeFragmentLocation, dFragment)
                .commit();


    }
}
