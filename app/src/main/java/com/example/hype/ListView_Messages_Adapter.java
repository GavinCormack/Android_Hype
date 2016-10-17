package com.example.hype;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Gavin on 05-Oct-15.
 */

public class ListView_Messages_Adapter extends BaseAdapter
{
    Context context;
    List<Conversation> conversations;

    //TODO
    public ListView_Messages_Adapter(Context context, List<Conversation> conversations)
    {
        this.context = context;
        this.conversations = conversations;
    }

    @Override
    public int getCount() {
        return conversations.size();
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

        View v = inflater.inflate(R.layout.listview_messages, parent, false);

        String mostRecent, user;
        boolean recentSeen;

        user = conversations.get(position).user;
        mostRecent = conversations.get(position).mostRecent;
        recentSeen = conversations.get(position).recentSeen;

        TextView tvUser = (TextView) v.findViewById(R.id.ListView_Messages_TextView_Username);
        TextView tvRecent = (TextView) v.findViewById(R.id.ListView_Messages_TextView_Message);
        TextView tvSeen = (TextView) v.findViewById(R.id.ListView_Messages_TextView_SeenUnseen);
        if(recentSeen)
        {
            tvSeen.setTextColor(context.getResources().getColor(R.color.hype_grey));
        }
        else
        {
            tvSeen.setTextColor(context.getResources().getColor(R.color.hype_blue));
        }

        tvSeen.setTypeface(((Activity_Main) context).hypeFont);
        tvUser.setText(user);
        tvRecent.setText(mostRecent);

        return v;
    }
}
