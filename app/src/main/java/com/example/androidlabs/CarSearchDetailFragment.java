package com.example.androidlabs;

import android.app.Activity;
import android.content.Intent;
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
    private long id;
    private String title;
    private String lat;
    private String lon;
    private String phoneNum;

    public void setTablet(boolean tablet) { isTablet = tablet; }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(CarDatabaseHelper.COL_ID );
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
        textLat.setText("Latitude:" + phoneNum);

        TextView textLon = (TextView)result.findViewById(R.id.carDetailLon);
        textLon.setText("Longitude:" + phoneNum);


        return result;
    }
}
