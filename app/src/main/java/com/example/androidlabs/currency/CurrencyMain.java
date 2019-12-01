package com.example.androidlabs.currency;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.androidlabs.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

public class CurrencyMain extends AppCompatActivity {

    /**
     * Custom adapter for currency objects.
     */
    private BaseAdapter currencyAdapter;
    /**
     * All the queries supported by the API are fetched and stored here to be displayed to the user.
     */
    private ArrayList<CurrencyObject> currencies;


    /**
     * Initializes widgets on the screen.
     *
     * @param savedInstanceState not used on this activity after onCreate gets called
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_main);
        currencies = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CurrencyQuery query = new CurrencyQuery();
        query.execute();

        ListView theList = findViewById(R.id.theList);
        theList.setAdapter(currencyAdapter = new CurrencyAdapter());

        Button goToConverter = findViewById(R.id.goToConvert);
        goToConverter.setOnClickListener(clk -> {
            Intent converter = new Intent(CurrencyMain.this, CurrencyConverter.class);
            startActivity(converter);
        });
    }

    /**
     * @param menu calls custom menu XML file.
     * @return true always
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
     * @return true always
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //what to do when the menu item is selected:
            case R.id.currencyFavourite:
                Intent currencyFavourites = new Intent(CurrencyMain.this, CurrencyConverter.class);
                startActivity(currencyFavourites);
                break;

            case R.id.currencyHelp:
                //Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    private class CurrencyQuery extends AsyncTask<String, Integer, String> {

        /**
         * @param strings not used
         * @return any error message that may have occurred
         */
        @Override
        protected String doInBackground(String... strings) {

            String ret = null;

            String jsonUrl = "https://api.exchangeratesapi.io/latest";
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
                Iterator<String> iter = jObject.getJSONObject("rates").keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    currencies.add(new CurrencyObject(key));
                }
                currencies.add(new CurrencyObject("EUR"));

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
         * Loads adapter with Currencies after query is complete.
         *
         * @param s passed in to paramter. Not used.
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            currencyAdapter.notifyDataSetChanged();
        }
    }

    public class CurrencyAdapter extends BaseAdapter {

        /**
         * @return number of currencies in list.
         */
        @Override
        public int getCount() {
            return currencies.size();
        }

        /**
         * @param i number in iteration of list.
         * @return Currency object at i position;
         */
        @Override
        public CurrencyObject getItem(int i) {
            return currencies.get(i);
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
            name.setText(getItem(i).getName());

            return view;
        }
    }
}
