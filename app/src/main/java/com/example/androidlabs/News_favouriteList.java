package com.example.androidlabs;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.content.DialogInterface;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * load the favouritelist and click on item will open up the link in webview
 */
public class News_favouriteList extends AppCompatActivity {

    private ArrayList<News_item> favouriteList;
    private ListView favourListView;
    private MyListAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_news);

        Button gobackButton = findViewById(R.id.gobackToMainButton);
        gobackButton.setOnClickListener(v->{
            finish();
        });

         favouriteList = new ArrayList<>();
         favourListView = findViewById(R.id.favourites_list_view);
         adapter = new MyListAdapter(favouriteList,this);
        favourListView.setAdapter(adapter);

        News_DatabaseOpenHelper dbopener = new News_DatabaseOpenHelper(this);
        SQLiteDatabase db = dbopener.getWritableDatabase();

        String[] columns = {News_DatabaseOpenHelper.COL_ID, News_DatabaseOpenHelper.COL_TITLE, News_DatabaseOpenHelper.COL_DESCRIPTION,
                News_DatabaseOpenHelper.COL_ARTICLEURL, News_DatabaseOpenHelper.COL_IMAGEURL};
        Cursor results = db.query(false, News_DatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        int titleColumnIndex = results.getColumnIndex(News_DatabaseOpenHelper.COL_TITLE);
        int descriptionColumnIndex = results.getColumnIndex(News_DatabaseOpenHelper.COL_DESCRIPTION);
        int idColumnIndex = results.getColumnIndex(News_DatabaseOpenHelper.COL_ID);
        int articleUrlColumnIndex = results.getColumnIndex(News_DatabaseOpenHelper.COL_ARTICLEURL);
        int imageUrlColumnIndex = results.getColumnIndex(News_DatabaseOpenHelper.COL_IMAGEURL);
        while (results.moveToNext()) {

            String title = results.getString(titleColumnIndex);
            String description = results.getString(descriptionColumnIndex);
            String articleUrl = results.getString(articleUrlColumnIndex);
            String imageUrl = results.getString(imageUrlColumnIndex);
            long id = results.getLong(idColumnIndex);


            favouriteList.add(new News_item(id, title, articleUrl, imageUrl, description));
        }
        adapter.notifyDataSetChanged();
        /**
         * click item opens up the url link in webview
         */
        favourListView.setOnItemClickListener(((parent, view, position, id) -> {
            News_item item = (News_item)parent.getItemAtPosition(position);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getNews_url()));
            startActivity(intent);
        }));

//        favourListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//                News_item item = (News_item) parent.getItemAtPosition(position);
//
//                Intent nextActivity = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getNews_url()));
//                startActivity(nextActivity);
//
//
//            }
//        });

        /**
         * Long click pops up alert dialog asking whether delete or not
         */
        favourListView.setOnItemLongClickListener((parent, view, position, id) ->  {

                // it will get the position of selected item from the ListView

                final int selected_item = position;

                new AlertDialog.Builder(News_favouriteList.this).
                        setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure...")
                        .setMessage("Do you want to delete the selected item..?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                db.delete(News_DatabaseOpenHelper.TABLE_NAME,
                                        News_DatabaseOpenHelper.COL_ID + "=?", new String[]{Long.toString(favouriteList.get(selected_item).getNews_id())});
                                favouriteList.remove(selected_item);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No" , null).show();

                return true;

        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
