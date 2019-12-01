package com.example.androidlabs.Recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.androidlabs.R;
import com.squareup.picasso.Picasso;


public class RecipeDetailFragment extends Fragment {

    private boolean isTablet;
    private Bundle bundle;


    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bundle = getArguments();

        View result = inflater.inflate(R.layout.frament_recipe_detail, container, false);

        ImageView imageView = result.findViewById(R.id.imageRecipe);
        String path = bundle.getString(RecipeDatabaseHelper.COL_IMAGE);
        Picasso.get().load(path).into(imageView);

        TextView textTitle = (TextView) result.findViewById(R.id.textTitle);
        textTitle.setText("Title: " + bundle.getString(RecipeDatabaseHelper.COL_TITLE));

        TextView textPublisher = (TextView) result.findViewById(R.id.textUrlValue);
        textPublisher.setText("Publisher:" + bundle.getString(RecipeDatabaseHelper.COL_CONTENT_URL));

        TextView imgurlText = (TextView) result.findViewById(R.id.textValue2);
        imgurlText.setText("Image Url: " + bundle.getString(RecipeDatabaseHelper.COL_IMAGE));

        return result;
    }
}