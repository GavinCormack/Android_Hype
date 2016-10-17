package com.example.hype;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Gavin on 22/09/2015.
 */

public class Fragment_Newsfeed extends Fragment implements View.OnClickListener
{
    //declare xml elements here
    Button fragment_Newsfeed_Button_Camera;
    ListView fragment_Newsfeed_ListView_Newsfeed;

    //instance of the interface for this fragment
    Fragment_Newsfeed_ButtonClickListener fragment_Newsfeed_ButtonClickListener;

    //reference to the view of fragment
    View view;



    //Parsing JSON
    JSONObject jsonobject;
    JSONArray jsonarray;

    //Arraylist of posts
    ArrayList<HashMap<String, String>> allPosts;

    //global variables
    static ListAdapter adapter;
    String imagePath;
    Bitmap postImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Creates a reference to the xml layout since it is a fragment and not an activity.
        view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        init();

        return view;
    }

    //initialise global variables where possible
    public void init()
    {
        //initialise the ImageButton and onClickListener
        fragment_Newsfeed_Button_Camera = (Button) view.findViewById(R.id.Fragment_Newsfeed_Button_Camera);
        fragment_Newsfeed_Button_Camera.setOnClickListener(this);

        //initialise the ListView
        fragment_Newsfeed_ListView_Newsfeed = (ListView) view.findViewById(R.id.Fragment_Newsfeed_ListView_Newsfeed);

        //set the symbol's font
        setSymbolsAndFont();

        ((Activity_Main) getContext()).new FetchNewsfeedTask().execute();

        //populateList();

    }

    //sets the font of the symbols so they work
    public void setSymbolsAndFont()
    {
        fragment_Newsfeed_Button_Camera.setTypeface(((Activity_Main) getContext()).hypeFont);
    }

    //populates listview
    public void populateList()
    {
        // Pass the results into ListViewAdapter.java
        adapter = new ListView_Newsfeed_Adapter(getContext(), ((Activity_Main) getContext()).newsfeedPosts);
        // Set the adapter to the ListView
        fragment_Newsfeed_ListView_Newsfeed.setAdapter(adapter);

    }

    //when the fragment is visible on screen
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        ((Activity_Main) context).showTabbar();

        try
        {
            //initialising fragment interface
            fragment_Newsfeed_ButtonClickListener = (Fragment_Newsfeed_ButtonClickListener) context;
        }
        catch(Exception ex)
        {}
    }

    @Override
    public void onResume()
    {
        super.onResume();

        ((Activity_Main) getContext()).showTabbar();
    }

    //button clicks
    @Override
    public void onClick(View v)
    {
        //camera button was clicked
        if(v.getId() == R.id.Fragment_Newsfeed_Button_Camera)
        {
            //TODO comment this out so it can act as a refresh button for now
            fragment_Newsfeed_ButtonClickListener.fragment_Newsfeed_CameraButtonClick();
            //new FetchNewsfeedTask().execute();
        }
    }

    //interface that the Main Activity implements to talk to fragment
    public interface Fragment_Newsfeed_ButtonClickListener
    {
        //method for each button click
        void fragment_Newsfeed_CameraButtonClick();
    }



    //AsyncTask for getting newsfeed
    public class FetchNewsfeedTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            // Create an array
            allPosts = new ArrayList<HashMap<String, String>>();

            // Retrieve JSON Objects from the given URL address
            jsonarray = _JSONFunctions.getJSONfromURL("http://hype.cloudapp.net:8080/api/feed/newsfeed?mode=1");

            if(jsonarray == null)
            {
                Log.i("STU", "~~~~~~~~~~~~~~~ITS NULL LADS!!!!~~~~~~~~~~");
            }

            try
            {
                // Locate the array name in JSON
                //jsonarray = jsonobject.getJSONArray("Posts");

                for (int i = 0; i < jsonarray.length(); i++)
                {
                    HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);

                    map.put(((Activity_Main) getContext()).json_Post_PostID, jsonobject.getString("PostID"));
                    map.put(((Activity_Main) getContext()).json_Post_Caption, jsonobject.getString("Body"));
                    map.put(((Activity_Main) getContext()).json_Post_Username, jsonobject.getString("Owner"));
                    map.put(((Activity_Main) getContext()).json_Post_ImagePath, jsonobject.getString("Image"));
                    map.put(((Activity_Main) getContext()).json_Post_Tags, jsonobject.getString("Tags"));
                    map.put(((Activity_Main) getContext()).json_Post_ViewCount, jsonobject.getString("Views"));
                    map.put(((Activity_Main) getContext()).json_Post_ViewedByUser, jsonobject.getString("IsViewedByUser"));
                    map.put(((Activity_Main) getContext()).json_Post_LikeCount, jsonobject.getString("Likes"));
                    map.put(((Activity_Main) getContext()).json_Post_LikedByUser, jsonobject.getString("UserLiked"));
                    map.put(((Activity_Main) getContext()).json_Post_ReactionCount, jsonobject.getString("Comments"));

                    // Set the JSON Objects into the array
                    allPosts.add(map);
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void args) {

            populateList();

        }

    }

    //AsyncTask for getting newsfeed
    public class FetchImageTask extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params)
        {
            String urlS = "http://hype.cloudapp.net:8080" + params[0];

            URL url;
            Bitmap image = null;
            try {
                url = new URL(urlS);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            Log.i("STU", "DoInBackground image = "+ image.toString());

            postImage = image;

            return null;
        }

        @Override
        protected void onPostExecute(Void args)
        {
            ((Activity_Main) getContext()).setPostImage(postImage);
            ((Activity_Main) getContext()).fragment_ShowImage.fragment_ShowImage_ImageView_Image.postInvalidate();
        }

    }

}
