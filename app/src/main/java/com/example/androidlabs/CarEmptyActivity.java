package com.example.androidlabs;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CarEmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_empty);

        Bundle dataToPass = getIntent().getExtras();

        CarSearchDetailFragment dFragment = new CarSearchDetailFragment();
        dFragment.setArguments(dataToPass); //pass data to the the fragment
        dFragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.carFragmentLocation, dFragment)
                .addToBackStack("AnyName")
                .commit();
    }
}
