package com.example.hype;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Gavin on 28-Sep-15.
 */

public class Fragment_ShowImage extends Fragment implements View.OnClickListener
{
    //declare xml elements here
    ImageView fragment_ShowImage_ImageView_Image;

    //instance of the interface for this fragment
    Fragment_ShowImage_ButtonClickListener fragment_ShowImage_ButtonClickListener;

    //reference to the view of fragment
    View view;

    //global variables


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Creates a reference to the xml layout since it is a fragment and not an activity.
        view = inflater.inflate(R.layout.fragment_showimage, container, false);

        init();

        return view;
    }

    //initialise global variables where possible
    public void init()
    {
        //initialise the ImageView
        fragment_ShowImage_ImageView_Image = (ImageView) view.findViewById(R.id.Fragment_ShowImage_ImageView_Image);
        fragment_ShowImage_ImageView_Image.setOnClickListener(this);
        fragment_ShowImage_ImageView_Image.setImageBitmap(((Activity_Main) getContext()).postImage);
    }


    //when the fragment is visible on screen
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            //initialising fragment interface
            fragment_ShowImage_ButtonClickListener = (Fragment_ShowImage_ButtonClickListener) context;
        }
        catch(Exception ex)
        {}
    }

    //button clicks
    @Override
    public void onClick(View v)
    {
        //camera button was clicked
        if(v.getId() == R.id.Fragment_ShowImage_ImageView_Image)
        {
            fragment_ShowImage_ButtonClickListener.fragment_ShowImage_ImageButtonClick();
        }
    }

    //interface that the Main Activity implements to talk to fragment
    public interface Fragment_ShowImage_ButtonClickListener
    {
        //method for each button click
        void fragment_ShowImage_ImageButtonClick();

    }

}
