package com.example.androidlabs;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class CurrencyConverter extends AppCompatActivity {

    private ArrayAdapter<String> theAdapter;
    private ArrayList<String> currencies = new ArrayList<>(Arrays.asList("CAD", "HKD", "ISK", "PHP",
            "DKK", "HUF", "CZK", "AUD", "RON", "SEK", "IDR", "INR", "BRL", "RUB", "HRK", "JPY", "THB", "CHF",
            "SGD", "PLN", "BGN", "TRY", "CNY", "NOK", "NZD", "ZAR", "USD", "MXN", "ILS", "GBP", "KRW",
            "MYR", "EUR"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_layout);

        ListView theList = findViewById(R.id.theList);
        theAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currencies);
        theList.setAdapter(theAdapter);
        theList.setOnItemClickListener((list, item, position, id) -> {

        });

    }
}
