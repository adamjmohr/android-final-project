package com.example.androidlabs.Recipe;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.androidlabs.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;


/**This is our Fragment class that is used to display the details of or Records/List items
 * It extends Fragment and implements View.OnClickListener.
 * Sets an onClickListener() to our Star icon for using the Favorites List
 */
public class RecipeDetailFragment extends Fragment implements View.OnClickListener{

    private boolean isTablet;
    private boolean isFave;
    private Bundle bundle;
    private String title;
    private ImageButton faveButton;


    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }


    /** This is the Override of Fragment Class's onCreateView.
     * It sets up all the values in our layout like, text, images, and hyperlinks
     * @param inflater @see Fragment.onCreateView()
     * @param container @see Fragment.onCreateView()
     * @param savedInstanceState @see Fragment.onCreateView()
     * @return returns the view back to the calling activity
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bundle = getArguments();

        title = bundle.getString(RecipeDatabaseHelper.COL_TITLE);

        View result = inflater.inflate(R.layout.frament_recipe_detail, container, false);

        ImageView imageView = result.findViewById(R.id.imageRecipe);
        String path = bundle.getString(RecipeDatabaseHelper.COL_IMAGE);
        Picasso.get().load(path).into(imageView);

        TextView textTitle = (TextView) result.findViewById(R.id.textTitle);
        textTitle.setText(getString(R.string.Title) + title);

        faveButton = result.findViewById(R.id.faveButton);

        faveButton.setOnClickListener(this);
        updateIcon();

        Spanned Text;

        TextView HyperLink= result.findViewById(R.id.textValue);;
        String urlLink = bundle.getString(RecipeDatabaseHelper.COL_WEBSITE);
        String publisher = bundle.getString(RecipeDatabaseHelper.PUBLISHER);


            Text = Html.fromHtml(getString(R.string.Publisher)+publisher+ " " +
                    "<br><a href='"+urlLink+"'>"+getString(R.string.gotorecipe)+"</a>");

            HyperLink.setMovementMethod(LinkMovementMethod.getInstance());
            HyperLink.setText(Text);

        return result;
    }

    /** A method to set up the onClick function to control what happens when our button is clicked
     * @param v pass the Image button
     */
    @Override
    public void onClick(View v) {

        String tableName = RecipeDatabaseHelper.FAVORITE_TABLE;
        String image = bundle.getString(RecipeDatabaseHelper.COL_IMAGE);
        String recipeId = bundle.getString(RecipeDatabaseHelper.COL_WEBSITE);
        String publisher = bundle.getString(RecipeDatabaseHelper.PUBLISHER);



        switch (v.getId()) {
            case R.id.faveButton:
                if (RecipeDatabaseHelper.doesRecordExist(tableName, RecipeDatabaseHelper.COL_TITLE, title)) {
                    //show a notification: first parameter is any view on screen. second parameter is the text. Third parameter is the length (SHORT/LONG)
                    Snackbar.make(v, "removed from Favorites", Snackbar.LENGTH_LONG).show();
                    RecipeDatabaseHelper.deleteItem(title , tableName);
                    updateIcon();
                } else {
                    Snackbar.make(v, "added to Favorites", Snackbar.LENGTH_LONG).show();
                    RecipeDatabaseHelper.insertItem(tableName, title, publisher, recipeId, image);
                    updateIcon();
                }
                break;
            default:
                break;
        }

    }

    /**
     * A simple function to update the filled/unfilled star icon based on if the record is in the Favorites Table
     */
    private void updateIcon() {
        isFave = RecipeDatabaseHelper.doesRecordExist(RecipeDatabaseHelper.FAVORITE_TABLE, RecipeDatabaseHelper.COL_TITLE, title);
        if (RecipeDatabaseHelper.doesRecordExist(RecipeDatabaseHelper.FAVORITE_TABLE, RecipeDatabaseHelper.COL_TITLE, title)) {
            faveButton.setImageResource(R.drawable.star_filled);
        } else {
            faveButton.setImageResource(R.drawable.star_unfilled);
        }
    }


}