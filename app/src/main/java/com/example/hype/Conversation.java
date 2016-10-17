package com.example.hype;

/**
 * Created by Gavin on 05-Oct-15.
 */
public class Conversation
{
    public String[] messages;
    public String mostRecent;
    public String user;
    public boolean recentSeen;

    public Conversation(String[] messages,
                        String mostRecent,
                        String user,
                        boolean recentSeen)
    {
        this.messages = messages;
        this.mostRecent = mostRecent;
        this.user = user;
        this.recentSeen = recentSeen;
    }
}


