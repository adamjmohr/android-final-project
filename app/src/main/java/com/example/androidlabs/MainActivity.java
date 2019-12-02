package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.example.androidlabs.Recipe.RecipeMain;
import com.example.androidlabs.currency.CurrencyMain;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_toolbar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //what to do when the menu item is selected:
            case R.id.app1:
                Intent currency = new Intent(MainActivity.this, CurrencyMain.class);
                startActivity(currency);
                break;

            case R.id.app2:
                Intent electricCar = new Intent(MainActivity.this, ElectricCarFinder.class);
                startActivity(electricCar);
                break;

            case R.id.app3:
                Intent recipes = new Intent(MainActivity.this, RecipeMain.class);
                startActivity(recipes);
                break;

            case R.id.app4:
                Intent newsHeadlines = new Intent(MainActivity.this, NewsHeadlines.class);
                startActivity(newsHeadlines);
                break;
        }
        return true;
    }

}