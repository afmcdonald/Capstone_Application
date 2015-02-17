package com.example.aidan.contactmanager;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//extends
public class DisplayItem extends ActionBarActivity {


    List<Contact> Contacts = new ArrayList<Contact>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_item);


        Intent intent = getIntent();//get the data passed to this activity

        //String phoneNumber =

        Uri itemImage = Uri.parse(intent.getStringExtra("image"));
        ImageView image = (ImageView) findViewById(R.id.selectImage);
        image.setImageURI(itemImage);

        String itemTitle = intent.getStringExtra("title");
        TextView name = (TextView) findViewById(R.id.selectTitle);
        name.setText(itemTitle);

        String itemPrice = intent.getStringExtra("price");
        TextView price = (TextView) findViewById(R.id.selectPrice);
        price.setText(itemPrice);

        String itemKeywords = intent.getStringExtra("keywords");
        TextView keywords = (TextView) findViewById(R.id.selectKeywords);
        keywords.setText(itemKeywords);

        String itemDescription = intent.getStringExtra("description");
        TextView description = (TextView) findViewById(R.id.selectDescription);
        description.setText(itemDescription);

        Button callButton = (Button) findViewById(R.id.callButton);
        String phone = intent.getStringExtra("phone");
        final Uri phoneNumber = Uri.parse("tel:" + phone);


        callButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(phoneNumber);
                    startActivity(callIntent);
                } catch (ActivityNotFoundException activityException) {
                    Log.e("Calling a Phone Number", "Call failed", activityException);
                }
            }
        });

        Button textButton = (Button) findViewById(R.id.textButton);

        textButton.setOnClickListener((v) -> {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, "SMS text", null, null);
        });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_item, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    private class ContactListAdapter extends ArrayAdapter<Contact> { //Here's the list
//        public ContactListAdapter(){
//            super(DisplayItem.this, R.layout.listview_item, Contacts);
//        }
//
//        @Override
//        public View getView(int position, View view, ViewGroup parent){
//            if(view == null)//what does this inflater do?
//                view = getLayoutInflater().inflate(R.layout.activity_display_item, parent, false);
//
//            Contact currentContact = Contacts.get(position);
//
//            Intent mIntent = getIntent();
//            TextView name = (TextView) view.findViewById(R.id.selectTitle);
//            name.setText("LISTEN TO ME MANNNNNN");
//            // name.setText(mIntent.getIntExtra("tempId", 0));
//            TextView phone = (TextView) view.findViewById(R.id.selectPrice);
//            phone.setText(currentContact.getPrice());
//            TextView email = (TextView) view.findViewById(R.id.selectKeywords);
//            email.setText(currentContact.getKeywords());
//            TextView address = (TextView) view.findViewById(R.id.selectDescription);
//            address.setText(currentContact.getDescription());
//            ImageView ivContactImage = (ImageView) view.findViewById(R.id.selectImage);
//            ivContactImage.setImageURI(currentContact.getImageURI());
//
//            return view;
//        }
//    }
}
