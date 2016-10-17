package com.example.hype;

import android.graphics.Bitmap;


/**
 * Created by Gavin on 25-Sep-15.
 */

public class _Post
{
    public int postID;
    public int userID;
    public String username;
    public String expire;
    public String caption;
    public String image;
    public Boolean liked;
    public Boolean reacted;
    public Boolean viewed;
    public String tagged;
    public int views;
    public int likes;
    public int reactions;

    public _Post(int postID,
                int userID,
                String username,
                String expire,
                String caption,
                String image,
                Boolean liked,
                Boolean reacted,
                Boolean viewed,
                String tagged,
                int views,
                int likes,
                int reactions)
    {
        this.postID = postID;
        this.userID = userID;
        this.username = username;
        this.expire = expire;
        this.caption = caption;
        this.image = image;
        this.liked = liked;
        this.reacted = reacted;
        this.viewed = viewed;
        this.tagged = tagged;
        this.views = views;
        this.likes = likes;
        this.reactions = reactions;
    }
}
