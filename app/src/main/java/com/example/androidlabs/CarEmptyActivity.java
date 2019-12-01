package com.example.androidlabs;

import android.os.Bundle;

/**
 * Handles inflating of search fragment for phones
 */
import androidx.appcompat.app.AppCompatActivity;

public class CarEmptyActivity extends AppCompatActivity {

    /**
     * Create and display Search object fragment
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_empty);

        Bundle dataToPass = getIntent().getExtras();

        CarSearchDetailFragment dFragment = new CarSearchDetailFragment();
        dFragment.setArguments(dataToPass);
        dFragment.setTablet(false);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.carFragmentLocation, dFragment)
                .addToBackStack("AnyName")
                .commit();
    }
}
