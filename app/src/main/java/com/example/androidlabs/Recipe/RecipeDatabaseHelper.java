package com.example.androidlabs.Recipe;

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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.security.PublicKey;

public class RecipeDatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "RecipeDB04";
    public static  int VERSION_NUM = 1;
    public static final String RESULTS_TABLE = "resultTable";
    public static final String FAVORITE_TABLE = "faveTable";
    public static final String PK_ID = "_id";
    public static final String COL_TITLE = "title";
    public static final String COL_CONTENT_URL = "publisher";
    public static final String COL_RECIPE_ID = "recipe_id";
    public static final String COL_IMAGE = "image";

    public RecipeDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION_NUM);
    }

    public void onCreate(SQLiteDatabase db) {

        createTable(db, RESULTS_TABLE);
        createTable(db, FAVORITE_TABLE);
        /* Replaced
        db.execSQL("CREATE TABLE " + RESULTS_TABLE + "( "
            + PK_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_TITLE + " TEXT, "
            + COL_RECIPE_ID + " TEXT, "
            + COL_IMAGE_URL + " TEXT, "
            + COL_CONTENT_URL + " TEXT)");

        db.execSQL("CREATE TABLE " + FAVORITE_TABLE + "( "
                + PK_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " TEXT, "
                + COL_RECIPE_ID + " TEXT, "
                + COL_IMAGE_URL + " TEXT, "
                + COL_CONTENT_URL + " TEXT)");*/
    }

    public void onOpen(SQLiteDatabase db) {

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        VERSION_NUM++;
        db.execSQL("DROP TABLE IF EXISTS " + RESULTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FAVORITE_TABLE);
        onCreate(db);
    }

    public static long insertItem(SQLiteDatabase db, String tableName, String title, String publisher, String recipeId, String image) {
        ContentValues content = new ContentValues();
        content.put(COL_TITLE, title);
        content.put(COL_CONTENT_URL, publisher);
        content.put(COL_RECIPE_ID, recipeId);
        content.put(COL_IMAGE, image);
        return db.insertWithOnConflict(tableName, null, content, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public static int deleteItem(SQLiteDatabase db, String key, String tableName) {
        return db.delete(tableName, PK_ID +" = ?", new String[] {key});
    }


    public static void dropTable(SQLiteDatabase db, String table) {
        try {
            db.execSQL("DROP TABLE " + table);
        } catch (SQLiteException e){
             e.printStackTrace();
        }
    }

    public static void createTable(SQLiteDatabase db, String table) {
        db.execSQL("CREATE TABLE " + table + "( "
                + PK_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " TEXT UNIQUE, "
                + COL_RECIPE_ID + " TEXT, "
                + COL_IMAGE + " TEXT, "
                + COL_CONTENT_URL + " TEXT)");
    }

    public Cursor getCursor(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(tableName, new String[]{"*"}, null, null, null, null, null);
    }

}
