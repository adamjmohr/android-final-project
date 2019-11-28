package com.example.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class CarFavDetailFragment extends Fragment {
    private boolean isTablet;
    private Bundle dataFromActivity;
    private int id;
    private int pos;
    private String title;
    private String lat;
    private String lon;
    private String phoneNum;

    public void setTablet(boolean tablet) { isTablet = tablet; }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        pos = dataFromActivity.getInt(ElectricCarFinder.ITEM_POSITION);
        id = (int)dataFromActivity.getLong(CarDatabaseHelper.COL_ID);
        title = dataFromActivity.getString(CarDatabaseHelper.COL_TITLE);
        lat = dataFromActivity.getString(CarDatabaseHelper.COL_LAT);
        lon = dataFromActivity.getString(CarDatabaseHelper.COL_LON);
        phoneNum = dataFromActivity.getString(CarDatabaseHelper.COL_PHONENUM);

        View result =  inflater.inflate(R.layout.car_fav_fragment, container, false);

        TextView idTitle = (TextView)result.findViewById(R.id.carFavDetailId);
        idTitle.setText("ID: " + id);

        TextView textTitle = (TextView)result.findViewById(R.id.carFavDetailTitle);
        textTitle.setText("Title: " + title);

        TextView textPhone = (TextView)result.findViewById(R.id.carFavDetailPhoneNum);
        textPhone.setText("Phone Number:" + phoneNum);

        TextView textLat = (TextView)result.findViewById(R.id.carFavDetailLat);
        textLat.setText("Latitude:" + lat);

        TextView textLon = (TextView)result.findViewById(R.id.carFavDetailLon);
        textLon.setText("Longitude:" + lon);

        ImageButton closeButton = (ImageButton)result.findViewById(R.id.carFavFragBackButton);
        closeButton.setOnClickListener( clk -> {


            if(isTablet) {
                CarFavoritesList parent = (CarFavoritesList)getActivity();

                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                CarFavEmpty parent = (CarFavEmpty) getActivity();
                Intent backToFragmentExample = new Intent();


                parent.setResult(Activity.RESULT_CANCELED, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });

        Button removeFavButton = (Button)result.findViewById(R.id.carFavDetailFavoriteButton);
        removeFavButton.setOnClickListener( clk -> {


            if(isTablet) {
                CarFavoritesList parent = (CarFavoritesList)getActivity();
                parent.removeFromFavorite(id, pos);

                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                CarFavEmpty parent = (CarFavEmpty) getActivity();
                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra(CarDatabaseHelper.COL_ID, id );
                backToFragmentExample.putExtra(ElectricCarFinder.ITEM_POSITION, pos );

                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });

        Button navigateButton = (Button)result.findViewById(R.id.carFavDetailGoButton);
        navigateButton.setOnClickListener( clk -> {

            String strUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lon + " (" + title + ")";
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        });



        return result;
    }
}
