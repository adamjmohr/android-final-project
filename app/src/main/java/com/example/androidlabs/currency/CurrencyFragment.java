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
    private long id;
    private int position;

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
//        id = dataFromActivity.getLong(ChatRoomActivity.ITEM_ID);
//        position = dataFromActivity.getInt(ChatRoomActivity.ITEM_POSITION);

        // Inflate the layout for this fragment
        //View result = inflater.inflate(R.layout.fragment_detail, container, false);

        //show
//        deleteButton.setOnClickListener(clk -> {
//
//            if (isTablet) { //both the list and details are on the screen:
//                CurrencyConverter parent = (CurrencyConverter) getActivity();
//
//
//                //now remove the fragment since you deleted it from the database:
//                // this is the object to be removed, so remove(this):
//                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
//            }
//            //for Phone:
//            else //You are only looking at the details, you need to go back to the previous list page
//            {
//                EmptyActivity parent = (EmptyActivity) getActivity();
//                Intent backToFragmentExample = new Intent();
////                backToFragmentExample.putExtra(ChatRoomActivity.ITEM_ID, dataFromActivity.getLong(ChatRoomActivity.ITEM_ID));
////                backToFragmentExample.putExtra(ChatRoomActivity.ITEM_POSITION, dataFromActivity.getInt(ChatRoomActivity.ITEM_POSITION));
//
//                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
//                parent.finish(); //go back
//            }
//        });
        return null;
    }
}
