package com.example.hype;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

/**
 * Created by Gavin on 29-Sep-15.
 */

public class Fragment_Caption extends Fragment implements View.OnClickListener
{
    //declare xml elements here
    Button fragment_Caption_Button_Tags,
            fragment_Caption_Button_Post;
    Switch fragment_Caption_Switch_Group;
    EditText fragment_Caption_EditText_Caption;

    //instance of the interface for this fragment
    Fragment_Caption_ButtonClickListener fragment_Caption_ButtonClickListener;

    //reference to the view of fragment
    View view;

    //global variables

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Creates a reference to the xml layout since it is a fragment and not an activity.
        view = inflater.inflate(R.layout.fragment_caption, container, false);

        init();

        return view;
    }

    //initialise global variables where possible
    public void init()
    {
        //initialise the Button and onClickListener
        fragment_Caption_Button_Tags = (Button) view.findViewById(R.id.Fragment_Caption_Button_Tags);
        fragment_Caption_Button_Tags.setOnClickListener(this);
        fragment_Caption_Button_Post = (Button) view.findViewById(R.id.Fragment_Caption_Button_Post);
        fragment_Caption_Button_Post.setOnClickListener(this);

        //initialise the SurfaceView
        fragment_Caption_Switch_Group = (Switch) view.findViewById(R.id.Fragment_Caption_Switch_Group);

        fragment_Caption_EditText_Caption = (EditText) view.findViewById(R.id.Fragment_Caption_EditText_Caption);

    }


    //when the fragment is visible on screen
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            //initialising fragment interface
            fragment_Caption_ButtonClickListener = (Fragment_Caption_ButtonClickListener) context;
        }
        catch(Exception ex)
        {}
    }

    //button clicks
    @Override
    public void onClick(View v)
    {
        //which button was clicked
        switch (v.getId())
        {
            case R.id.Fragment_Caption_Button_Tags:
                fragment_Caption_ButtonClickListener.fragment_Caption_TagsButtonClick();
                break;

            case R.id.Fragment_Caption_Button_Post:
                fragment_Caption_ButtonClickListener.fragment_Caption_PostButtonClick(fragment_Caption_EditText_Caption.getText().toString());
                break;
        }
    }

    //interface that the Main Activity implements to talk to fragment
    public interface Fragment_Caption_ButtonClickListener
    {
        //method for each button click
        void fragment_Caption_TagsButtonClick();
        void fragment_Caption_PostButtonClick(String caption);

    }
}

