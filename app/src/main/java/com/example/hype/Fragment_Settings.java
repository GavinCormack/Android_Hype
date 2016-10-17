package com.example.hype;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Gavin on 22/09/2015.
 */
public class Fragment_Settings extends Fragment implements View.OnClickListener
{
    //declare xml elements here
    ListView fragment_Settings_ListView_Settings;

    //reference to the view of fragment
    View view;

    //global variables

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Creates a reference to the xml layout since it is a fragment and not an activity.
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        init();

        return view;
    }

    //initialise global variables where possible
    public void init()
    {
        //initialise the ListView
        fragment_Settings_ListView_Settings = (ListView) view.findViewById(R.id.Fragment_Settings_ListView_Settings);

        //set the symbol's font
        setSymbolsAndFont();

    }

    //sets the font of the symbols so they work
    public void setSymbolsAndFont()
    {

    }


    //when the fragment is visible on screen
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    //button clicks
    @Override
    public void onClick(View v)
    {

    }

}

