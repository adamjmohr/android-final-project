package com.example.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class CarSearchDetailFragment extends Fragment {
    private boolean isTablet;
    private Bundle dataFromActivity;
    private int id;
    private String title;
    private String lat;
    private String lon;
    private String phoneNum;

    public void setTablet(boolean tablet) { isTablet = tablet; }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getInt("POSITION");
        title = dataFromActivity.getString(CarDatabaseHelper.COL_TITLE);
        lat = dataFromActivity.getString(CarDatabaseHelper.COL_LAT);
        lon = dataFromActivity.getString(CarDatabaseHelper.COL_LON);
        phoneNum = dataFromActivity.getString(CarDatabaseHelper.COL_PHONENUM);

        View result =  inflater.inflate(R.layout.car_search_detail, container, false);

        TextView textTitle = (TextView)result.findViewById(R.id.carDetailTitle);
        textTitle.setText("Title: " + title);

        TextView textPhone = (TextView)result.findViewById(R.id.carDetailPhoneNum);
        textPhone.setText("Phone Number:" + phoneNum);

        TextView textLat = (TextView)result.findViewById(R.id.carDetailLat);
        textLat.setText("Latitude:" + lat);

        TextView textLon = (TextView)result.findViewById(R.id.carDetailLon);
        textLon.setText("Longitude:" + lon);

        Button addFavButton = (Button)result.findViewById(R.id.carDetailFavoriteButton);
        addFavButton.setOnClickListener( clk -> {


            if(isTablet) {
                ElectricCarFinder parent = (ElectricCarFinder)getActivity();
                parent.addToFavorite(id);

                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                CarEmptyActivity parent = (CarEmptyActivity) getActivity();
                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra(CarDatabaseHelper.COL_ID, id );

                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });

        Button navigateButton = (Button)result.findViewById(R.id.carDetailGoButton);
        navigateButton.setOnClickListener( clk -> {

            String strUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lon + " (" + title + ")";
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        });



        return result;
    }
}
