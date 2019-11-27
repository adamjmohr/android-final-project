package com.example.androidlabs;

//This is for my Recipe Activity

/* this is what we will pull in a query row

{
"publisher": "BBC Good Food",
"f2f_url": "http://food2fork.com/view/495802",
"title": "Chicken cacciatore",
"source_url": "http://www.bbcgoodfood.com/recipes/4251/chicken-cacciatore",
"recipe_id": "495802",
"image_url": "http://static.food2fork.com/4251_MEDIUM71f0.jpg",
"social_rank": 99.99999994031722,
"publisher_url": "http://www.bbcgoodfood.com"
}
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDatabaseHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "RecipeDB";
    private static int VERSION_NUM = 1;
    private static String TABLE_NAME = "myTableDuhhh";
    private static String PK_ID = "_id";
    private static String COL_TITLE = "title";
    private static String COL_CONTENT_URL = "publisher";
    private static String COL_RECIPE_ID = "recipe_id";
    private static String COL_IMAGE_URL = "image_url";

    public RecipeDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION_NUM);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
            + PK_ID +" INTERGER PRIMARY KEY AUTOINCREMENT,"
            + COL_TITLE + " TEXT, "
            + COL_RECIPE_ID + " TEXT, "
            + COL_IMAGE_URL + "TEXT, "
            + COL_CONTENT_URL + " TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        VERSION_NUM++;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
