package com.example.aidan.contactmanager;

import android.app.Activity;
import android.content.Context;
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


    public GetAllItemsListViewAdapter(JSONArray jsonArray, Activity a){

        this.dataArray = jsonArray;
        this.activity = a;

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
            cell.TailNo = (TextView) convertView.findViewById(R.id.tail_no);
            cell.Make = (TextView) convertView.findViewById(R.id.make);

            cell.picture = (ImageView) convertView.findViewById(R.id.plane_pic);

            convertView.setTag(cell);
        }
        else{
            cell = (ListCell) convertView.getTag();
        }
        //change cell data

        try {
            JSONObject jsonObject = this.dataArray.getJSONObject(position);
            cell.TailNo.setText(jsonObject.getString("title"));
            cell.Make.setText(" "+jsonObject.getString("description"));

            //photo here
            cell.picture.setImageResource(R.drawable.ic_launcher);
        } catch (JSONException e){
            e.printStackTrace();
        }

        return convertView;
    }

    private class ListCell{
        private TextView TailNo;
        private TextView Make;

        private ImageView picture;
    }
}
