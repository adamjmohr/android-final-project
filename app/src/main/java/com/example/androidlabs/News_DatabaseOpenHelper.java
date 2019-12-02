package com.example.androidlabs;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * database opener from the inclassExamples2019F demos
 *  @author Kaikai Mao
 *  @since 11/25/2019
 */
public class News_DatabaseOpenHelper extends SQLiteOpenHelper {

    /**
     * database name
     */
    public static final String DATABASE_NAME = "news";
    /**
     * version of a database
     */
    public static final int VERSION_NUM = 1;
    /**
     * table name
     */
    public static final String TABLE_NAME = "NewsArticles";
    /**
     * column name in a table
     */
    public static final String COL_ID = "_id";
    /**
     * column name in a table
     */
    public static final String COL_TITLE = "TITLE";
    /**
     * column name in a table
     */
    public static final String COL_DESCRIPTION = "DESCRIPTION";
    /**
     * column name in a table
     */
    public static final String COL_ARTICLEURL = "ARTICLEURL";
    /**
     * column name in a table
     */
    public static final String COL_IMAGEURL = "IMAGEURL";

    public News_DatabaseOpenHelper(Activity ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " TEXT, " + COL_DESCRIPTION + " TEXT, "
                + COL_ARTICLEURL + " TEXT, " + COL_IMAGEURL + " TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    /**
     * Method downgrades a database
     * @param db database
     * @param oldVersion old version number
     * @param newVersion new version number
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
