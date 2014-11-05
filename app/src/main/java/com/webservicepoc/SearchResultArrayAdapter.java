package com.webservicepoc;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.webservicepoc.Model.Result;

import java.util.List;

/**
 * Created by Bryant on 11/4/2014.
 *
 * This class allows for complex ListView rows and is a more performant implementation of a ListView
 */
public class SearchResultArrayAdapter extends ArrayAdapter<Result> {
    private List<Result> searchResults;
    private Context context;
    private int layoutResource;

    public SearchResultArrayAdapter(Context context, int layoutResource, List<Result> searchResults) {
        super(context, layoutResource, searchResults);
        this.context = context;

        // This is a custom defined layout that will be used for each row in the ListView
        this.layoutResource = layoutResource;
        this.searchResults = searchResults;
    }

    /*
    * Performance Notes - http://stackoverflow.com/questions/15912999/recycling-views-in-custom-array-adapter-how-exactly-is-it-handled
    *                   - http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
    * */
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        ViewHolder viewHolder;

        //Check if a view exists, if not create one
        if(rowView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            rowView = inflater.inflate(layoutResource, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) rowView.findViewById(R.id.name);
            viewHolder.address = (TextView) rowView.findViewById(R.id.address);
            rowView.setTag(viewHolder);
        }

        //If a does exist, recycle it by simply setting new data instead of creating a new view
        viewHolder = (ViewHolder) rowView.getTag();
        Result searchResult = searchResults.get(position);
        viewHolder.name.setText(searchResult.getName());
        viewHolder.address.setText(searchResult.getAddress());

        return rowView;
    }

    static class ViewHolder {
        public TextView name;
        public TextView address;
    }
}
