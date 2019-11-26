package com.example.androidlabs.currency;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidlabs.R;

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

public class CurrencyConverter extends AppCompatActivity {

    private double amountToConvert;
    private String currencyFrom, currencyTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_converter);

        ProgressBar progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        EditText amount = findViewById(R.id.amount);

        Spinner spinnerFrom = findViewById(R.id.currencies_spinner_from);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterFrom = ArrayAdapter.createFromResource(this,
                R.array.currencies_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerFrom.setAdapter(adapterFrom);

        Spinner spinnerTo = findViewById(R.id.currencies_spinner_to);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterTo = ArrayAdapter.createFromResource(this,
                R.array.currencies_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerTo.setAdapter(adapterTo);

        Button runConversion = findViewById(R.id.run_conversion);
        runConversion.setOnClickListener(clk -> {
            amountToConvert = Double.parseDouble(amount.getText().toString());
            currencyFrom = spinnerFrom.getSelectedItem().toString();
            currencyTo = spinnerTo.getSelectedItem().toString();

            CurrencyQuery query = new CurrencyQuery(currencyFrom, currencyTo, amountToConvert);
            query.execute();

        });


    }

    private class CurrencyQuery extends AsyncTask<String, Integer, String> {

        private String date, baseCurrency, targetCurrency;
        private double amount, rate;

        public CurrencyQuery(String baseCurrency, String targetCurrency, double amount) {
            this.baseCurrency = baseCurrency;
            this.targetCurrency = targetCurrency;
            this.amount = amount;
        }

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

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            ProgressBar progressBar = findViewById(R.id.progress);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }


    }

}