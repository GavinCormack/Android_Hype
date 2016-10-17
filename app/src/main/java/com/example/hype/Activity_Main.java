package com.example.hype;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Activity_Main extends AppCompatActivity implements Fragment_Newsfeed.Fragment_Newsfeed_ButtonClickListener,
                                                                Fragment_Messages.Fragment_Messages_ButtonClickListener,
                                                                Fragment_ShowImage.Fragment_ShowImage_ButtonClickListener,
                                                                Fragment_Camera.Fragment_Camera_ButtonClickListener,
                                                                Fragment_Canvas.Fragment_Canvas_ButtonClickListener,
                                                                Fragment_Caption.Fragment_Caption_ButtonClickListener,
                                                                Fragment_Conversation.Fragment_Conversation_ButtonClickListener,
                                                                View.OnClickListener
{

    //declare xml elements here
    Button activity_Main_Button_Newsfeed,
            activity_Main_Button_Messages,
            activity_Main_Button_Discover,
            activity_Main_Button_More;
    LinearLayout activity_Main_LinearLayout_TabBar;

    //fragment manager, fragment transaction and individual fragments
    FragmentManager fragmentManager; //allows you to interact with fragments
    FragmentTransaction fragmentTransaction; //allows you to switch between fragments
    Fragment_Newsfeed fragment_Newsfeed;
    Fragment_Messages fragment_Messages;
    Fragment_Discover fragment_Discover;
    Fragment_Settings fragment_More;
    Fragment_ShowImage fragment_ShowImage;
    Fragment_Camera fragment_Camera;
    Fragment_Canvas fragment_Canvas;
    Fragment_Caption fragment_Caption;
    Fragment_Conversation fragment_Conversation;
    Fragment_Settings fragment_Settings;

    //intent for credentials activity
    Intent activityCredentials;


    //JSON Keys
    static String json_Post_PostID = "PostID",
                    json_Post_UserID = "UserID",
                    json_Post_Username = "Username",
                    json_Post_Caption = "Caption",
                    json_Post_Expire = "Expire",
                    json_Post_Tags = "Tags",
                    json_Post_ImagePath = "ImagePath",
                    json_Post_LikeCount = "LikeCount",
                    json_Post_ReactionCount = "ReactionCount",
                    json_Post_ViewCount = "ViewCount",
                    json_Post_LikedByUser = "LikedByUser",
                    json_Post_ViewedByUser = "ViewedByUser",
                    json_Post_ReactedByUser = "ReactedByUser";

    //Newsfeed variables
    String postImagePath; //path to image in post
    Bitmap postImage; //image in post

    //Newsfeed_Adapter variables
    ArrayList<HashMap<String, String>> newsfeedPosts;
    JSONObject jsonobject;
    JSONArray jsonarray;

    //Camera variables
    Bitmap picTaken; //image taken by camera

    //Canvas variables
    Bitmap overlayedImage; //the image and canvas merged

    //Caption variables
    String postCaption;



    //Global variables
    List<Fragment> fragmentHistoryList = new ArrayList<>(); //to keep track of fragments for back navigation
    Fragment fragmentHistoryPrevious; //keeps track of the previous fragment for back navigation tabBar selection colours
    boolean paletteIsVisible; //if the palette of colours on the canvas fragment is visible

    public static Typeface hypeFont; //the font for the symbols

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    public void init()
    {
        fullscreenMode();

        //initialise the Tab and its Buttons
        activity_Main_Button_Messages = (Button) findViewById(R.id.Activity_Main_Button_Messages);
        activity_Main_Button_Messages.setOnClickListener(this);
        activity_Main_Button_Newsfeed = (Button) findViewById(R.id.Activity_Main_Button_Newsfeed);
        activity_Main_Button_Newsfeed.setOnClickListener(this);
        activity_Main_Button_Discover = (Button) findViewById(R.id.Activity_Main_Button_Discover);
        activity_Main_Button_Discover.setOnClickListener(this);
        activity_Main_Button_More = (Button) findViewById(R.id.Activity_Main_Button_More);
        activity_Main_Button_More.setOnClickListener(this);
        activity_Main_LinearLayout_TabBar = (LinearLayout) findViewById(R.id.Activity_Main_LinearLayout_TabBar);

        //set the symbol's font
        setSymbolsAndFont();

        //initialise the intents
        activityCredentials = new Intent(this, Activity_Credentials.class);

        //fragment manager, fragment transaction and individual fragments
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragment_Newsfeed = new Fragment_Newsfeed();
        fragment_Messages = new Fragment_Messages();
        fragment_Discover = new Fragment_Discover();
        fragment_Settings = new Fragment_Settings();
        fragment_ShowImage = new Fragment_ShowImage();
        fragment_Camera = new Fragment_Camera();
        fragment_Canvas = new Fragment_Canvas();
        fragment_Caption = new Fragment_Caption();
        fragment_Conversation = new Fragment_Conversation();

        //setting the initial fragment as login fragment and commit transaction
        fragmentTransaction.add(R.id.Activity_Main_LinearLayout_FragmentContainer, fragment_Newsfeed);
        fragmentTransaction.commit();

        //add newsfeed fragment to FragmentHistoryList because it is the default
        fragmentHistoryList.add(fragment_Newsfeed);

        //newsfeed button is selected because it is default
        activity_Main_Button_Newsfeed.setSelected(true);
        activity_Main_Button_Newsfeed.setTextColor(getResources().getColor(R.color.hype_red));


        //set the images to something by default so they aren't null
        postImage = BitmapFactory.decodeResource(getResources(), R.drawable.mona_lisa);
        picTaken = BitmapFactory.decodeResource(getResources(), R.drawable.mona_lisa);

        paletteIsVisible = false; //not visible by default

    }

    //makes the activity fullscreen by removing the StatusBar and ActionBar
    public void fullscreenMode()
    {
        View decorView = getWindow().getDecorView();
        // Hide the StatusBar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // StatusBar is hidden, now hide ActionBar
        getSupportActionBar().hide();
    }

    //sets the font of the symbols so they work
    public void setSymbolsAndFont()
    {
        hypeFont = Typeface.createFromAsset(getAssets(), "fonts/gav.ttf");

        activity_Main_Button_Messages.setTypeface(hypeFont);
        activity_Main_Button_Newsfeed.setTypeface(hypeFont);
        activity_Main_Button_Discover.setTypeface(hypeFont);
        activity_Main_Button_More.setTypeface(hypeFont);
    }


    //Changes the background colour of the buttons on the tabBar to mimic selecting button
    public void tabBarSelectionColours(View v)
    {
        //dull out all icons
        activity_Main_Button_Newsfeed.setSelected(false);
        activity_Main_Button_Newsfeed.setTextColor(getResources().getColor(R.color.hype_grey));
        activity_Main_Button_Messages.setSelected(false);
        activity_Main_Button_Messages.setTextColor(getResources().getColor(R.color.hype_grey));
        activity_Main_Button_Discover.setSelected(false);
        activity_Main_Button_Discover.setTextColor(getResources().getColor(R.color.hype_grey));
        activity_Main_Button_More.setSelected(false);
        activity_Main_Button_More.setTextColor(getResources().getColor(R.color.hype_grey));

        //change the icon color of selected
        switch (v.getId())
        {
            case R.id.Activity_Main_Button_Newsfeed:
                activity_Main_Button_Newsfeed.setTextColor(getResources().getColor(R.color.hype_red));
                break;

            case R.id.Activity_Main_Button_Messages:
                activity_Main_Button_Messages.setTextColor(getResources().getColor(R.color.hype_blue));
                break;

            case R.id.Activity_Main_Button_Discover:
                activity_Main_Button_Discover.setTextColor(getResources().getColor(R.color.hype_yellow));
                break;

            case R.id.Activity_Main_Button_More:
                activity_Main_Button_More.setTextColor(getResources().getColor(R.color.hype_green));
                break;

        }

        v.setSelected(true);
    }

    //Changes the background colour of the buttons on the tabBar when back button is being pressed (back navigation)
    public void tabBarSelectionColoursBack()
    {
        if(fragmentHistoryList.size() > 1)
        {
            fragmentHistoryList.remove(fragmentHistoryList.size() - 1); //remove the last fragment / entry

            fragmentHistoryPrevious = fragmentHistoryList.get(fragmentHistoryList.size()-1); //set previous to the next fragment / entry

            //change button colour on tabBar depending on current fragment
            if(fragmentHistoryPrevious.toString().contains("Newsfeed"))
            {
                tabBarSelectionColours(activity_Main_Button_Newsfeed);
            }
            else if(fragmentHistoryPrevious.toString().contains("Messages"))
            {
                tabBarSelectionColours(activity_Main_Button_Messages);
            }
            else if(fragmentHistoryPrevious.toString().contains("Discover"))
            {
                tabBarSelectionColours(activity_Main_Button_Discover);
            }
            else if(fragmentHistoryPrevious.toString().contains("More"))
            {
                tabBarSelectionColours(activity_Main_Button_More);
            }
        }
    }

    //replace current fragment with the button selected on the tabBar
    public void changeToFragment(Fragment f)
    {
        //change fragment
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Activity_Main_LinearLayout_FragmentContainer, f);
        fragmentTransaction.addToBackStack(null); //add previous fragment to backstack
        fragmentTransaction.commit();

        //add new fragment to list for back navigation
        fragmentHistoryList.add(f);
        fragmentHistoryPrevious = fragmentHistoryList.get(fragmentHistoryList.size()-1);
    }

    //Hides the tabbar
    public void hideTabbar()
    {
        activity_Main_LinearLayout_TabBar.setVisibility(View.GONE);
    }

    //shows the tabbar
    public void showTabbar()
    {
        activity_Main_LinearLayout_TabBar.setVisibility(View.VISIBLE);
    }


    //fetchs the image of the given path from the server
    public void fetchPostImage()
    {
        new FetchImageTask().execute(postImagePath);
    }

    /***********************************************************************************************************
     *
     * Sets and Gets of variables
     *
     ***********************************************************************************************************/

    //sets path to post image
    public void setPostImagePath(String path)
    {
        postImagePath = path;
    }

    //returns path to post image
    public String getPostImagePath()
    {
        return postImagePath;
    }

    //sets post image
    public void setPostImage(Bitmap image)
    {
        postImage = image;
    }

    //returns post image
    public Bitmap getPostImage()
    {
        return postImage;
    }

    //sets the variable to the image that was just taken by the camera
    public void setPicTaken(Bitmap image)
    {
        picTaken = image;
    }

    //returns the image that was just taken by the camera
    public Bitmap getPicTaken()
    {
        return picTaken;
    }

    //sets path to post image
    public void setOverlayedImage(Bitmap image)
    {
        overlayedImage = image;
    }

    //returns path to post image
    public Bitmap getOverlayedImage()
    {
        return overlayedImage;
    }




    /***********************************************************************************************************
     *
     * Android OnClick method for button clicks
     *
     ***********************************************************************************************************/

    //button clicks on the TabBar
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.Activity_Main_Button_Newsfeed:
                if(!v.isSelected())
                {
                    changeToFragment(fragment_Newsfeed); //display fragment
                    tabBarSelectionColours(v); //change button background colour so it looks selected
                    activity_Main_Button_Newsfeed.setTextColor(getResources().getColor(R.color.hype_red));
                }
                break;

            case R.id.Activity_Main_Button_Messages:
                if(!v.isSelected())
                {
                    changeToFragment(fragment_Messages); //display fragment
                    tabBarSelectionColours(v); //change button background colour so it looks selected
                    activity_Main_Button_Messages.setTextColor(getResources().getColor(R.color.hype_blue));
                }
                break;

            case R.id.Activity_Main_Button_Discover:
                if(!v.isSelected())
                {
                    changeToFragment(fragment_Discover); //display fragment
                    tabBarSelectionColours(v); //change button background colour so it looks selected
                    activity_Main_Button_Discover.setTextColor(getResources().getColor(R.color.hype_yellow));
                }
                break;

            case R.id.Activity_Main_Button_More:
                if(!v.isSelected())
                {
                    changeToFragment(fragment_More); //display
                    tabBarSelectionColours(v); //change button background colour so it looks selected
                    activity_Main_Button_More.setTextColor(getResources().getColor(R.color.hype_green));
                }
                break;

        }
    }





    /***********************************************************************************************************
     *
     * Android Methods to deal with lifecycle or hardware button clicks
     *
     ***********************************************************************************************************/

    //back button click
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        tabBarSelectionColoursBack(); //mimics selection of tabBar while pressing the back button (back navigation)
    }





    /***********************************************************************************************************
     *
     * Methods to deal with Button clicks on the Newsfeed Fragment via Interface
     *
     ***********************************************************************************************************/

    //camera button click
    @Override
    public void fragment_Newsfeed_CameraButtonClick()
    {
        /**
         * replace with camera fragment
         * hide the tab bar for fullscreen effect
         */

        changeToFragment(fragment_Camera);
    }





    /***********************************************************************************************************
     *
     * Methods to deal with Button clicks on the Messages Fragment via Interface
     *
     ***********************************************************************************************************/

    //createNew button click
    @Override
    public void fragment_Messages_CreateNewButtonClick()
    {
        /**
         * replace with newConversation fragment
         */
    }






    /***********************************************************************************************************
     *
     * Methods to deal with Button clicks on the ShowImage Fragment via Interface
     *
     ***********************************************************************************************************/

    //hideImage button click
    @Override
    public void fragment_ShowImage_ImageButtonClick()
    {
        onBackPressed();
        showTabbar();
    }





    /***********************************************************************************************************
     *
     * Methods to deal with Button clicks on the Camera Fragment via Interface
     *
     ***********************************************************************************************************/

    //back button click
    @Override
    public void fragment_Camera_BackButtonClick()
    {

    }

    //rotate button click
    @Override
    public void fragment_Camera_RotateButtonClick()
    {

    }

    //ok button click
    @Override
    public void fragment_Camera_OkButtonClick()
    {
        changeToFragment(fragment_Canvas);
    }





    /***********************************************************************************************************
     *
     * Methods to deal with Button clicks on the Canvas Fragment via Interface
     *
     ***********************************************************************************************************/

    //back button click
    @Override
    public void fragment_Canvas_BackButtonClick()
    {

    }

    //palette button click
    @Override
    public void fragment_Canvas_PaletteButtonClick()
    {
        if(paletteIsVisible)
        {
            fragment_Canvas.fragment_Canvas_LinearLayout_Dropdown.setVisibility(View.GONE); //hide colours
            paletteIsVisible = false;
        }
        else
        {
            fragment_Canvas.fragment_Canvas_LinearLayout_Dropdown.setVisibility(View.VISIBLE); //show colours
            paletteIsVisible = true;
        }
    }

    //ok button click
    @Override
    public void fragment_Canvas_OkButtonClick()
    {
        changeToFragment(fragment_Caption);
    }

    //blue button click
    @Override
    public void fragment_Canvas_BlueButtonClick()
    {
        fragment_Canvas.fragment_Canvas_LinearLayout_Dropdown.setVisibility(View.GONE); //hide colours
        fragment_Canvas.fragment_Canvas_Button_Palette.performClick(); //ensuring button isn't currently pressed down
    }

    //red button click
    @Override
    public void fragment_Canvas_RedButtonClick()
    {
        fragment_Canvas.fragment_Canvas_LinearLayout_Dropdown.setVisibility(View.GONE); //hide colours
        fragment_Canvas.fragment_Canvas_Button_Palette.performClick(); //ensuring button isn't current;y pressed down
    }

    //yellow button click
    @Override
    public void fragment_Canvas_YellowButtonClick()
    {
        fragment_Canvas.fragment_Canvas_LinearLayout_Dropdown.setVisibility(View.GONE); //hide colours
        fragment_Canvas.fragment_Canvas_Button_Palette.performClick(); //ensuring button isn't currently pressed down
    }

    //green button click
    @Override
    public void fragment_Canvas_GreenButtonClick()
    {
        fragment_Canvas.fragment_Canvas_LinearLayout_Dropdown.setVisibility(View.GONE); //hide colours
        fragment_Canvas.fragment_Canvas_Button_Palette.performClick(); //ensuring button isn't currently pressed
    }





    /***********************************************************************************************************
     *
     * Methods to deal with Button clicks on the Caption Fragment via Interface
     *
     ***********************************************************************************************************/

    //tags button click
    @Override
    public void fragment_Caption_TagsButtonClick()
    {

    }

    //post button click
    @Override
    public void fragment_Caption_PostButtonClick(String caption)
    {
        postCaption = caption;
        new UploadPostTask().execute(getOverlayedImage());
        changeToFragment(fragment_Newsfeed);
    }





    /***********************************************************************************************************
     *
     * Methods to deal with Button clicks on the Conversation Fragment via Interface
     *
     ***********************************************************************************************************/

    //sendMessage button click
    @Override
    public void fragment_Conversation_SendMessageButtonClick()
    {

    }





    /***********************************************************************************************************
     *
     * AsyncTask for fetching the Newsfeed Posts
     *
     ***********************************************************************************************************/

    //AsyncTask for getting newsfeed
    public class FetchNewsfeedTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            // Create an array
            newsfeedPosts = new ArrayList<HashMap<String, String>>();

            // Retrieve JSON Objects from the given URL address
            jsonarray = _JSONFunctions.getJSONfromURL("http://hypeinc.cloudapp.net:8080/api/feed/newsfeed?mode=1");

            if(jsonarray == null)
            {
                Log.i("STU", "~~~~~~~~~~~~~~~ITS NULL LADS!!!!~~~~~~~~~~");
            }

            try
            {
                // Locate the array name in JSON
                //jsonarray = jsonobject.getJSONArray("Posts");

                if(jsonarray != null)
                {
                    for (int i = 0; i < jsonarray.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        jsonobject = jsonarray.getJSONObject(i);

                        map.put(json_Post_PostID, jsonobject.getString("PostID"));
                        map.put(json_Post_Caption, jsonobject.getString("Body"));
                        map.put(json_Post_Username, jsonobject.getString("Owner"));
                        map.put(json_Post_ImagePath, jsonobject.getString("Image"));
                        map.put(json_Post_Tags, jsonobject.getString("Tags"));
                        map.put(json_Post_ViewCount, jsonobject.getString("Views"));
                        map.put(json_Post_ViewedByUser, jsonobject.getString("IsViewedByUser"));
                        map.put(json_Post_LikeCount, jsonobject.getString("Likes"));
                        map.put(json_Post_LikedByUser, jsonobject.getString("UserLiked"));
                        map.put(json_Post_ReactionCount, jsonobject.getString("Comments"));

                        // Set the JSON Objects into the array
                        newsfeedPosts.add(map);
                    }
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void args)
        {
            fragment_Newsfeed.populateList();
        }

    }





    /***********************************************************************************************************
     *
     * AsyncTask for fetching the Post Image
     *
     ***********************************************************************************************************/

    //AsyncTask for getting newsfeed
    public class FetchImageTask extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params)
        {
            String urlS = "http://hypeinc.cloudapp.net:8080" + params[0];

            Log.i("STU", "Url = "+urlS);
            URL url;
            Bitmap image = null;
            try
            {
                url = new URL(urlS);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


//            Log.i("STU", "DoInBackground image = "+ image.toString());

            postImage = image;

            return null;
        }

        @Override
        protected void onPostExecute(Void args)
        {
            setPostImage(postImage);
            fragment_ShowImage.fragment_ShowImage_ImageView_Image.setImageBitmap(postImage);
        }

    }





    /***********************************************************************************************************
     *
     * AsyncTask for Uploading Post
     *
     ***********************************************************************************************************/

    public class UploadPostTask extends AsyncTask<Bitmap, Void, Void>
    {


        protected Void doInBackground(Bitmap... bitmaps)
        {

            Bitmap bitmap = bitmaps[0];


            String attachmentName = "bitmap";
            String attachmentFileName = "bitmap.jpg";
            String crlf = "\r\n";
            String twoHyphens = "--";
            String boundary =  "*****";

            HttpURLConnection httpUrlConnection = null;
            DataOutputStream request = null;

            try {
                URL url = new URL("http://hypeinc.cloudapp.net:8080/api/feed/PostFile");
                httpUrlConnection = (HttpURLConnection) url.openConnection();
                httpUrlConnection.setUseCaches(false);
                httpUrlConnection.setDoOutput(true);

                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
                httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                String basicAuth = "Basic " + Base64.encodeToString("stuquilletyb@gmail.com:aaaaaa".getBytes(), Base64.NO_WRAP);
                httpUrlConnection.setRequestProperty("Authorization", basicAuth);


                request = new DataOutputStream(
                        httpUrlConnection.getOutputStream());



                request.writeBytes(twoHyphens + boundary + crlf);
                request.writeBytes("Content-Disposition: form-data; name=" + "\"" +
                        "Body" + "\"" + crlf + crlf +
                        postCaption + crlf);
                request.writeBytes(crlf);



                request.writeBytes(twoHyphens + boundary + crlf);
                request.writeBytes("Content-Disposition: form-data; name=\"" +
                        "input" + "\";filename=\"" +
                        attachmentFileName + "\"" + crlf);
                request.writeBytes(crlf);

                //I want to send only 8 bit black & white bitmaps
                /*byte[] pixels = new byte[bitmap.getWidth() * bitmap.getHeight()];
                for (int i = 0; i < bitmap.getWidth(); ++i) {
                    for (int j = 0; j < bitmap.getHeight(); ++j) {
                        //we're interested only in the MSB of the first byte,
                        //since the other 3 bytes are identical for B&W images
                        pixels[i + j] = (byte) ((bitmap.getPixel(i, j) & 0x80) >> 7);
                    }
                }*/

                //Get bitmap from parameters
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); 		// convert Bitmap to ByteArrayOutputStream
                //InputStream in = new ByteArrayInputStream(stream.toByteArray()); // convert ByteArrayOutputStream to ByteArrayInputStream


                request.write(stream.toByteArray());


                request.writeBytes(crlf);
                request.writeBytes(twoHyphens + boundary +
                        twoHyphens + crlf);

                request.flush();
                request.close();


                InputStream responseStream = new
                        BufferedInputStream(httpUrlConnection.getInputStream());

                BufferedReader responseStreamReader =
                        new BufferedReader(new InputStreamReader(responseStream));

                String line = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = responseStreamReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                responseStreamReader.close();

                String response = stringBuilder.toString();
                Log.i("STU", "Response = "+response);

                responseStream.close();
                httpUrlConnection.disconnect();

            }
            catch (IOException i){}


            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

        }
    }




}
