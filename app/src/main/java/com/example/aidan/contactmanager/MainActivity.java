package com.example.aidan.contactmanager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public final static String EXTRA_MESSAGE = "com.example.aidan.contactmanager.MESSAGE";

    private static final int VIEWITEM = 0, DELETE = 1;

    EditText title, price, keywords, description;

    ListView postedItemListView;


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

    Button addBtn;



    String search;
    SearchView searchView;
    private SearchView mSearchView;
    private ListView keywordListView;
    private String text = "";
    private String[] str;
    private ArrayAdapter<String> adp;


    private Button changeImageButton;
    private static final int SELECT_PICTURE =1;
    private ImageView picture;


    private Button myProfileButton;


    private MultiAutoCompleteTextView autoText;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String myLatitude;
    private String myLongitude;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneNumber = tMgr.getLine1Number();



        //location
        buildGoogleApiClient();





        //get to the customers personal page
        this.myProfileButton = (Button) findViewById(R.id.myProfile);
        this.myProfileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MyProfile.class));
            }

        });



        postedItemListView = (ListView) findViewById(R.id.itemView);
        new GetAllItemsTask().execute(new ApiConnector());

        //button to upload image?
        this.picture = (ImageView) this.findViewById(R.id.pic);
//        this.changeImageButton = (Button) this.findViewById(R.id.changeImage);
//        this.changeImageButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
//
//            }
//        });


        mSearchView = (SearchView) findViewById(R.id.searchView);
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);

        setupSearchView();


        //This goes to the specific page of the clicked item
        this.postedItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //get clicked item
                    JSONObject itemClicked = jsonArray.getJSONObject(position);


                    Intent showDetails = new Intent(getApplicationContext(), DisplayItem.class);

                    showDetails.putExtra("Phone", itemClicked.getString("phoneNo"));

                    showDetails.putExtra("Time", itemClicked.getString("time"));



                    startActivity(showDetails);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });




        title = (EditText) findViewById(R.id.itemTitle); //title of selling post
        price = (EditText) findViewById(R.id.itemPrice);
        keywords = (EditText) findViewById(R.id.itemKeywords);
        keywords.setVisibility(View.GONE);
        description = (EditText) findViewById(R.id.itemDescription);
//        keywordListView = (ListView) findViewById(R.id.keywordListView);
//        keywordListView.setVisibility(View.GONE);
       // itemImage = (ImageView) findViewById(R.id.addImage);



        //autopopup keywords
//        keywords.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void afterTextChanged(Editable mEdit) {
//                keywordListView.setVisibility(View.VISIBLE);
//                text = mEdit.toString();
//                new findKeywordTask().execute(new ApiConnector());
//
//
//            }
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//        });




        autoText = (MultiAutoCompleteTextView) findViewById(R.id.autoText);
        autoText.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        autoText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
                text = mEdit.toString();
                new findKeywordTask().execute(new ApiConnector());


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });




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



        addBtn = (Button) findViewById(R.id.addItem);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //displays pop up with postedItem created
                itemTitle = String.valueOf(title.getText());
                itemPrice = String.valueOf(price.getText());
                itemKeywords = autoText.getText().toString();
                itemDescription = description.getText().toString();



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

    //location stuff
    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            myLatitude = String.valueOf(mLastLocation.getLatitude());
            myLongitude =  String.valueOf(mLastLocation.getLongitude());
        }
    }

    @Override
    protected void onStart() {

        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionSuspended(int i) {


    }


    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            System.out.println("ISEMPTY");
        } else {
            System.out.println("USELESS");
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        search = query;
        new SearchItemsTask().execute(new ApiConnector());
        return false;
    }



    //AsyncTasks for executing MySQL Queries

    public void setListAdapter(JSONArray jsonArray){
        this.jsonArray = jsonArray;

        GetAllItemsListViewAdapter temp = new GetAllItemsListViewAdapter(this.jsonArray, this, myLatitude, myLongitude);

        this.postedItemListView.setAdapter(temp);

    }

    public void setKeywordListAdapter(JSONArray jsonArray){
        this.jsonArray = jsonArray;

        if(keywords.getText().length()>0) {
            GetKeywordsListViewAdapter temp = new GetKeywordsListViewAdapter(this.jsonArray, this);
            keywordListView.setVisibility(View.VISIBLE);
            this.keywordListView.setAdapter(temp);
        }
        else{
            keywordListView.setVisibility(View.GONE);
        }




    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private class InsertItemTask extends AsyncTask<ApiConnector, Long, JSONArray>
    {
        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            params[0].InsertItem(mPhoneNumber,itemTitle,  itemPrice, itemKeywords, itemDescription, myLatitude, myLongitude);
           //params[0].InsertItem(itemParams);
            return null;
        }
    }

    private class findKeywordTask extends AsyncTask<ApiConnector, Long, JSONArray>
    {
        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            return params[0].FindKeyword(text);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {


            if (jsonArray != null) {
                str = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {


                        str[i] = jsonArray.getJSONObject(i).getString("keyword");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            //cheekyaf way of setting the adapter
            Context context = addBtn.getContext();
            adp =new ArrayAdapter<String>(context , android.R.layout.simple_dropdown_item_1line,str);
            autoText.setThreshold(1);
            autoText.setAdapter(adp);

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
            //search = "k";
            return params[0].SearchItems(search);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            setListAdapter(jsonArray);

        }
    }


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



}
