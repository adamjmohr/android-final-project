package com.example.androidlabs.Recipe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.androidlabs.R;
import com.google.android.material.snackbar.Snackbar;

import static com.example.androidlabs.Recipe.RecipeDatabaseHelper.*;

/**
 * This class does shows the listviews and allows for searching.
 * It extends AppCompatActivity
 */
public class RecipeSearch extends AppCompatActivity {

    public static Boolean setTable = true;
    private static SQLiteDatabase db = null;
    private static Cursor cursor;
    private static Menu menu = null;
    private static ListView list;
    private static RecipeDatabaseHelper opener;
    private static SimpleCursorAdapter chatAdapter;


    /**This Overrides the superclass's onCreate method,
     * It sets up the tool bar and button as well as selects the right Table to show in the list view.
     * It sets up the Click Listener for the listview and the search button.
     * @param savedInstanceState @See AppCompatActivity.onCreate()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.recipeToolbar);
        setSupportActionBar(toolbar);


        Button search = findViewById(R.id.recipeSearchButton);
        EditText editText = findViewById(R.id.searchEditText);
        list = findViewById(R.id.recipeListView);
        opener = new RecipeDatabaseHelper(this);


        //Set up views
        if (setTable) {
            setResults();
        } else {
            setFave();
        }

        //This is the onClickListener for my List
        list.setOnItemClickListener((mlist, item, position, id) -> {

            cursor.moveToPosition(position);

            Bundle mBundle = new Bundle();
            mBundle.putString(PK_ID, cursor.getString(cursor.getColumnIndex(PK_ID)));
            mBundle.putString(COL_TITLE, cursor.getString(cursor.getColumnIndex(COL_TITLE)));
            mBundle.putString(COL_IMAGE, cursor.getString(cursor.getColumnIndex(COL_IMAGE)));
            mBundle.putString(PUBLISHER, cursor.getString(cursor.getColumnIndex(PUBLISHER)));
            mBundle.putString(COL_WEBSITE, cursor.getString(cursor.getColumnIndex(COL_WEBSITE)));
            mBundle.putInt("position", position);

            boolean isTablet = findViewById(R.id.recipeFragmentLocation) != null;
            if (isTablet) {
                RecipeDetailFragment fragment = new RecipeDetailFragment();
                fragment.setArguments(mBundle);
                fragment.setTablet(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.recipeFragmentLocation, fragment)
                        .commit();
            } else //isPhone
            {
                Intent nextActivity = new Intent(RecipeSearch.this, RecipeEmptyActivity.class);
                nextActivity.putExtras(mBundle); //send data to next activity
                startActivityForResult(nextActivity, 346); //make the transition

            }
        });


        search.setOnClickListener(click ->
        {
            //show a notification: first parameter is any view on screen. second parameter is the text. Third parameter is the length (SHORT/LONG)
            Snackbar.make(search, "Searching online for Chicken. That is what you typed right?", Snackbar.LENGTH_LONG).show();
            Intent nextActivity = new Intent(RecipeSearch.this, RecipeAsync.class);
            startActivityForResult(nextActivity, 346); //make the transition
            list.deferNotifyDataSetChanged();
            search.setVisibility(View.INVISIBLE);
            editText.setVisibility(View.INVISIBLE);
        });

    }

    /**This method Overrides the superclass's onCreateOptionsMenu() method
     * It sets up the toolbar and sets the toggleling icon based on the current list showing
     * @param menu @see AppCompatActivity.onCreateOptionsMenu()
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the app bar.
        getMenuInflater().inflate(R.menu.recipe_menu, menu);

        if (!setTable) {
            menu.getItem(0).setIcon(R.drawable.search);
        }

        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    /**This sets actions for what will happen when items in the Toolbar are clicked
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.recipeHelp:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.information))
                        .setMessage(getString(R.string.recipeVersion) + "\n" + getString(R.string.recipeSearchHelp))
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case R.id.recipeFav:

                if (setTable) {
                    setFave();
                    menu.getItem(0).setIcon(R.drawable.search);

                } else {
                    setResults();
                    menu.getItem(0).setIcon(R.drawable.star_unfilled);
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }

    /**
     * This method is to load the Favorite table into the list view
     */
    private void setFave() {
        setTable = false;

        try {
            db = opener.getWritableDatabase();
            cursor = opener.getCursor("faveTable");

            chatAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_2,
                    cursor,
                    new String[]{COL_TITLE, PUBLISHER, COL_IMAGE, PK_ID},
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0);
            list.setAdapter(chatAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this,
                    "Database unavailable",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
        findViewById(R.id.searchEditText).setVisibility(View.INVISIBLE);
        findViewById(R.id.recipeSearchButton).setVisibility(View.INVISIBLE);
        list.deferNotifyDataSetChanged();
    }

    /**
     * This method is to load the Search Results Table into the List
     */
    private void setResults() {
        setTable = true;

        try {
            db = opener.getWritableDatabase();
            cursor = opener.getCursor("resultTable");

            chatAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_2,
                    cursor,
                    new String[]{COL_TITLE, PK_ID, COL_IMAGE, COL_WEBSITE},
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0);
            list.setAdapter(chatAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this,
                    "Database unavailable",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
        findViewById(R.id.searchEditText).setVisibility(View.VISIBLE);
        findViewById(R.id.recipeSearchButton).setVisibility(View.VISIBLE);
        list.deferNotifyDataSetChanged();
    }

    /**
     * This method ensures that the right Table is shown in the list. And that it is updated if its
     * the Favorites Table. It needs to recheck the database for new entries or in case one was deleted.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (setTable) {
            setResults();
        } else {
            setFave();
        }
    }
}
