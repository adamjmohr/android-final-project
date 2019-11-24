package com.example.androidlabs.currency;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidlabs.R;

public class CurrencyConverter extends AppCompatActivity {

    public static final int EMPTY_ACTIVITY = 345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_converter);

        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

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
                Intent nextActivity = new Intent(CurrencyConverter.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
            }
        });


    }

}
