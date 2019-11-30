package com.example.androidlabs;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Handles displaying and removing items from favorites database
 * can start navigating to favorites items
 */
public class CarFavoritesList extends AppCompatActivity {

    /**
     * ArrayList containing all favorite car charging stations
     */
    private ArrayList<CarSearchObject> favorites;
    /**
     * db helper
     */
    private SQLiteDatabase db;
    /**
     * Adapter to help display list view
     */
    private BaseAdapter myAdapter;

    /**
     * Populates and display favorites in list view
     * Handles selection of a favorite from listview or back button
     */
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

        while (results.moveToNext()) {

            String lat = results.getString(latColumnIndex);
            String lon = results.getString(lonColIndex);
            String title = results.getString(titleColumnIndex);
            String phone = results.getString(phoneColIndex);
            long id = results.getLong(idColIndex);


            favorites.add(new CarSearchObject(title, lat, lon, phone, id));
        }
        myAdapter.notifyDataSetChanged();

        ImageButton backButton = findViewById(R.id.carFavBackButton);
        backButton.setOnClickListener( clk ->
        {
            Intent backPage = new Intent(CarFavoritesList.this, ElectricCarFinder.class);
            startActivity(backPage);

        });

        theList.setOnItemClickListener( (list, item, position, id) -> {

            Bundle dataToPass = new Bundle();
            dataToPass.putString(CarDatabaseHelper.COL_LAT, favorites.get(position).getLat());
            dataToPass.putString(CarDatabaseHelper.COL_LON, favorites.get(position).getLon());
            dataToPass.putString(CarDatabaseHelper.COL_TITLE, favorites.get(position).getTitle());
            dataToPass.putString(CarDatabaseHelper.COL_PHONENUM, favorites.get(position).getTelephone());
            dataToPass.putInt(ElectricCarFinder.ITEM_POSITION, position);
            dataToPass.putLong(CarDatabaseHelper.COL_ID, id);

            boolean isTablet = findViewById(R.id.carFragmentLocation) != null;

            if(isTablet)
            {
                CarFavDetailFragment dFragment = new CarFavDetailFragment();
                dFragment.setArguments( dataToPass );
                dFragment.setTablet(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.carFavFragmentLocation, dFragment)
                        .addToBackStack("AnyName")
                        .commit();
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(CarFavoritesList.this, CarFavEmpty.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, ElectricCarFinder.EMPTY_ACTIVITY); //make the transition

            }
        });
    }

    /**
     * Method to check if the results were successful from the empty activity.
     * If proper codes are given take the id from the data and remove from favorites
     *
     * @param requestCode int to check if the result came from the empty activity
     * @param resultCode int to check if the activity finished successfully
     * @param data data from activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ElectricCarFinder.EMPTY_ACTIVITY)
        {
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getIntExtra(CarDatabaseHelper.COL_ID, 0);
                int pos = data.getIntExtra(ElectricCarFinder.ITEM_POSITION, 0);
                removeFromFavorite((int)id, pos);
            }
        }
    }

    /**
     * Method that take an id of the item and then removes that item from the favorites database
     * @param id id of the item being removed from favorites
     */
    public void removeFromFavorite(int id, int pos) {
        db.delete(CarDatabaseHelper.TABLE_NAME, CarDatabaseHelper.COL_ID + "=" + id, null);
        favorites.remove(pos);
        Toast.makeText(this, "Removed From Favorite", Toast.LENGTH_LONG).show();
        myAdapter.notifyDataSetChanged();

    }

    /**
     * Adapter to help display listview
     */
    private class CarFavListAdapter extends BaseAdapter {

        /**
         *size getter
         * @return size of listview
         */
        public int getCount() {
            return favorites.size();  }

        /**
         *
         * @param position position of the requested object
         * @return object at position
         */
        public CarSearchObject getItem(int position) {
            return favorites.get(position);  }

        /**
         *
         * @param p position of object
         * @return id of the requested object
         */
        public long getItemId(int p) {
            return p; }

        /**
         * inflates and returns view for display in listview
         *
         * @param p position of object
         * @param recycled old view to be recycled
         * @param parent
         * @return inflated vied for listview
         */
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
