package com.example.androidlabs;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.View.GONE;

/**
 * MyListAdapter is a subclass of BaseAdapter
 * overrides all the methods of BaseAdapter
 */
public class MyListAdapter extends BaseAdapter {
    private ArrayList<News_item> newsItems;
    private Context context;

    public MyListAdapter(ArrayList<News_item> newsItems, Context context) {
        this.newsItems = newsItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return newsItems.size();
    }

    @Override
    public News_item getItem(int position) {
        return newsItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View thisRow = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        if(convertView==null)
            thisRow = inflater.inflate(R.layout.activity_newslist_row_layout,null);

        TextView itemText = thisRow.findViewById(R.id.row_title);
        News_item item = newsItems.get(position);

        if(!item.getNews_title().isEmpty())
            itemText.setText(Html.fromHtml(item.getNews_title()));
        else
            itemText.setVisibility(GONE);



        return thisRow;

    }
}
