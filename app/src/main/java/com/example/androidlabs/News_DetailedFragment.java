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
 *  * @author Kaikai Mao
 *  * @since 11/25/2019
 */
public class News_DetailedFragment extends Fragment {
    private boolean isTablet;
    News_DatabaseOpenHelper dbOpener;
    private TextView articleDescription;
    private TextView articleTitle;
    private ImageView articleImage;
    private TextView articleUrl;
    private News_item item;

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
         item = (News_item) bundle.getSerializable("Article");
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.activity_news_clicked_item, container, false);
         articleTitle = result.findViewById(R.id.news_title);
         articleDescription = result.findViewById(R.id.news_description);
         articleImage = result.findViewById(R.id.details_image);
         articleUrl = result.findViewById(R.id.url_textview);

        //show the message

        articleTitle.setText("Title: "+item.getNews_title());
        articleDescription.setText("Description: "+ item.getNews_description());
//        URL url = null;
//        Bitmap bmp = null;
//        try {
//             url = new URL(item.getNews_imageUrl());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        if (url != null) {
//            try {
//                 bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        articleImage.setImageBitmap(bmp);
        Picasso.get().load(item.getNews_imageUrl()).into(articleImage);
        articleUrl.setText("News URL: " + item.getNews_url());

        Button openInBrowser = result.findViewById(R.id.go_to_url_button);
        Button addToFavouritesButton = result.findViewById(R.id.add_to_favourites_button);
        Button backBtn = result.findViewById(R.id.goback);

        SQLiteDatabase db = dbOpener.getWritableDatabase();

        backBtn.setOnClickListener(v -> {
            if (isTablet) {
                NewsHeadlines parent = (NewsHeadlines) getActivity();
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            } else {
                News_EmptyActivity parent = (News_EmptyActivity) getActivity();
                parent.finish();
            }
        });

        addToFavouritesButton.setOnClickListener(fav -> {

            ContentValues newRowValues = new ContentValues();
            newRowValues.put(News_DatabaseOpenHelper.COL_TITLE, item.getNews_title());
            newRowValues.put(News_DatabaseOpenHelper.COL_DESCRIPTION, item.getNews_description());
            newRowValues.put(News_DatabaseOpenHelper.COL_ARTICLEURL, item.getNews_url());
            newRowValues.put(News_DatabaseOpenHelper.COL_IMAGEURL, item.getNews_imageUrl());
            long newId = db.insert(News_DatabaseOpenHelper.TABLE_NAME, null, newRowValues);
            AlertDialog.Builder builder = new AlertDialog.Builder(News_DetailedFragment.super.getActivity());
            AlertDialog dialog = builder.setMessage("added News to Favourites!")
                    .setPositiveButton("OK", (d, w) -> {  /* nothing */})
                    .create();
            dialog.show();

        });


        openInBrowser.setOnClickListener(browser -> {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getNews_url()));
            startActivity(browserIntent);

        });



        return result;
    }



}
