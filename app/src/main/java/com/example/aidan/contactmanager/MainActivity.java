package com.example.aidan.contactmanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "com.example.aidan.contactmanager.MESSAGE";

    private static final int VIEWITEM = 0, DELETE = 1;

    EditText title, price, keywords, description,phone;
//    ImageView itemImage;
//    List<PostedItem> PostedItems = new ArrayList<PostedItem>();
    ListView postedItemListView;
//    Uri imageUri = Uri.parse("android.resource://com.example.aidan.contactmanager/drawable/add_icon.png");
//    DatabaseHandler dbHandler;
//    int longClickedItemIndex;
//    ArrayAdapter<PostedItem> postedItemAdapter;

    Button upload;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;

    private JSONArray jsonArray;

    //putting these into adapter
    String mPhoneNumber;
    String itemTitle;
    String itemPrice;
    String itemKeywords;
    String itemDescription;
    String search;


    private Button changeImageButton;
    private static final int SELECT_PICTURE =1;
    private ImageView picture;
    //public List<NameValuePair> itemParams = new ArrayList<NameValuePair>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postedItemListView = (ListView) findViewById(R.id.itemView);
        new GetAllItemsTask().execute(new ApiConnector());

        //button to upload image?
        this.picture = (ImageView) this.findViewById(R.id.pic);
        this.changeImageButton = (Button) this.findViewById(R.id.changeImage);
        this.changeImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

            }
        });





        this.postedItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //get clicked item
                    JSONObject itemClicked = jsonArray.getJSONObject(position);

                    //send tail no
                    Intent showDetails = new Intent(getApplicationContext(), DisplayItem.class);
                    showDetails.putExtra("Title", itemClicked.getString("title"));
                    showDetails.putExtra("Price", itemClicked.getString("price"));
                    //showDetails.putExtra("Keywords", itemClicked.getString("keywords"));
                    showDetails.putExtra("Phone", itemClicked.getString("phoneNo"));
                    showDetails.putExtra("Description", itemClicked.getString("description"));

                    startActivity(showDetails);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneNumber = tMgr.getLine1Number();

        title = (EditText) findViewById(R.id.itemTitle); //title of selling post
        price = (EditText) findViewById(R.id.itemPrice);
        keywords = (EditText) findViewById(R.id.itemKeywords);
        description = (EditText) findViewById(R.id.itemDescription);
       // itemImage = (ImageView) findViewById(R.id.addImage);





        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("creator");
        tabSpec.setContent(R.id.sellTab);
        tabSpec.setIndicator("SELL");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("list");
        tabSpec.setContent(R.id.buyTab);
        tabSpec.setIndicator("BUY");
        tabHost.addTab(tabSpec);

        final Button searchBtn = (Button) findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //displays pop up with postedItem created


                new SearchItemsTask().execute(new ApiConnector());
            }
        });

        final Button addBtn = (Button) findViewById(R.id.addItem);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //displays pop up with postedItem created
                itemTitle = String.valueOf(title.getText());
                itemPrice = String.valueOf(price.getText());
                itemKeywords = String.valueOf(keywords.getText());
                itemDescription = description.getText().toString();

//                itemParams.add(new BasicNameValuePair("phone", mPhoneNumber));
//                itemParams.add(new BasicNameValuePair("title", itemTitle));
//                itemParams.add(new BasicNameValuePair("price", itemPrice));
//                itemParams.add(new BasicNameValuePair("keywords", itemKeywords));
//                itemParams.add(new BasicNameValuePair("description", itemDescription));
//                itemParams.add(new BasicNameValuePair("location", "Issaquah"));


                new InsertItemTask().execute(new ApiConnector());


//                if(!postedItemExists(postedItem)) {
//                    dbHandler.createPostedItem(postedItem);
//                    PostedItems.add(postedItem);
//                    postedItemAdapter.notifyDataSetChanged();
//                    Toast.makeText(getApplicationContext(), String.valueOf(title.getText()) +" has been added to your PostedItems!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                //Toast.makeText(getApplicationContext(), String.valueOf(title.getText()) + " already exists. Please use a different name.", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Added!!!", Toast.LENGTH_SHORT).show();

            }
        });
    }
//        title.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                addBtn.setEnabled(!String.valueOf(title.getText()).trim().isEmpty());//enabled if txt != nothing
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        itemImage.setOnClickListener(new View.OnClickListener(){
//            @Override
//        public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");//like sending email or something or pick images
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Item Image"), 1);
//            }
//        });

    //This deals with selecting picture from gallery
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode==SELECT_PICTURE){
                Uri selectedImageUri = data.getData();
                if(Build.VERSION.SDK_INT < 19){
                    String selectedImagePath = getPath(selectedImageUri);
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                    SetImage(bitmap);
                } else{
                    ParcelFileDescriptor parcelFileDescriptor;
                    try{
                        parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImageUri, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();
                        SetImage(image);
                    } catch(FileNotFoundException e){
                        e.printStackTrace();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void SetImage(Bitmap image){
        this.picture.setImageBitmap(image);

        //upload
        String imageData = encodeTobase64(image);

        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("image", imageData));
    }

    public static String encodeTobase64(Bitmap image){
        System.gc();

        if(image == null)return null;

        Bitmap imagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();

        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    public String getPath(Uri uri){
        if(uri==null){
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    public void setListAdapter(JSONArray jsonArray){
        this.jsonArray = jsonArray;

        GetAllItemsListViewAdapter temp = new GetAllItemsListViewAdapter(this.jsonArray, this);

        this.postedItemListView.setAdapter(temp);

    }

    private class InsertItemTask extends AsyncTask<ApiConnector, Long, JSONArray>
    {
        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            params[0].InsertItem(mPhoneNumber,itemTitle,  itemPrice, itemKeywords, itemDescription, "issaquah");
           //params[0].InsertItem(itemParams);
            return null;
        }
    }


    private class GetAllItemsTask extends AsyncTask<ApiConnector, Long, JSONArray>
    {
        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            //it is executed on Background thread

            return params[0].GetAllItems();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            setListAdapter(jsonArray);



        }
    }

    private class SearchItemsTask extends AsyncTask<ApiConnector, Long, JSONArray>
    {
        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            //it is executed on Background thread
            search = "k";
            return params[0].SearchItems(search);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            setListAdapter(jsonArray);

        }
    }




}
