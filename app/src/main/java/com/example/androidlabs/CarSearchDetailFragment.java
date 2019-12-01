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

/**
 * Fragment for display information about selected Search chose from list view
 * Handles buttons actions in fragment
 */
public class CarSearchDetailFragment extends Fragment {
    /**
     * Is the object a tablet
     */
    private boolean isTablet;
    /**
     * Data of the item being displayed in fragment
     */
    private Bundle dataFromActivity;
    /**
     * id of item
     */
    private int id;
    /**
     * position of item
     */
    private int pos;
    /**
     * Title of car charging station
     */
    private String title;
    /**
     * Latitude of car charging station
     */
    private String lat;
    /**
     * Longitude of car charging station
     */
    private String lon;
    /**
     * Phone number of car charging station
     */
    private String phoneNum;

    /**
     * Sets boolean containing if the device is a tablet or not
     * @param tablet Is the device a tablet
     */
    public void setTablet(boolean tablet) { isTablet = tablet; }

    /**
     * Displays information about car charging station and its placement in the database
     * Handles the buttons press of the backbutton the gobutton and the add to favorties button
     * @param inflater inflater used ti inflate the fragment
     * @param container container to hold fragment
     * @param savedInstanceState
     * @return inflated fragment
     */
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


        ImageButton closeButton = (ImageButton)result.findViewById(R.id.carFragBackButton);
        closeButton.setOnClickListener( clk -> {


            if(isTablet) {
                ElectricCarFinder parent = (ElectricCarFinder)getActivity();

                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                CarEmptyActivity parent = (CarEmptyActivity) getActivity();
                Intent backToFragmentExample = new Intent();


                parent.setResult(Activity.RESULT_CANCELED, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });

        Button addFavButton = (Button)result.findViewById(R.id.carDetailFavoriteButton);
        addFavButton.setOnClickListener( clk -> {


            if(isTablet) {
                ElectricCarFinder parent = (ElectricCarFinder)getActivity();
                parent.addToFavorite(id);

                            }
            //for Phone:
            else             {
                CarEmptyActivity parent = (CarEmptyActivity) getActivity();
                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra(CarDatabaseHelper.COL_ID, id );

                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish();
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
