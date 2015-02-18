package com.example.aidan.contactmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aidan on 1/31/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_TITLE = "postedItemManager",
    TABLE_POSTEDITEM = "posteditem",
    KEY_ID = "id",
    KEY_TITLE = "title",
    KEY_PRICE = "price",
    KEY_KEYWORDS= "keywords",
    KEY_DESCRIPTION = "description",
    KEY_IMAGEURI = "imageUri";

    public DatabaseHandler(Context context){
        super(context, DATABASE_TITLE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_POSTEDITEM + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE+ " TEXT," + KEY_PRICE + " TEXT," + KEY_KEYWORDS+ " TEXT," + KEY_DESCRIPTION + " TEXT," + KEY_IMAGEURI + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTEDITEM);
        onCreate(db);
    }

    public void createPostedItem(PostedItem postedItem){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, postedItem.getTitle());
        values.put(KEY_PRICE, postedItem.getPrice());
        values.put(KEY_KEYWORDS, postedItem.getKeywords());
        values.put(KEY_DESCRIPTION, postedItem.getDescription());
        values.put(KEY_IMAGEURI, postedItem.getImageURI().toString());

        db.insert(TABLE_POSTEDITEM, null, values);
        db.close();

    }

    public PostedItem getPostedItem(int id) {//reading one postedItem
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_POSTEDITEM, new String[] {KEY_ID, KEY_TITLE, KEY_PRICE, KEY_KEYWORDS, KEY_DESCRIPTION, KEY_IMAGEURI}, KEY_ID + "=?", new String[] {String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        //get the key id
        PostedItem postedItem = new PostedItem(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), Uri.parse(cursor.getString(5)));
        db.close();
        cursor.close();
        return postedItem;
    }

    public void deletePostedItem(PostedItem postedItem){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_POSTEDITEM, KEY_ID + "=?", new String[] {String.valueOf(postedItem.getId())});
        db.close();
    }

    public int getPostedItemCount(){
        //EX: SELECT * FROM CONTACTS
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_POSTEDITEM, null);//second parameter is selection args (not needed)
        int count = cursor.getCount();
        db.close();
        cursor.close();


        return count;

    }

    public int updatePostedItem(PostedItem postedItem){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, postedItem.getTitle());
        values.put(KEY_PRICE, postedItem.getPrice());
        values.put(KEY_KEYWORDS, postedItem.getKeywords());
        values.put(KEY_DESCRIPTION, postedItem.getDescription());
        values.put(KEY_IMAGEURI, postedItem.getImageURI().toString());

        int rowsAffected = db.update(TABLE_POSTEDITEM, values, KEY_ID + "=?", new String[] {String.valueOf(postedItem.getId())});//how many rows were affected
        db.close();
        return rowsAffected;
    }

    public List<PostedItem> getAllPostedItems(){
        List<PostedItem> postedItems = new ArrayList<PostedItem>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_POSTEDITEM, null);

        if (cursor.moveToFirst()){
            //PostedItem postedItem;
            do {
                postedItems.add(new PostedItem(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), Uri.parse(cursor.getString(5)))); //add it to the list

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return postedItems;
    }
}
