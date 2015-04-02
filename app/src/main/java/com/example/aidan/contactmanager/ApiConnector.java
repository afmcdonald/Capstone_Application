package com.example.aidan.contactmanager;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOError;
import java.io.IOException;
import java.net.HttpRetryException;
import java.util.List;

/**
 * Created by Aidan on 3/31/2015.
 */
public class ApiConnector {


    public JSONArray GetAllItems(){
        String url = "ec2-52-5-107-228.compute-1.amazonaws.com/getAllItems.php";

        //Get HttpResponse Object from url
        //Get HttpEntitiy from Http response object

        HttpEntity httpEntity = null;

        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);

            httpEntity = httpResponse.getEntity();
        } catch(ClientProtocolException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }


        //Convert HttpEntity into JSON Array
        JSONArray jsonArray = null;

        if (httpEntity != null){
            try{
                String entityResponse = EntityUtils.toString(httpEntity);
                Log.e("Entity Response : ", entityResponse);

                jsonArray = new JSONArray(entityResponse);
            } catch(JSONException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }
        }

        return jsonArray;
    }

    public JSONArray GetItemDetails(String phone, String description){
        String url = "ec2-52-5-107-228.compute-1.amazonaws.com/getItemDetails.php?PhoneNumber="+phone+"&Description="+description;

        //Get HttpResponse Object from url
        //Get HttpEntitiy from Http response object

        HttpEntity httpEntity = null;

        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);

            httpEntity = httpResponse.getEntity();
        } catch(ClientProtocolException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }


        //Convert HttpEntity into JSON Array
        JSONArray jsonArray = null;

        if (httpEntity != null){
            try{
                String entityResponse = EntityUtils.toString(httpEntity);
                Log.e("Entity Response : ", entityResponse);

                jsonArray = new JSONArray(entityResponse);
            } catch(JSONException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }
        }

        return jsonArray;
    }

//    public void InsertItem(List<NameValuePair> params){
//
//        String url = "http://ec2-52-4-203-65.compute-1.amazonaws.com/postItem.php";
//
//        //Get HttpResponse Object from url
//        //Get HttpEntitiy from Http response object
//
//        HttpEntity httpEntity = null;
//
//        try{
//            DefaultHttpClient httpClient = new DefaultHttpClient();
//
//            HttpPost httpPost = new HttpPost(url);
//            httpPost.setEntity(new UrlEncodedFormEntity(params));
//            //there is no response so i think...
//            httpClient.execute(httpPost);
//            //HttpResponse httpResponse = httpClient.execute(httpGet);
//
//            //httpEntity = httpResponse.getEntity();
//        } catch(ClientProtocolException e){
//            e.printStackTrace();
//        } catch(IOException e){
//            e.printStackTrace();
//        }
//
//
////
//    }

    public void InsertItem(String phone, String title, String price, String keywords, String description, String location){

        String url = "ec2-52-5-107-228.compute-1.amazonaws.com/createItem.php?Phone="+phone+"&Title="+title+"&Price="+price+"&Keywords="+keywords+"&Description="+description+"&Location="+location;

        //Get HttpResponse Object from url
        //Get HttpEntitiy from Http response object

        HttpEntity httpEntity = null;

        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            //there is no response so i think...
            httpClient.execute(httpGet);
            //HttpResponse httpResponse = httpClient.execute(httpGet);

            //httpEntity = httpResponse.getEntity();
        } catch(ClientProtocolException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }


//
    }

    public Boolean uploadImageToServer(List<NameValuePair> params){
        String url = "ec2-52-5-107-228.compute-1.amazonaws.com/uploadImage.php";

        //Get HttpResponse Object from url
        //Get HttpEntitiy from Http response object

        HttpEntity httpEntity = null;

        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse httpResponse = httpClient.execute(httpPost);

            httpEntity = httpResponse.getEntity();

            String entityResponse = EntityUtils.toString(httpEntity);

            Log.e("Entity Response : ", entityResponse);
            return true;
        } catch(ClientProtocolException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }
}
