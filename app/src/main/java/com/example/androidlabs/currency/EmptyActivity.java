package com.example.androidlabs.currency;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidlabs.R;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_empty);

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample
        //So far the screen is blank
        //This is copied directly from FragmentExample.java lines 47-54
        CurrencyFragment dFragment = new CurrencyFragment();
        dFragment.setArguments(dataToPass); //pass data to the the fragment
        dFragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentLocation, dFragment)
                .addToBackStack("AnyName")
                .commit();
    }
}
