package com.example.aidan.contactmanager;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by Aidan on 3/31/2015.
 */
public class GetAllItemsListViewAdapter extends BaseAdapter {

    private JSONArray dataArray;
    private Activity activity;

    private static LayoutInflater layoutInflater = null;
    private String myLatitude;
    private String myLongitude;
    private float[] distance;


    public GetAllItemsListViewAdapter(JSONArray jsonArray, Activity a, String latitude, String longitude){

        this.dataArray = jsonArray;
        this.activity = a;
        myLatitude = latitude;
        myLongitude = longitude;

        layoutInflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public int getCount() {
        return this.dataArray.length();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListCell cell;
        //set up convert view if it is null
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.get_all_planes_cell, null);
            cell = new ListCell();
            cell.Title = (TextView) convertView.findViewById(R.id.title);
            cell.Price = (TextView) convertView.findViewById(R.id.price);
            cell.Distance = (TextView) convertView.findViewById(R.id.distance);
            cell.picture = (ImageView) convertView.findViewById(R.id.plane_pic);

            convertView.setTag(cell);
        }
        else{
            cell = (ListCell) convertView.getTag();
        }
        //change cell data

        try {
            JSONObject jsonObject = this.dataArray.getJSONObject(position);
            cell.Title.setText(jsonObject.getString("title"));
            cell.Price.setText(jsonObject.getString("price"));
            distance = new float[3];
            Location.distanceBetween(Double.parseDouble(myLatitude), Double.parseDouble(myLongitude), Double.parseDouble(jsonObject.getString("latitude")), Double.parseDouble(jsonObject.getString("longitude")), distance);
            cell.Distance.setText(Double.toString(distance[0]));
            //photo here
            cell.picture.setImageResource(R.drawable.ic_launcher);
        } catch (JSONException e){
            e.printStackTrace();
        }

        return convertView;
    }

    private class ListCell{
        private TextView Title;
        private TextView Price;
        private TextView Distance;

        private ImageView picture;
    }
}
