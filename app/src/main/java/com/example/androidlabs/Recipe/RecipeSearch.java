package com.example.androidlabs.Recipe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidlabs.R;
import com.google.android.material.snackbar.Snackbar;

import static com.example.androidlabs.Recipe.RecipeDatabaseHelper.*;

public class RecipeSearch extends AppCompatActivity {

    private static SQLiteDatabase db = null;
    private static Cursor cursor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);



        Button search = findViewById(R.id.recipeSearchButton);

        //Set up view

        ListView list = findViewById(R.id.recipeListView);

        //initiate a database
        RecipeDatabaseHelper opener = new RecipeDatabaseHelper(this);
        try {
            db = opener.getWritableDatabase();
            cursor = opener.getCursor("resultTable");

            SimpleCursorAdapter chatAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_2,
                    cursor,
                    new String[]{PK_ID, COL_TITLE, COL_IMAGE, COL_RECIPE_ID},
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0);
            list.setAdapter(chatAdapter);
            cursor.requery();
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this,
                    "Database unavailable",
                    Toast.LENGTH_SHORT);
            toast.show();
        }





        //This is the onClickListener for my List
       list.setOnItemClickListener( (mlist, item, position, id) -> {

           cursor.moveToPosition(position);

           Bundle mBundle = new Bundle();
           mBundle.putString(PK_ID, cursor.getString( cursor.getColumnIndex(PK_ID)) );
           mBundle.putString(COL_TITLE, cursor.getString( cursor.getColumnIndex(COL_TITLE)));
           mBundle.putString(COL_IMAGE, cursor.getString( cursor.getColumnIndex(COL_IMAGE)));
           mBundle.putString(COL_CONTENT_URL, cursor.getString( cursor.getColumnIndex(COL_CONTENT_URL)));
           mBundle.putString(COL_RECIPE_ID, cursor.getString( cursor.getColumnIndex(COL_RECIPE_ID)));
           mBundle.putInt("position", position);

            boolean isTablet = findViewById(R.id.recipeFragmentLocation) != null;
            if(isTablet)
            {
                RecipeDetailFragment fragment = new RecipeDetailFragment();
                fragment.setArguments( mBundle );
                fragment.setTablet(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.recipeFragmentLocation, fragment)
                        .commit();
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(RecipeSearch.this, RecipeEmptyActivity.class);
                nextActivity.putExtras(mBundle); //send data to next activity
                startActivityForResult(nextActivity,346); //make the transition

            }
        });



        search.setOnClickListener(click ->
        {
            //show a notification: first parameter is any view on screen. second parameter is the text. Third parameter is the length (SHORT/LONG)
            Snackbar.make(search, "Searching online for Chicken. That is what you typed right?", Snackbar.LENGTH_LONG).show();
            Intent nextActivity = new Intent(RecipeSearch.this, RecipeAsync.class);
            startActivityForResult(nextActivity,346); //make the transition
            list.deferNotifyDataSetChanged();


        });

        /*
        receiveButton.setOnClickListener(click ->
        {
            //* get the text that were typed
            String text = chatText.getText().toString();

            //add to the database and get the new ID
            long newId = opener.insertData(db, text, "receive");

            //clear the EditText fields and update list
            chatText.setText("");
            ;

            //show a notification: first parameter is any view on screen. second parameter is the text. Third parameter is the length (SHORT/LONG)
            Snackbar.make(sendButton, "Inserted item id:" + newId, Snackbar.LENGTH_LONG).show();
        });*/


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();

    }
}
