package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.androidlabs.currency.CurrencyMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ElectricCarFinder extends AppCompatActivity {

    private ArrayList<CarSearchObject> search;
    private ProgressBar progressBar;
    private Button searchButton;
    private BaseAdapter myAdapter;
    private SQLiteDatabase db;
    public static final String ITEM_POSITION = "POSITION";
    public static final int EMPTY_ACTIVITY = 345;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_main);

        CarDatabaseHelper dbOpener = new CarDatabaseHelper(this);
        db = dbOpener.getWritableDatabase();

        Toolbar toolbar = findViewById(R.id.carToolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences("carPrefs", Context.MODE_PRIVATE);
        String strLat = prefs.getString("Latitude", "");
        String strLong= prefs.getString("Longitude", "");

        search = new ArrayList<>();

        ListView theList = findViewById(R.id.carListSearch);
        theList.setAdapter( myAdapter = new MyListAdapter() );

        progressBar = findViewById(R.id.carProgressCar);

        EditText editLat = findViewById(R.id.carEditLat);
        editLat.setText(strLat);

        EditText editLong= findViewById(R.id.carEditLong);
        editLong.setText(strLong);

        searchButton = findViewById(R.id.carButtonSearch);
        searchButton.setOnClickListener( clk ->
        {
            SharedPreferences.Editor edit = prefs.edit();

            edit.putString("Latitude", editLat.getText().toString());
            edit.putString("Longitude", editLong.getText().toString());
            edit.commit();

            search = new ArrayList<>();

            CarQuery query = new CarQuery(editLat.getText().toString(), editLong.getText().toString());
            query.execute();

        });

        theList.setOnItemClickListener( (list, item, position, id) -> {

            Bundle dataToPass = new Bundle();
            dataToPass.putString(CarDatabaseHelper.COL_LAT, search.get(position).getLat());
            dataToPass.putString(CarDatabaseHelper.COL_LON, search.get(position).getLon());
            dataToPass.putString(CarDatabaseHelper.COL_TITLE, search.get(position).getTitle());
            dataToPass.putString(CarDatabaseHelper.COL_PHONENUM, search.get(position).getTelephone());
            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putLong(CarDatabaseHelper.COL_ID, position);

            boolean isTablet = findViewById(R.id.carFragmentLocation) != null;

            if(isTablet)
            {
                CarSearchDetailFragment dFragment = new CarSearchDetailFragment();
                dFragment.setArguments( dataToPass );
                dFragment.setTablet(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.carFragmentLocation, dFragment)
                        .addToBackStack("AnyName")
                        .commit();
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(ElectricCarFinder.this, CarEmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition

            }
        });


        

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EMPTY_ACTIVITY)
        {
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getIntExtra(CarDatabaseHelper.COL_ID, 0);
                addToFavorite((int)id);
            }
        }
    }

    public void addToFavorite(int id){
        CarSearchObject addObj = search.get(id);

        ContentValues newRowValues = new ContentValues();

        newRowValues.put(CarDatabaseHelper.COL_PHONENUM, addObj.getTelephone());
        newRowValues.put(CarDatabaseHelper.COL_LON, addObj.getLon());
        newRowValues.put(CarDatabaseHelper.COL_LAT, addObj.getLat());
        newRowValues.put(CarDatabaseHelper.COL_TITLE, addObj.getTitle());

        long newId = db.insert(CarDatabaseHelper.TABLE_NAME, null, newRowValues);
        if(newId > 0 ){
            Toast.makeText(this, "Added to Favorite", Toast.LENGTH_LONG).show();
        }

    }

    private class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return search.size();  } //This function tells how many objects to show

        public CarSearchObject getItem(int position) {
            return search.get(position);  }  //This returns the string at position p

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


    class CarQuery extends AsyncTask<String, Integer, String>{

        private String title;
        private String lat;
        private String lon;
        private String telephone;


        public CarQuery(String lat, String lon){
            this.lat = lat;
            this.lon = lon;
        }

        @Override
        protected String doInBackground(String... strings) {
            String ret = null;
            String queryURL = "https://api.openchargemap.io/v3/poi/?output=json&countrycode=CA&latitude="+lat+"&longitude="+lon+"&maxresults=10";
            CarSearchObject tempObj;

            try {
                publishProgress(0);

                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                //JSONObject jObject = new JSONObject(result);

                //JSONArray jArray = jObject.getJSONArray("carChargingStations");
                JSONArray jArray = new JSONArray(result);
                for(int i = 0; i < jArray.length(); i++){
                    publishProgress(i*10);
                    JSONObject object = jArray.getJSONObject(i);

                    JSONObject operatorInfo;
                    try {
                        operatorInfo = object.getJSONObject("OperatorInfo");
                    }
                    catch (JSONException e){
                        operatorInfo = null;
                    }

                    if(operatorInfo == null){
                        title = "No operator information";
                    }
                    else {
                        title = operatorInfo.getString("Title");
                    }

                    JSONObject addressInfo = object.getJSONObject("AddressInfo");

                    lat = addressInfo.getDouble("Latitude") + "";
                    lon = addressInfo.getDouble("Longitude")+"";
                    telephone = addressInfo.getString("ContactTelephone1");

                    tempObj = new CarSearchObject(title, lat, lon, telephone);
                    search.add(tempObj);



                }

                publishProgress(100);


            }
            catch(MalformedURLException mfe){ ret = "Malformed URL exception"; }
            catch(IOException ioe)          { ret = "IO Exception. Is the Wifi connected?";}
            catch(JSONException e)          { ret = "Broken JSON";
                Log.e("Broken Json", e.getMessage()); }

            return ret;
        }

        @Override
        protected void onProgressUpdate(Integer ... values) {
            super.onProgressUpdate(values);
            searchButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);
            searchButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.car_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.carHelp:
                View middle = getLayoutInflater().inflate(R.layout.car_dialog, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .setView(middle);

                builder.create().show();
                break;

            case R.id.carFav:
                Intent favPage = new Intent(ElectricCarFinder.this, CarFavoritesList.class);
                startActivity(favPage);
                break;

            case R.id.carHome:
                Intent homePage = new Intent(ElectricCarFinder.this, MainActivity.class);
                startActivity(homePage);
                break;

        }
        return true;
    }
}
