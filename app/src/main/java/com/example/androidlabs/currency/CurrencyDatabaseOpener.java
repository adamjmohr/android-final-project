package com.example.androidlabs.currency;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CurrencyDatabaseOpener extends SQLiteOpenHelper {

    /**
     *
     */
    public static final String DATABASE_NAME = "CurrencyFavourites";
    /**
     *
     */
    public static final int VERSION_NUM = 2;
    /**
     *
     */
    public static final String TABLE_NAME = "currency_favourites";
    /**
     *
     */
    public static final String COL_ID = "_id";
    /**
     *
     */
    public static final String COL_CURRENCY_FROM = "CURRENCY_FROM";
    /**
     *
     */
    public static final String COL_CURRENCY_TO = "CURRENCY_TO";


    /**
     * @param ctx
     */
    public CurrencyDatabaseOpener(Activity ctx) {
        //The factory parameter should be null, unless you know a lot about Database Memory management
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * @param db
     */
    public void onCreate(SQLiteDatabase db) {
        //Make sure you put spaces between SQL statements and Java strings:
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_CURRENCY_FROM + " TEXT, " + COL_CURRENCY_TO + " TEXT)");
    }

    /**
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:" + newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    /**
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:" + newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }
}
