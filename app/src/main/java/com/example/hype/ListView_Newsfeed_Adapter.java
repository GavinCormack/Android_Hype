package com.example.hype;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Gavin on 28-Sep-15.
 */

public class ListView_Newsfeed_Adapter extends BaseAdapter
{
    //xml elements
    Button bUsername, bTags, bLike, bReact, bViews;
    ImageButton ibImage;
    TextView tvCaption, tvExpire;

    private static LayoutInflater inflater;

    //JSON Storing
    ArrayList<HashMap<String, String>> allPosts;


    //JSON Elements
    String postUsername, postCaption, postExpire, postTags, postImagePath;
    String postID, postUserID, postLikeCount, postReactionCount, postViewCount;
    boolean postLikedByUser, postViewedByUser, postReactedByUser;


    //global variables
    Context context;
    Bitmap postImage;

   // _ImageLoader imageLoader;
   // _ViewHolder holder;


    public ListView_Newsfeed_Adapter(Context context, ArrayList<HashMap<String, String>> aPosts)
    {
        this.context = context;
        allPosts = aPosts;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //imageLoader = new _ImageLoader(context);
    }

    @Override
    public int getCount()
    {
        return allPosts.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(context);

        View v = inflater.inflate(R.layout.listview_post, parent, false);
/*
        if(convertView == null)
        {
            holder = new _ViewHolder();
            holder.bUsername = (Button) v.findViewById(R.id.ListView_Post_Button_Username);
            holder.bLike = (Button) v.findViewById(R.id.ListView_Post_Button_Like);
            holder.bReact = (Button) v.findViewById(R.id.ListView_Post_Button_React);
            holder.bTags = (Button) v.findViewById(R.id.ListView_Post_Button_Tags);
            holder.bViews = (Button) v.findViewById(R.id.ListView_Post_Button_Views);
            holder.tvExpire = (TextView) v.findViewById(R.id.ListView_Post_TextView_Expire);
            holder.tvCaption = (TextView) v.findViewById(R.id.ListView_Post_TextView_Caption);
            holder.ibImage = (ImageButton) v.findViewById(R.id.ListView_Post_ImageButton_PostImage);

            v.setTag(holder);
        }
        else
        {
            holder = (_ViewHolder) v.getTag();
        }
*/
        Log.i("STU", "Posts = "+allPosts);

        //sets the variables to their respective data
        postID = allPosts.get(position).get(((Activity_Main) context).json_Post_PostID);
        postUserID = allPosts.get(position).get(((Activity_Main) context).json_Post_UserID);
        postUsername = allPosts.get(position).get(((Activity_Main) context).json_Post_Username);
        postCaption = allPosts.get(position).get(((Activity_Main) context).json_Post_Caption);
        postExpire = allPosts.get(position).get(((Activity_Main) context).json_Post_Expire);
        postTags = allPosts.get(position).get(((Activity_Main) context).json_Post_Tags);
        postImagePath = allPosts.get(position).get(((Activity_Main) context).json_Post_ImagePath);
        postLikeCount = allPosts.get(position).get(((Activity_Main) context).json_Post_LikeCount);
        postReactionCount = allPosts.get(position).get(((Activity_Main) context).json_Post_ReactionCount);
        postViewCount = allPosts.get(position).get(((Activity_Main) context).json_Post_ViewCount);
        postLikedByUser = Boolean.getBoolean(allPosts.get(position).get(((Activity_Main) context).json_Post_LikedByUser));
        postReactedByUser = Boolean.getBoolean(allPosts.get(position).get(((Activity_Main) context).json_Post_ReactedByUser));
        postViewedByUser = Boolean.getBoolean(allPosts.get(position).get(((Activity_Main) context).json_Post_ViewedByUser));

        //initialise xml elements
        bUsername = (Button) v.findViewById(R.id.ListView_Post_Button_Username);
        bLike = (Button) v.findViewById(R.id.ListView_Post_Button_Like);
        bReact = (Button) v.findViewById(R.id.ListView_Post_Button_React);
        bTags = (Button) v.findViewById(R.id.ListView_Post_Button_Tags);
        bViews = (Button) v.findViewById(R.id.ListView_Post_Button_Views);
        tvExpire = (TextView) v.findViewById(R.id.ListView_Post_TextView_Expire);
        tvCaption = (TextView) v.findViewById(R.id.ListView_Post_TextView_Caption);
        ibImage = (ImageButton) v.findViewById(R.id.ListView_Post_ImageButton_PostImage);

        //allows custom font to be displayed
        setSymbolsAndFont();


        bUsername.setText(postUsername);
        bLike.setText(context.getResources().getString(R.string.icon_heart) + postLikeCount);
        bReact.setText(postReactionCount + context.getResources().getString(R.string.icon_reactions));
        bTags.setText(postTags);
        bViews.setText(context.getResources().getString(R.string.icon_eye) + postViewCount);
        tvExpire.setText(postExpire);
        tvCaption.setText(postCaption);

        //imageLoader.displayImage("http://hypeinc.cloudapp.net"+postImagePath, holder.ibImage);

        ibImage.setTag(position);

        ibImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.i("STU","Click, Tag = " + v.getTag());
                Log.i("STU","Click, allPosts = " + allPosts.toString());
                Log.i("STU", "Click, Final Path = " + allPosts.get(position).get(((Activity_Main) context).json_Post_ImagePath));
                ((Activity_Main) context).setPostImagePath(allPosts.get(position).get(((Activity_Main) context).json_Post_ImagePath));
                ((Activity_Main) context).fetchPostImage();
                ((Activity_Main) context).changeToFragment(((Activity_Main) context).fragment_ShowImage);
                ((Activity_Main) context).hideTabbar();
            }
        });



        if(postLikedByUser)
        {
            bLike.setTextColor(context.getResources().getColor(R.color.hype_red));
        }
        else
        {
            bLike.setTextColor(context.getResources().getColor(R.color.hype_grey));
        }
        if(postReactedByUser)
        {
            bReact.setTextColor(context.getResources().getColor(R.color.hype_red));
        }
        else
        {
            bReact.setTextColor(context.getResources().getColor(R.color.hype_grey));
        }

        return v;
    }

    public void setSymbolsAndFont()
    {
        bLike.setTypeface(((Activity_Main) context).hypeFont);
        bReact.setTypeface(((Activity_Main) context).hypeFont);
        bViews.setTypeface(((Activity_Main) context).hypeFont);
    }



    public static class _ViewHolder
    {
        Button bUsername, bTags, bLike, bReact, bViews;
        ImageButton ibImage;
        TextView tvCaption, tvExpire;
    }



}



