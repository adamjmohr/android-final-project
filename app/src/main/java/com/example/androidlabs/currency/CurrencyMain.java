package com.example.androidlabs.currency;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidlabs.R;

import java.util.ArrayList;
import java.util.Arrays;

public class CurrencyMain extends AppCompatActivity {

    public static final int EMPTY_ACTIVITY = 345;
    private ArrayAdapter<String> theAdapter;
    private ArrayList<String> currencies = new ArrayList<>(Arrays.asList("CAD", "HKD", "ISK", "PHP",
            "DKK", "HUF", "CZK", "AUD", "RON", "SEK", "IDR", "INR", "BRL", "RUB", "HRK", "JPY", "THB", "CHF",
            "SGD", "PLN", "BGN", "TRY", "CNY", "NOK", "NZD", "ZAR", "USD", "MXN", "ILS", "GBP", "KRW",
            "MYR", "EUR"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_main);

        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        ListView theList = findViewById(R.id.theList);
        theAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currencies);
        theList.setAdapter(theAdapter);
        theList.setOnItemClickListener((list, item, position, id) -> {
            Bundle dataToPass = new Bundle();

            if (isTablet) {
                CurrencyFragment cFragment = new CurrencyFragment(); //add a DetailFragment
                cFragment.setArguments(dataToPass); //pass it a bundle for information
                cFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentLocation, cFragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            } else {//isPhone
                Intent nextActivity = new Intent(CurrencyMain.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
            }
        });

        Button goToConverter = findViewById(R.id.goToConvert);
        goToConverter.setOnClickListener(clk -> {
            Intent converter = new Intent(CurrencyMain.this, CurrencyConverter.class);
            startActivity(converter);
        });


    }
}
