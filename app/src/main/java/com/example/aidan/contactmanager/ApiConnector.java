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
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Aidan on 3/31/2015.
 */
public class ApiConnector {
    private String theUrl = "http://ec2-52-5-213-87.compute-1.amazonaws.com/";

    public JSONArray SearchItems(String search){
        String url =  theUrl +"searchTitleDescription.php?Search="+search;

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


    public JSONArray GetAllItems(){
        String url = theUrl +"getAllItems.php";

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


    public JSONArray GetMyItems(String phone){
        String url = theUrl +"getMyItems.php?Phone="+phone;

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

    public JSONArray GetLikedItems(String phone){
        String url = theUrl +"getLikedItems.php?Phone="+phone;

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

    public JSONArray GetItemDetails(String phone, String time){
        String finalTime = time.replace(" ", "%20");
        String url = theUrl + "getItemDetails.php?PhoneNumber="+phone+"&Time="+finalTime;

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



    public void InsertItem(String phone, String title, String price, String keywords, String description, String latitude, String longitude){
        title = title.replace(" ", "%20");
        price = price.replace(" ", "%20");
        description = description.replace(" ", "%20");
        keywords = keywords.replace(", ", "%20");

        String url = theUrl + "createItem.php?Phone="+phone+"&Title="+title+"&Price="+price+"&Keywords="+keywords+"&Description="+description+"&Latitude="+latitude+"&Longitude="+longitude;

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
    }

    public void InsertKeyword(String phone, String keyword){

        String url = theUrl+"createKeyword.php?Phone="+phone+"&Keyword="+keyword;

        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            httpClient.execute(httpGet);
        } catch(ClientProtocolException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void DeleteKeyword(String phone, String keyword){

        String url = theUrl+"deleteKeyword.php?Phone="+phone+"&Keyword="+keyword;

        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            httpClient.execute(httpGet);
        } catch(ClientProtocolException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public JSONArray FindKeyword(String searchKey){
        String finalKey = searchKey.replace(", ", "%20");
        String url =  theUrl +"searchKeyword.php?Search="+finalKey;

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

    public JSONArray FindMyKeywords(String phone){
        String url =  theUrl +"findMyKeywords.php?Phone="+phone;

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

    public void DeleteItem(String phone, String time){
        String finalTime = time.replace(" ", "%20");
        String url = theUrl +"deleteItem.php?PhoneNumber="+phone+"&Time="+finalTime;

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
        String url = theUrl + "uploadImage.php";

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
