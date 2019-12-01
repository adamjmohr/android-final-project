package com.example.androidlabs.currency;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CurrencyDatabaseOpener extends SQLiteOpenHelper {

    /**
     * Name of the database.
     */
    public static final String DATABASE_NAME = "CurrencyFavourites";
    /**
     * Version of the current database. Change it to upgrade or downgrade database and start a new database.
     */
    public static final int VERSION_NUM = 1;
    /**
     * Name of the table within database.
     */
    public static final String TABLE_NAME = "currency_favourites";
    /**
     * Column for database ID's.
     */
    public static final String COL_ID = "_id";
    /**
     * Column for base currency to be used in conversion.
     */
    public static final String COL_CURRENCY_FROM = "CURRENCY_FROM";
    /**
     * Column for target currency after conversion.
     */
    public static final String COL_CURRENCY_TO = "CURRENCY_TO";


    /**
     * Initialize database with name and version. If version number changes, this will get called again.
     *
     * @param ctx used in super constructor.
     */
    public CurrencyDatabaseOpener(Activity ctx) {
        //The factory parameter should be null, unless you know a lot about Database Memory management
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * Create new table with specified columns.
     *
     * @param db database to be created.
     */
    public void onCreate(SQLiteDatabase db) {
        //Make sure you put spaces between SQL statements and Java strings:
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_CURRENCY_FROM + " TEXT, " + COL_CURRENCY_TO + " TEXT)");
    }

    /**
     * Gets called when version number is higher than current version number.
     *
     * @param db         new database object.
     * @param oldVersion lower version number.
     * @param newVersion higher version number.
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:" + newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    /**
     * Gets called when version number is lower than current version number.
     *
     * @param db         new database object.
     * @param oldVersion higher version number.
     * @param newVersion lower version number.
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:" + newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }
}
