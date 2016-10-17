package com.example.hype;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gavin on 22/09/2015.
 */
public class Fragment_Messages extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    //declare xml elements here
    Button fragment_Messages_Button_CreateNew;
    ListView fragment_Messages_ListView_RecentConversations;

    //instance of the interface for this fragment
    Fragment_Messages_ButtonClickListener fragment_Messages_ButtonClickListener;

    //reference to the view of fragment
    View view;

    //global variables

    //TODO
    List<Conversation> conversations;
    String[] messages;

    ListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Creates a reference to the xml layout since it is a fragment and not an activity.
        view = inflater.inflate(R.layout.fragment_messages, container, false);

        init();

        return view;
    }

    //initialise global variables where possible
    public void init()
    {
        //initialise the Button and onClickListener
        fragment_Messages_Button_CreateNew = (Button) view.findViewById(R.id.Fragment_Messages_Button_CreateNew);
        fragment_Messages_Button_CreateNew.setOnClickListener(this);

        //initialise the ListView
        fragment_Messages_ListView_RecentConversations = (ListView) view.findViewById(R.id.Fragment_Messages_ListView_RecentConversations);

        //set the symbol's font
        setSymbolsAndFont();

        //TODO
        messages = new String[]{"Hey", "Wazzup?", "In tomorrow?", "Yep :/", "Effort!!!", "I know", "Only a half day though :)", "That's not too bad, I thought it was going to be another long ass day"};
        conversations = new ArrayList<>();
        conversations.add(new Conversation(messages, "That's not too bad, I thought it was going to be another long ass day", "Stuart Quille", false));
        conversations.add(new Conversation(messages, "That's not too bad, I thought it was going to be another long ass day", "Rory Kirke", true));
        conversations.add(new Conversation(messages, "That's not too bad, I thought it was going to be another long ass day", "Barry Cormack", true));

        adapter = new ListView_Messages_Adapter(getContext(), conversations);
        fragment_Messages_ListView_RecentConversations.setAdapter(adapter);
        fragment_Messages_ListView_RecentConversations.setOnItemClickListener(this);

    }

    //sets the font of the symbols so they work
    public void setSymbolsAndFont()
    {
        fragment_Messages_Button_CreateNew.setTypeface(((Activity_Main) getContext()).hypeFont);
    }


    //when the fragment is visible on screen
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            //initialising fragment interface
            fragment_Messages_ButtonClickListener = (Fragment_Messages_ButtonClickListener) context;
        }
        catch(Exception ex)
        {}
    }

    //button clicks
    @Override
    public void onClick(View v)
    {
        //camera button was clicked
        if(v.getId() == R.id.Fragment_Messages_Button_CreateNew)
        {
            fragment_Messages_ButtonClickListener.fragment_Messages_CreateNewButtonClick();
        }
    }

    //listview clicks
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        ((Activity_Main) getContext()).changeToFragment(((Activity_Main) getContext()).fragment_Conversation);
    }

    //interface that the Main Activity implements to talk to fragment
    public interface Fragment_Messages_ButtonClickListener
    {
        //method for each button click
        void fragment_Messages_CreateNewButtonClick();

    }
}