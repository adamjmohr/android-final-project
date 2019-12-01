package com.example.androidlabs.Recipe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.androidlabs.R;
import com.google.android.material.snackbar.Snackbar;

public class RecipeMain extends AppCompatActivity {

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_main_activty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);



        String chickOrLasgne;
        Boolean  bool = false;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        pref.getBoolean("chicken", bool);

        if(bool) {
            chickOrLasgne = "Your last choice was to look for chicken recipes";
        } else {
            chickOrLasgne = "Your last choice was to look for lasagna recipes";
        }
        CoordinatorLayout cLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        Snackbar.make(cLayout, chickOrLasgne, Snackbar.LENGTH_INDEFINITE)
                .setAction("Gotcha", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Respond to the click, or not it's really up to you
                    }
                }).show();
        Button button = findViewById(R.id.recipeMainButton);

        button.setOnClickListener(click ->
        {
            //show a notification: first parameter is any view on screen. second parameter is the text. Third parameter is the length (SHORT/LONG)
            Intent nextActivity = new Intent(RecipeMain.this, RecipeSearch.class);
            startActivityForResult(nextActivity,346); //make the transition
        });




    }
}
