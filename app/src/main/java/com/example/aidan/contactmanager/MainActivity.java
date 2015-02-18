package com.example.aidan.contactmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
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


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "com.example.aidan.contactmanager.MESSAGE";

    private static final int VIEWITEM = 0, DELETE = 1;

    EditText title, price, keywords, description;
    ImageView itemImage;
    List<PostedItem> PostedItems = new ArrayList<PostedItem>();
    ListView postedItemListView;
    Uri imageUri = Uri.parse("android.resource://com.example.aidan.contactmanager/drawable/add_icon.png");
    DatabaseHandler dbHandler;
    int longClickedItemIndex;
    ArrayAdapter<PostedItem> postedItemAdapter;

    Button upload;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = (EditText) findViewById(R.id.itemTitle); //title of selling post
        price = (EditText) findViewById(R.id.itemPrice);
        keywords = (EditText) findViewById(R.id.itemKeywords);
        description = (EditText) findViewById(R.id.itemDescription);
        postedItemListView = (ListView) findViewById(R.id.itemView);
        itemImage = (ImageView) findViewById(R.id.addImage);
        dbHandler = new DatabaseHandler(getApplicationContext());



        registerForContextMenu(postedItemListView);

        postedItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                longClickedItemIndex = position;
                PostedItem viewPostedItem = PostedItems.get(longClickedItemIndex);

                String title = viewPostedItem.getTitle();
                String price = viewPostedItem.getPrice();
                String keywords = viewPostedItem.getKeywords();
                String description = viewPostedItem.getDescription();

                Uri image = viewPostedItem.getImageURI();

                Intent intent = new Intent(view.getContext(),DisplayItem.class);
                TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String mPhoneNumber = tMgr.getLine1Number();
                intent.putExtra("image", image.toString());
                intent.putExtra("title", title);
                intent.putExtra("price", price);
                intent.putExtra("keywords", keywords);
                intent.putExtra("description", description);
                intent.putExtra("phone", mPhoneNumber);

                startActivity(intent);
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

        final Button addBtn = (Button) findViewById(R.id.btnAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //displays pop up with postedItem created
                PostedItem postedItem = new PostedItem(dbHandler.getPostedItemCount(), String.valueOf(title.getText()), String.valueOf(price.getText()),String.valueOf(keywords.getText()), String.valueOf(description.getText()), imageUri );
                if(!postedItemExists(postedItem)) {
                    dbHandler.createPostedItem(postedItem);
                    PostedItems.add(postedItem);
                    postedItemAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), String.valueOf(title.getText()) +" has been added to your PostedItems!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), String.valueOf(title.getText()) + " already exists. Please use a different name.", Toast.LENGTH_SHORT).show();
            }
        });

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addBtn.setEnabled(!String.valueOf(title.getText()).trim().isEmpty());//enabled if txt != nothing
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        itemImage.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");//like sending email or something or pick images
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Item Image"), 1);
            }
        });


        //darrens stuff
        upload = (Button) findViewById(R.id.upload);
        this.imageView = (ImageView) this.findViewById(R.id.image);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }

        });




        if (dbHandler.getPostedItemCount()!= 0)
            PostedItems.addAll(dbHandler.getAllPostedItems());


        populateList();
    }

    public void openItem(View view){
        Intent intent = new Intent(this,DisplayItem.class);
        //intent.putExtra("tempId", R.id.buyId);
        startActivity(intent);
    }


    private boolean postedItemExists(PostedItem postedItem){
        String name = postedItem.getTitle();
        int contactCount = PostedItems.size();

        for (int i = 0; i < contactCount; i++){
            if(name.compareToIgnoreCase(PostedItems.get(i).getTitle())==0){//0 if match
                return true;
            }
        }
        return false;
    }

    public void onActivityResult(int reqCode, int resCode, Intent data){
        if (resCode== RESULT_OK){
            if (reqCode == 1){
                imageUri = (Uri) data.getData();
                itemImage.setImageURI(data.getData());
            }
            if (reqCode == CAMERA_REQUEST){
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                itemImage.setImageBitmap(photo);
            }

        }
    }

    private void populateList() {
        postedItemAdapter = new PostedItemListAdapter();
        postedItemListView.setAdapter(postedItemAdapter);//what is this adapter?
    }


    private class PostedItemListAdapter extends ArrayAdapter<PostedItem>{ //Here's the list
        public PostedItemListAdapter(){
            super(MainActivity.this, R.layout.listview_item, PostedItems);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            if(view == null)//what does this inflater do?
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            PostedItem currentpostedItem = PostedItems.get(position);

            //TextView tempId = (TextView) view.findViewById(R.id.buyId);
            //tempId.setText(Integer.toString(currentContact.getId()));
            TextView name = (TextView) view.findViewById(R.id.buyTitle);
            name.setText(currentpostedItem.getTitle());
            TextView phone = (TextView) view.findViewById(R.id.buyPrice);
            phone.setText(currentpostedItem.getPrice());
            TextView email = (TextView) view.findViewById(R.id.buyKeywords);
            email.setText(currentpostedItem.getKeywords());
            TextView address = (TextView) view.findViewById(R.id.buyDescription);
            address.setText(currentpostedItem.getDescription());
            ImageView ivPostedItemImage = (ImageView) view.findViewById(R.id.ivContactImage);
            ivPostedItemImage.setImageURI(currentpostedItem.getImageURI());

            return view;
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    //    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
//        super.onCreateContextMenu(menu, view, menuInfo);
//
//        menu.setHeaderIcon(R.drawable.pencil_icon);
//        menu.setHeaderTitle("Item Options");
//        menu.add(Menu.NONE, VIEWITEM, menu.NONE, "View Item");
//        menu.add(Menu.NONE, DELETE, menu.NONE, "Delete Item");
//    }



//    public boolean onContextItemSelected(MenuItem item){
//        switch(item.getItemId()){
//            case VIEWITEM:
//                Item viewPostedItem = PostedItems.get(longClickedItemIndex);
//
//                String title = viewPostedItem.getTitle();
//                String price = viewPostedItem.getPrice();
//                String keywords = viewPostedItem.getKeywords();
//                String description = viewPostedItem.getDescription();
//
//
//                Intent intent = new Intent(this,DisplayItem.class);
//                TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                String mPhoneNumber = tMgr.getLine1Number();
//                intent.putExtra("image", image.toString());
//                intent.putExtra("title", title);
//                intent.putExtra("price", price);
//                intent.putExtra("keywords", keywords);
//                intent.putExtra("description", description);
//                intent.putExtra("phone", mPhoneNumber);
//
//                startActivity(intent);
//
//                break;
//            case DELETE:
//                dbHandler.deleteContact(PostedItems.get(longClickedItemIndex));
//                PostedItems.remove(longClickedItemIndex);
//                contactAdapter.notifyDataSetChanged();
//                break;
//        }
//
//        return super.onContextItemSelected(item);
//    }

}
