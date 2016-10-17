package com.example.hype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Gavin on 05-Oct-15.
 */
public class ListView_Conversation_Adapter extends BaseAdapter
{
    Context context;
    String[] messages;


    //TODO
    public ListView_Conversation_Adapter(Context context, String[] messages)
    {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(context);

        String message;

        message = messages[position];

        View v;
        if(position%2 == 0)
        {
            v = inflater.inflate(R.layout.listview_conversation_other, parent, false);
            TextView tvMessage = (TextView) v.findViewById(R.id.ListView_ConversationOther_TextView_MessageText);

            tvMessage.setText(message);
        }
        else
        {
            v = inflater.inflate(R.layout.listview_conversation_user, parent, false);
            TextView tvMessage = (TextView) v.findViewById(R.id.ListView_ConversationUser_TextView_MessageText);

            tvMessage.setText(message);
        }

        return v;
    }
}
