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
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gavin on 05-Oct-15.
 */
public class Fragment_Conversation extends Fragment implements View.OnClickListener {
    //declare xml elements here
    Button fragment_Conversation_Button_SendMessage;
    EditText fragment_Conversation_EditText_ComposeMessage;
    ListView fragment_Conversation_ListView_Messages;

    //instance of the interface for this fragment
    Fragment_Conversation_ButtonClickListener fragment_Conversation_ButtonClickListener;

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
        view = inflater.inflate(R.layout.fragment_conversation, container, false);

        init();

        return view;
    }

    //initialise global variables where possible
    public void init()
    {
        //initialise the Button and onClickListener
        fragment_Conversation_Button_SendMessage = (Button) view.findViewById(R.id.Fragment_Conversation_Button_SendMessage);
        fragment_Conversation_Button_SendMessage.setOnClickListener(this);

        //initialise edittext
        fragment_Conversation_EditText_ComposeMessage = (EditText) view.findViewById(R.id.Fragment_Conversation_EditText_ComposeMessage);

        //initialise the ListView
        fragment_Conversation_ListView_Messages = (ListView) view.findViewById(R.id.Fragment_Conversation_ListView_Messages);

        //TODO
        messages = new String[]{"Hey", "Wazzup?", "In tomorrow?", "Yep :/", "Effort!!!", "I know", "Only a half day though :)", "That's not too bad, I thought it was going to be another long ass day"};
        conversations = new ArrayList<>();
        conversations.add(new Conversation(messages, "That's not too bad, I thought it was going to be another long ass day", "Stuart Quille", false));
        conversations.add(new Conversation(messages, "That's not too bad, I thought it was going to be another long ass day", "Rory Kirke", true));
        conversations.add(new Conversation(messages, "That's not too bad, I thought it was going to be another long ass day", "Barry Cormack", true));

        adapter = new ListView_Conversation_Adapter(getContext(), messages);
        fragment_Conversation_ListView_Messages.setAdapter(adapter);

    }


    //when the fragment is visible on screen
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            //initialising fragment interface
            fragment_Conversation_ButtonClickListener = (Fragment_Conversation_ButtonClickListener) context;
        }
        catch(Exception ex)
        {}
    }

    //button clicks
    @Override
    public void onClick(View v)
    {
        //camera button was clicked
        if(v.getId() == R.id.Fragment_Conversation_Button_SendMessage)
        {
            fragment_Conversation_ButtonClickListener.fragment_Conversation_SendMessageButtonClick();
        }
    }

    //interface that the Main Activity implements to talk to fragment
    public interface Fragment_Conversation_ButtonClickListener
    {
        //method for each button click
        void fragment_Conversation_SendMessageButtonClick();

    }
}
