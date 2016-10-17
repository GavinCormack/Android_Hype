package com.example.hype;

import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Gavin on 14-Oct-15.
 */
public class _JSONFunctions
{
    public static JSONArray getJSONfromURL(String url)
    {
        //InputStream inputStream = null;
        String result = "";

        HttpURLConnection urlConnection = null;

        try {
            URL urlOb = new URL(url);

            urlConnection = (HttpURLConnection) urlOb.openConnection();
            urlConnection.setRequestMethod("GET");
            String basicAuth = "Basic " + Base64.encodeToString("stuquilletyb@gmail.com:aaaaaa".getBytes(), Base64.NO_WRAP);
            urlConnection.setRequestProperty("Authorization", basicAuth);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            if(in != null){
                result = convertInputStreamToString(in);
                Log.i("STU","STU "+result);
            }
            else{
                result = "Did not work!";
                Log.i("STU","STU "+result);
            }
        }
        catch (MalformedURLException e){
            Log.i("STU","STU ~~~~~ Malformed");
        }
        catch (IOException i){


            Log.i("STU","STU ~~~~~ IO" + i);
        }
        catch (JSONException j){

            Log.i("STU","STU ~~~~~ JSON");
        }
            finally {
                urlConnection.disconnect();
            Log.i("STU", "STU ~~~~~ DISCONNECT");

        }






        JSONArray jArray = null;
        try {
            jArray = new JSONArray(result);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jArray;
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException, JSONException{

        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        return result;

    }
}
