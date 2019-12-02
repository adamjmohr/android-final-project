package com.example.androidlabs.currency;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidlabs.R;

public class CurrencyEmptyActivity extends AppCompatActivity {

    /**
     * Initialize the page for the fragment on phones.
     *
     * @param savedInstanceState used in super constructor.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_empty);

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample
        //So far the screen is blank
        //This is copied directly from FragmentExample.java lines 47-54
        CurrencyFragment cFragment = new CurrencyFragment();
        cFragment.setArguments(dataToPass); //pass data to the the fragment
        cFragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, cFragment)
                .commit();
    }
}
