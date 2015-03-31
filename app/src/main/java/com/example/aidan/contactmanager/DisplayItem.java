package com.example.aidan.contactmanager;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//extends
public class DisplayItem extends ActionBarActivity {
    private int tailNo;
    private TextView make;
    private TextView model;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_item);

        this.tailNo = Integer.parseInt(getIntent().getStringExtra("TailNumber"));
        this.make = (TextView) this.findViewById(R.id.planeMake);
        this.model = (TextView) this.findViewById(R.id.planeModel);

        if(this.tailNo != 0){
            //we have an item
//            TextView tailNum = (TextView) findViewById(R.id.tail_no);
//            tailNum.setText(this.tailNo);
            new GetPlaneDetails().execute(new ApiConnector());
        }

    }

    private class GetPlaneDetails extends AsyncTask<ApiConnector, Long, JSONArray>
    {
        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            //it is executed on Background thread

            return params[0].GetPlaneDetails(tailNo);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            try{
                JSONObject plane = jsonArray.getJSONObject(0);
                make.setText(plane.getString("make"));
                model.setText(plane.getString("model"));
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

}