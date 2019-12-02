package com.example.androidlabs;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.content.DialogInterface;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * load the favouritelist and click on item will open up the link in webview
 *  @author Kaikai Mao
 *  @since 11/25/2019
 */
public class News_favouriteList extends AppCompatActivity {
    /**
     * the arraylist to store news items upon the user's selection
     */
    private ArrayList<News_item> favouriteList;
    /**
     * the Listview to show the favourite list
     */
    private ListView favourListView;
    /**
     * the adapter to insert items into the list view
     */
    private MyListAdapter adapter;

    /**
     * onCreate method to launch the favourite list activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_news);
        //invoke the goback button to newsheadlines page, once clicked, it goes back to the previous page
        Button gobackButton = findViewById(R.id.gobackToMainButton);
        gobackButton.setOnClickListener(v->{
            finish();
        });

        //prepare the favourite list
         favouriteList = new ArrayList<>();
         favourListView = findViewById(R.id.favourites_list_view);
         adapter = new MyListAdapter(favouriteList,this);
        favourListView.setAdapter(adapter);

        //load the favourite list from the database
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

            //add the details to the news items
            favouriteList.add(new News_item(id, title, description,articleUrl, imageUrl ));
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

        /**
         * Long click pops up alert dialog asking whether delete or not
         * source: https://teamtreehouse.com/community/removing-item-from-listview
         */
        favourListView.setOnItemLongClickListener((parent, view, position, id) ->  {
                // it will get the position of selected item from the ListView
                final int selected_item = position;
                new AlertDialog.Builder(News_favouriteList.this).
                        setIcon(android.R.drawable.ic_delete)
                        .setTitle(R.string.favorlist_dialog_title)
                        .setMessage(R.string.favorlist_dialog_msg)
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
