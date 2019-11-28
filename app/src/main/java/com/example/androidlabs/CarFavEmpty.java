package com.example.androidlabs;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CarFavEmpty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_fav_empty);

        Bundle dataToPass = getIntent().getExtras();

        CarFavDetailFragment dFragment = new CarFavDetailFragment();
        dFragment.setArguments(dataToPass); //pass data to the the fragment
        dFragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.carFavFragmentLocation, dFragment)
                .addToBackStack("AnyName")
                .commit();
    }
}
