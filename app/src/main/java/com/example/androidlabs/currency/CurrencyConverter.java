package com.example.androidlabs.currency;

import android.content.ContentValues;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.androidlabs.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CurrencyConverter extends AppCompatActivity {

    /**
     * Used when opening fragment on phone screen.
     */
    public static final int EMPTY_ACTIVITY = 345;

    /**
     * Amount User enters to convert into another currency.
     */
    private double amountToConvert;
    /**
     * Target currency after conversion.
     */
    private String currencyFrom, currencyTo;
    /**
     * Alert message for duplicate favourite.
     */
    private String duplicateFav = "Favourite already exists";
    /**
     * List of favourite currency conversions saved for later use by User.
     */
    private ArrayList<CurrencyObject> favourites;
    /**
     * Custom adapter for currency objects.
     */
    private CurrencyAdapter currencyAdapter;

    /**
     * Base currency.
     */
    public static final String BASE_CURRENCY = "BASE";
    /**
     * Target currency.
     */
    public static final String TARGET_CURRENCY = "TARGET";
    /**
     * Position within ListView.
     */
    public static final String ITEM_POSITION = "POSITION";
    /**
     * Database ID of currency object within database.
     */
    public static final String ITEM_ID = "ID";

    /**
     * Initialize page with widgets and set listeners on buttons and ListView.
     *
     * @param savedInstanceState called on super constructor.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_converter);

        SharedPreferences prefs = getSharedPreferences("lastSearch", Context.MODE_PRIVATE);
        currencyFrom = prefs.getString("lastSearchFrom", "");
        currencyTo = prefs.getString("lastSearchTo", "");
        amountToConvert = prefs.getFloat("amount", 0);

        favourites = new ArrayList<>();
        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        CurrencyDatabaseOpener dbOpener = new CurrencyDatabaseOpener(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

        //query results from database
        String[] columns = {CurrencyDatabaseOpener.COL_ID, CurrencyDatabaseOpener.COL_CURRENCY_FROM, CurrencyDatabaseOpener.COL_CURRENCY_TO};
        Cursor results = db.query(false, CurrencyDatabaseOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //find the column indices:
        int idColIndex = results.getColumnIndex(CurrencyDatabaseOpener.COL_ID);
        int fromColIndex = results.getColumnIndex(CurrencyDatabaseOpener.COL_CURRENCY_FROM);
        int toColIndex = results.getColumnIndex(CurrencyDatabaseOpener.COL_CURRENCY_TO);

        //iterate over the results, return true if there is a next item
        while (results.moveToNext()) {
            String currencyFrom = results.getString(fromColIndex);
            String currencyTo = results.getString(toColIndex);
            long id = results.getLong(idColIndex);

            //add new message to arrayList
            favourites.add(new CurrencyObject(id, currencyFrom, currencyTo));

        }
        results.close();

        ListView theList = findViewById(R.id.theList);
        currencyAdapter = new CurrencyAdapter();
        theList.setAdapter(currencyAdapter);
        theList.setOnItemClickListener((list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString(BASE_CURRENCY, currencyAdapter.getItem(position).getName());
            dataToPass.putString(TARGET_CURRENCY, currencyAdapter.getItem(position).getFavourite());
            dataToPass.putLong(ITEM_ID, currencyAdapter.getItemId(position));
            dataToPass.putInt(ITEM_POSITION, position);

            if (isTablet) {
                CurrencyFragment cFragment = new CurrencyFragment(); //add a DetailFragment
                cFragment.setArguments(dataToPass); //pass it a bundle for information
                cFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, cFragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            } else {//isPhone
                Intent nextActivity = new Intent(CurrencyConverter.this, CurrencyEmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText amount = findViewById(R.id.amount);
        amount.setText(String.valueOf(amountToConvert));

        Spinner spinnerFrom = findViewById(R.id.currencies_spinner_from);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterFrom = ArrayAdapter.createFromResource(this,
                R.array.currencies_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerFrom.setAdapter(adapterFrom);
        if (currencyFrom != null) {
            int spinnerPosition = adapterFrom.getPosition(currencyFrom);
            spinnerFrom.setSelection(spinnerPosition);
        }

        Spinner spinnerTo = findViewById(R.id.currencies_spinner_to);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterTo = ArrayAdapter.createFromResource(this,
                R.array.currencies_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerTo.setAdapter(adapterTo);
        if (currencyTo != null) {
            int spinnerPosition = adapterTo.getPosition(currencyTo);
            spinnerTo.setSelection(spinnerPosition);
        }

        Button runConversion = findViewById(R.id.run_conversion);
        runConversion.setOnClickListener(clk -> {
            amountToConvert = Double.parseDouble(amount.getText().toString());
            currencyFrom = spinnerFrom.getSelectedItem().toString();
            currencyTo = spinnerTo.getSelectedItem().toString();

            CurrencyQuery query = new CurrencyQuery(currencyFrom, currencyTo, amountToConvert);
            query.execute();

        });

        Button saveFavourite = findViewById(R.id.save_favourites);
        saveFavourite.setOnClickListener(clk -> {

            Toast.makeText(this, duplicateFav, Toast.LENGTH_LONG).show();

            currencyFrom = spinnerFrom.getSelectedItem().toString();
            currencyTo = spinnerTo.getSelectedItem().toString();

            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //put string name in the MESSAGE column:
            newRowValues.put(CurrencyDatabaseOpener.COL_CURRENCY_FROM, currencyFrom);
            //put string email in the SENT column:
            newRowValues.put(CurrencyDatabaseOpener.COL_CURRENCY_TO, currencyTo);
            //insert in the database:
            long newId = db.insert(CurrencyDatabaseOpener.TABLE_NAME, null, newRowValues);

            CurrencyObject newFav = new CurrencyObject(newId, currencyFrom, currencyTo);
            favourites.add(newFav);
            currencyAdapter.notifyDataSetChanged();
        });

    }

    /**
     * Delete a favourite from the array list and database.
     *
     * @param id       within database of this object.
     * @param position within the array list of this object.
     */
    public void deleteMessageId(int id, int position) {
        Log.i("Delete this message:", " id=" + id);
        favourites.remove(position);
        CurrencyDatabaseOpener dbOpener = new CurrencyDatabaseOpener(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();
        db.delete(CurrencyDatabaseOpener.TABLE_NAME, "_id=?",
                new String[]{Long.toString(id)});
        currencyAdapter.notifyDataSetChanged();
    }

    /**
     * If returning from phone fragment, call delete function and notify adapter of change in data.
     *
     * @param requestCode from phone fragment.
     * @param resultCode  check to see if favourite is being removed.
     * @param data        from previous fragment activity.
     */
    //This function only gets called on the phone. The tablet never goes to a new activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EMPTY_ACTIVITY) {
            if (resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getLongExtra(ITEM_ID, 0);
                int position = data.getIntExtra(ITEM_POSITION, 0);
                deleteMessageId((int) id, position);
                currencyAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Save last conversion in shared preferences to display next time user visits.
     */
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("lastSearch", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Spinner spinnerFrom = findViewById(R.id.currencies_spinner_from);
        editor.putString("lastSearchFrom", spinnerFrom.getSelectedItem().toString());

        Spinner spinnerTo = findViewById(R.id.currencies_spinner_to);
        editor.putString("lastSearchTo", spinnerTo.getSelectedItem().toString());

        EditText amount = findViewById(R.id.amount);
        editor.putFloat("amount", Math.round(Float.parseFloat(amount.getText().toString())));

        editor.apply();
    }

    private class CurrencyQuery extends AsyncTask<String, Integer, String> {

        private String date, baseCurrency, targetCurrency;
        private double amount, rate;

        /**
         * Parameters needed to query the currency API. Base currency, target currency, and amount to convert.
         *
         * @param baseCurrency   base currency.
         * @param targetCurrency target currency.
         * @param amount         to convert.
         */
        public CurrencyQuery(String baseCurrency, String targetCurrency, double amount) {
            this.baseCurrency = baseCurrency;
            this.targetCurrency = targetCurrency;
            this.amount = amount;
        }

        /**
         * Query currency API using JSON. Network query so run in background to not disrupt GUI.
         *
         * @param strings not used
         * @return error message.
         */
        @Override
        protected String doInBackground(String... strings) {
            String ret = null;

            String jsonUrl = "https://api.exchangeratesapi.io/latest?base=" + baseCurrency + "&symbols=" + targetCurrency;
            try {       // Connect to the server:
                URL url = new URL(jsonUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                //Set up the JSON object parser:
                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String result = sb.toString();
                JSONObject jObject = new JSONObject(result);
                rate = jObject.getJSONObject("rates").getDouble(targetCurrency);

                BigDecimal roundedRate = new BigDecimal(rate * amount).setScale(2, RoundingMode.HALF_UP);
                rate = roundedRate.doubleValue();
                publishProgress(25);

                baseCurrency = jObject.getString("base");
                publishProgress(50);

                date = jObject.getString("date");
                publishProgress(75);

            } catch (MalformedURLException mfe) {
                ret = "Malformed URL exception";
            } catch (IOException ioe) {
                ret = "IO Exception. Is the Wifi connected?";
            } catch (JSONException e) {
                ret = "Json exception";
            }

            //What is returned here will be passed as a parameter to onPostExecute:
            return ret;
        }

        /**
         * After query executes, update page with data.
         *
         * @param s passed into super constructor.
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ProgressBar progressBar = findViewById(R.id.progress);
            progressBar.setVisibility(View.INVISIBLE);

            TextView afterConversion = findViewById(R.id.after_conversion);
            afterConversion.setText(String.format("Conversion Rate: %s", rate));

            TextView dateField = findViewById(R.id.date);
            dateField.setText(String.format("Date Retrieved: %s", date));

        }

        /**
         * Update progress bar as query executes.
         *
         * @param values for percentage of progress completed.
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            ProgressBar progressBar = findViewById(R.id.progress);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

    }

    /**
     * @param menu toolbar custom menu.
     * @return true always.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.currency_menu, menu);

        return true;
    }

    /**
     * @param item switch statement to determine what each icon on the toolbar does.
     * @return true always.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //what to do when the menu item is selected:
            case R.id.currencyFavourite:
                Intent currencyFavourites = new Intent(CurrencyConverter.this, CurrencyConverter.class);
                startActivity(currencyFavourites);
                break;

            case R.id.currencyHelp:
                //Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    private class CurrencyAdapter extends BaseAdapter {

        /**
         * @return number of favourites in list.
         */
        @Override
        public int getCount() {
            return favourites.size();
        }

        /**
         * @param i number in iteration of list.
         * @return Currency object at i position;
         */
        @Override
        public CurrencyObject getItem(int i) {
            return favourites.get(i);
        }

        /**
         * @param i number in iteration of list.
         * @return database id of currency object.
         */
        @Override
        public long getItemId(int i) {
            return getItem(i).getId();
        }

        /**
         * @param i         number in iteration of list.
         * @param view      XML file to inflate onto screen
         * @param viewGroup not used.
         * @return view for this object.
         */
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.currency_listview, null);
            TextView name = view.findViewById(R.id.currency_name);
            name.setText(String.format("%s <--> %s", getItem(i).getName(), getItem(i).getFavourite()));

            return view;
        }
    }

}