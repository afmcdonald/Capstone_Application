package com.example.aidan.contactmanager;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOError;
import java.io.IOException;
import java.net.HttpRetryException;

/**
 * Created by Aidan on 3/31/2015.
 */
public class ApiConnector {


    public JSONArray GetAllItems(){
        String url = "http://ec2-52-4-203-65.compute-1.amazonaws.com/getAllItems.php";

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

    public JSONArray GetPlaneDetails(int tailNumber){
        String url = "http://ec2-52-4-203-65.compute-1.amazonaws.com/getAirplaneDetails.php?TailNumber="+tailNumber;

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

    public void InsertItem(String phone, String title, String price, String keywords, String description, String location){

        String url = "http://ec2-52-4-203-65.compute-1.amazonaws.com/createItem.php?Phone="+phone+"&Title="+title+"&Price="+price+"&Keywords="+keywords+"&Description="+description+"&Location="+location;

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
}
