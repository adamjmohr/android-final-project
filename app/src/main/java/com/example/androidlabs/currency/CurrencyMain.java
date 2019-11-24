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

    private ArrayAdapter<String> theAdapter;
    private ArrayList<String> currencies = new ArrayList<>(Arrays.asList("CAD", "HKD", "ISK", "PHP",
            "DKK", "HUF", "CZK", "AUD", "RON", "SEK", "IDR", "INR", "BRL", "RUB", "HRK", "JPY", "THB", "CHF",
            "SGD", "PLN", "BGN", "TRY", "CNY", "NOK", "NZD", "ZAR", "USD", "MXN", "ILS", "GBP", "KRW",
            "MYR", "EUR"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_main);

        ListView theList = findViewById(R.id.theList);
        theAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currencies);
        theList.setAdapter(theAdapter);
        theList.setOnItemClickListener((list, item, position, id) -> {

        });

        Button goToConverter = findViewById(R.id.goToConvert);
        goToConverter.setOnClickListener(clk -> {
            Intent converter = new Intent(CurrencyMain.this, CurrencyConverter.class);
            startActivity(converter);
        });

    }
}
