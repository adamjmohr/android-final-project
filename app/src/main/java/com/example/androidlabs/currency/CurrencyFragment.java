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

    private boolean isTablet;
    private Bundle dataFromActivity;

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataFromActivity = getArguments();

        View result = inflater.inflate(R.layout.fragment_currency, container, false);

        TextView currency = result.findViewById(R.id.currency);
        currency.setText("Currency: ");

        TextView country = result.findViewById(R.id.country);
        country.setText("Country: ");

        // get the back button, and add a click listener:
        Button backButton = result.findViewById(R.id.back);
        backButton.setOnClickListener(clk -> {
            if (isTablet) { //both the list and details are on the screen:
                CurrencyConverter parent = (CurrencyConverter) getActivity();

                //now remove the fragment since you deleted it from the database:
                // this is the object to be removed, so remove(this):
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else {
                EmptyActivity parent = (EmptyActivity) getActivity();
                Intent backToFragmentExample = new Intent();
//                backToFragmentExample.putExtra(ChatRoomActivity.ITEM_ID, dataFromActivity.getLong(ChatRoomActivity.ITEM_ID));
//                backToFragmentExample.putExtra(ChatRoomActivity.ITEM_POSITION, dataFromActivity.getInt(ChatRoomActivity.ITEM_POSITION));

                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });
        return result;
    }
}