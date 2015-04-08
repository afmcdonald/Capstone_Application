package com.example.aidan.contactmanager;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;

/**
 * Created by Aidan on 4/7/2015.
 */
public class Interests extends ActionBarActivity {
    private EditText interest;
    private Button addInterest;
    private String keyword;
    private String mPhoneNumber;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interests);

        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneNumber = tMgr.getLine1Number();

        interest = (EditText) findViewById(R.id.myInterest);

        addInterest = (Button) findViewById(R.id.addInterest);
        addInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = interest.getText().toString();
                new InsertKeywordTask().execute(new ApiConnector());
                startActivity(new Intent(getApplicationContext(), MyProfile.class));
            }
        });






    }

    private class InsertKeywordTask extends AsyncTask<ApiConnector, Long, JSONArray>
    {
        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            params[0].InsertKeyword(mPhoneNumber, keyword);
            //params[0].InsertItem(itemParams);
            return null;
        }
    }
}
