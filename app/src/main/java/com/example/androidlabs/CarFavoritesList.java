package com.example.androidlabs;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CarFavoritesList extends AppCompatActivity {

    private ArrayList<CarSearchObject> favorites;
    private SQLiteDatabase db;
    private BaseAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_favorites);

        favorites = new ArrayList<>();

        CarDatabaseHelper dbOpener = new CarDatabaseHelper(this);
        db = dbOpener.getWritableDatabase();

        ListView theList = findViewById(R.id.carFavList);
        theList.setAdapter( myAdapter = new CarFavListAdapter() );

        String[] columns = {CarDatabaseHelper.COL_ID, CarDatabaseHelper.COL_LAT, CarDatabaseHelper.COL_LON, CarDatabaseHelper.COL_PHONENUM, CarDatabaseHelper.COL_TITLE};
        Cursor results = db.query(false, CarDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        //col index
        int latColumnIndex = results.getColumnIndex(CarDatabaseHelper.COL_LAT);
        int lonColIndex = results.getColumnIndex(CarDatabaseHelper.COL_LON);
        int titleColumnIndex = results.getColumnIndex(CarDatabaseHelper.COL_TITLE);
        int phoneColIndex = results.getColumnIndex(CarDatabaseHelper.COL_PHONENUM);
        int idColIndex = results.getColumnIndex(CarDatabaseHelper.COL_ID);

        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {

            String lat = results.getString(latColumnIndex);
            String lon = results.getString(lonColIndex);
            String title = results.getString(titleColumnIndex);
            String phone = results.getString(phoneColIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            favorites.add(new CarSearchObject(title, lat, lon, phone, id));
        }
        myAdapter.notifyDataSetChanged();

        ImageButton backButton = findViewById(R.id.carFavBackButton);
        backButton.setOnClickListener( clk ->
        {
            Intent backPage = new Intent(CarFavoritesList.this, ElectricCarFinder.class);
            startActivity(backPage);

        });
    }

    private class CarFavListAdapter extends BaseAdapter {

        public int getCount() {
            return favorites.size();  } //This function tells how many objects to show

        public CarSearchObject getItem(int position) {
            return favorites.get(position);  }  //This returns the string at position p

        public long getItemId(int p) {
            return p; } //This returns the database id of the item at position p

        public View getView(int p, View recycled, ViewGroup parent)
        {

            View thisRow = getLayoutInflater().inflate(R.layout.car_listview_layout, null);
            TextView titleText = thisRow.findViewById(R.id.carListTitle );
            titleText.setText(getItem(p).getTitle() );

            TextView latText = thisRow.findViewById(R.id.carListLat );
            latText.setText("Lat: " + getItem(p).getLat() );

            TextView longText = thisRow.findViewById(R.id.carListLong );
            longText.setText("Lon: " + getItem(p).getLon() );


            return thisRow;
        }
    }
}
