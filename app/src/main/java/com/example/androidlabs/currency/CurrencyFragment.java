package com.example.androidlabs.currency;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.androidlabs.R;

public class CurrencyFragment extends Fragment {

    /**
     *
     */
    private boolean isTablet;
    /**
     *
     */
    private Bundle dataFromActivity;
    /**
     *
     */
    private long id;
    /**
     *
     */
    private int position;

    /**
     * @param tablet
     */
    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }


    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(CurrencyConverter.ITEM_ID);
        position = dataFromActivity.getInt(CurrencyConverter.ITEM_POSITION);
        /**
         *
         */
        String baseCurrency = dataFromActivity.getString(CurrencyConverter.BASE_CURRENCY);
        String targetCurrency = dataFromActivity.getString(CurrencyConverter.TARGET_CURRENCY);

        View result = inflater.inflate(R.layout.fragment_currency, container, false);

        TextView currency = result.findViewById(R.id.currency);
        currency.setText("Base Currency: " + baseCurrency);

        TextView target = result.findViewById(R.id.target);
        target.setText("Converted to: " + targetCurrency);

        //show the id:
        TextView idView = result.findViewById(R.id.idText);
        idView.setText("ID= " + id);

        // get the delete button, and add a click listener:
        Button deleteButton = result.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(clk -> {
            if (isTablet) { //both the list and details are on the screen:
                CurrencyConverter parent = (CurrencyConverter) getActivity();
                parent.deleteMessageId((int) id, position); //this deletes the item and updates the list

                //now remove the fragment since you deleted it from the database:
                // this is the object to be removed, so remove(this):
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else {
                CurrencyEmptyActivity parent = (CurrencyEmptyActivity) getActivity();
                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra(CurrencyConverter.ITEM_ID, dataFromActivity.getLong(CurrencyConverter.ITEM_ID));
                backToFragmentExample.putExtra(CurrencyConverter.ITEM_POSITION, dataFromActivity.getInt(CurrencyConverter.ITEM_POSITION));
                backToFragmentExample.putExtra(CurrencyConverter.BASE_CURRENCY, dataFromActivity.getString(CurrencyConverter.BASE_CURRENCY));
                backToFragmentExample.putExtra(CurrencyConverter.TARGET_CURRENCY, dataFromActivity.getString(CurrencyConverter.TARGET_CURRENCY));

                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });

        // get the back button, and add a click listener:
        Button backButton = result.findViewById(R.id.back);
        backButton.setOnClickListener(clk -> {
            CurrencyEmptyActivity parent = (CurrencyEmptyActivity) getActivity();
            parent.finish();
        });
        return result;
    }
}