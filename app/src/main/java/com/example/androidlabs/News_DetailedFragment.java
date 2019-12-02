package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.squareup.picasso.Picasso;

/**
 * functionality about the fragment layout
 *  @author Kaikai Mao
 *  @since 11/25/2019
 */
public class News_DetailedFragment extends Fragment {
    /**
     * isTablet
     */
    private boolean isTablet;
    /**
     * database opener
     */
    private News_DatabaseOpenHelper dbOpener;

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbOpener = new News_DatabaseOpenHelper(News_DetailedFragment.super.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        News_item item = (News_item) bundle.getSerializable("Article");
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.activity_news_clicked_item, container, false);
        TextView articleTitle = result.findViewById(R.id.news_title);
        TextView articleDescription = result.findViewById(R.id.news_description);
        ImageView articleImage = result.findViewById(R.id.details_image);
        TextView articleUrl = result.findViewById(R.id.url_textview);

        //show the message
        articleTitle.setText("Title: "+item.getNews_title());
        articleDescription.setText("Description: "+ item.getNews_description());
        //use picasso to load the image
        //source:https://square.github.io/picasso/
        Picasso.get().load(item.getNews_imageUrl()).into(articleImage);
        articleUrl.setText("News URL: " + item.getNews_url());
        Button openInBrowser = result.findViewById(R.id.go_to_url_button);
        Button addToFavouritesButton = result.findViewById(R.id.add_to_favourites_button);
        Button backBtn = result.findViewById(R.id.goback);

        SQLiteDatabase db = dbOpener.getWritableDatabase();
        //back button functionality
        backBtn.setOnClickListener(v -> {
            if (isTablet) {
                NewsHeadlines parent = (NewsHeadlines) getActivity();
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            } else {
                News_EmptyActivity parent = (News_EmptyActivity) getActivity();
                parent.finish();
            }
        });
        //add to favour list button functionality,it invokes the database
        addToFavouritesButton.setOnClickListener(fav -> {
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(News_DatabaseOpenHelper.COL_TITLE, item.getNews_title());
            newRowValues.put(News_DatabaseOpenHelper.COL_DESCRIPTION, item.getNews_description());
            newRowValues.put(News_DatabaseOpenHelper.COL_ARTICLEURL, item.getNews_url());
            newRowValues.put(News_DatabaseOpenHelper.COL_IMAGEURL, item.getNews_imageUrl());
            long newId = db.insert(News_DatabaseOpenHelper.TABLE_NAME, null, newRowValues);
            AlertDialog.Builder builder = new AlertDialog.Builder(News_DetailedFragment.super.getActivity());
            AlertDialog dialog = builder.setMessage(R.string.detailF_dialog_msg)
                    .setPositiveButton("OK", (d, w) -> {  /* nothing */})
                    .create();
            dialog.show();
        });
        //open the news in webview
        openInBrowser.setOnClickListener(browser -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getNews_url()));
            startActivity(browserIntent);
        });
        return result;
    }
}
