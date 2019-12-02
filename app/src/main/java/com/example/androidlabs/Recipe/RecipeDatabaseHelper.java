package com.example.androidlabs.Recipe;

//This is for my Recipe Activity

/* this is what we will pull in a query row

{
"publisher": "BBC Good Food",
"f2f_url": "http://food2fork.com/view/495802",
"title": "Chicken cacciatore",
"source_url": "http://www.bbcgoodfood.com/recipes/4251/chicken-cacciatore",
"website": "495802",
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

/**This is my DatabaseHelper class for the Recipe Portion
 *
 */
public class RecipeDatabaseHelper extends SQLiteOpenHelper {


    public static SQLiteDatabase db = null;
    public static final String DB_NAME = "RecipeDB04";
    public static  int VERSION_NUM = 1;
    public static final String RESULTS_TABLE = "resultTable";
    public static final String FAVORITE_TABLE = "faveTable";
    public static final String PK_ID = "_id";
    public static final String COL_TITLE = "title";
    public static final String PUBLISHER = "publisher";
    public static final String COL_WEBSITE = "source_url";
    public static final String COL_IMAGE = "image";

    public RecipeDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION_NUM);
    }

    public void onCreate(SQLiteDatabase db) {

        this.db = db;
        createTable(db, RESULTS_TABLE);
        createTable(db, FAVORITE_TABLE);
        /* Replaced
        db.execSQL("CREATE TABLE " + RESULTS_TABLE + "( "
            + PK_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_TITLE + " TEXT, "
            + COL_WEBSITE + " TEXT, "
            + COL_IMAGE_URL + " TEXT, "
            + PUBLISHER + " TEXT)");

        db.execSQL("CREATE TABLE " + FAVORITE_TABLE + "( "
                + PK_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " TEXT, "
                + COL_WEBSITE + " TEXT, "
                + COL_IMAGE_URL + " TEXT, "
                + PUBLISHER + " TEXT)");*/
    }

    /**This just makes sure that the local db variable always refers to the most current database.
     * @param db
     */
    public void onOpen(SQLiteDatabase db) {
        this.db = db;
    }

    /** This is the onUpgrade, don't really use it. Only ever one version, will update if neeeded in the future
     * @param db SQLite Database
     * @param oldVersion Old version number
     * @param newVersion New version number
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        VERSION_NUM++;
        db.execSQL("DROP TABLE IF EXISTS " + RESULTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FAVORITE_TABLE);
        onCreate(db);
    }

    /**This method is used to insert values into one of the two tables
     * it uses an insertWithOnConflict() method with a CONFLICT_IGNORE flag to discard
     * failures to insert due to the Unique constraint on our title column
     * @param tableName This is the TableName
     * @param title This is the value for the title column
     * @param publisher This is the value for the publisher column
     * @param contentUrl This is the value for the source_url column
     * @param image This is the value for the image column
     * @return returns boolean true if successful or false if not.
     */
    public static long insertItem(String tableName, String title, String publisher, String contentUrl, String image) {
        ContentValues content = new ContentValues();
        content.put(COL_TITLE, title);
        content.put(PUBLISHER, publisher);
        content.put(COL_WEBSITE, contentUrl);
        content.put(COL_IMAGE, image);
        return db.insertWithOnConflict(tableName, null, content, SQLiteDatabase.CONFLICT_IGNORE);
    }


    /** This is my delete method for remocing specific Items/Rows from one of the Tables
     * @param title We will always use the title column value to delete since it is set to unique.
     * @param tableName This is the table name we should delete from
     * @return returns int which I believe represents the id of the row.
     */
    public static int deleteItem(String title, String tableName) {
        return db.delete(tableName, COL_TITLE + " = ?", new String[] {title});
    }


    /**This is used to drop our Tables. Mostly for a quick clean.
     * @param db This is the db we will drop from
     * @param table This is the table name we will drop
     */
    public static void dropTable(SQLiteDatabase db, String table) {
        try {
            db.execSQL("DROP TABLE " + table);
        } catch (SQLiteException e){
             e.printStackTrace();
        }
    }

    /**Used to Create tables, we need two and they will use the same columns, best practice would have fave table use
     * pk to fk relationship instead of duplicating data.
     * @param db  Database to add table to
     * @param table table name we will add
     */
    public static void createTable(SQLiteDatabase db, String table) {
        db.execSQL("CREATE TABLE " + table + "( "
                + PK_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " TEXT UNIQUE, "
                + COL_WEBSITE + " TEXT, "
                + COL_IMAGE + " TEXT, "
                + PUBLISHER + " TEXT)");
    }

    /**This method gets a Cursor object to a specified Table
     * @param tableName this is a String specifing the name of the table to grab the cursor for
     * @return the return varible is a Cursor object
     */
    public Cursor getCursor(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(tableName, new String[]{"*"}, null, null, null, null, null);
    }

    /**This method checks to see if a record already exists in our database table
     * @param tableName This is a String to specify the table to look in for the record
     * @param field this is a String specifying which column to look under. Should use PK_ID or COL_TITLE if looking for unique record.
     * @param fieldValue this is a String to search records for.
     * @return
     */
    public static boolean doesRecordExist(String tableName, String field, String fieldValue) {
        String Query = "Select * from " + tableName + " where " + field + " like " + "\""+fieldValue+"\"";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

}
