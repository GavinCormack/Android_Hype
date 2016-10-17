package com.example.hype;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Activity_Splash extends AppCompatActivity
{
    //declare xml elements here
    TextView textView_HypeLogo;

    //intent for credentials activity
    Intent activityCredentials;

    //length of splash screen
    int splashTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
    }

    //for initializing global variable where possible
    public void init()
    {
        //initialise the textview
        textView_HypeLogo = (TextView) findViewById(R.id.Activity_Splash_TextView_HypeLogo);

        fullscreenMode();
        startSplash();

        activityCredentials = new Intent(this, Activity_Credentials.class);
        splashTime = 3000;
    }

    //makes the activity fullscreen by removing the StatusBar and ActionBar
    public void fullscreenMode()
    {
        View decorView = getWindow().getDecorView();
        // Hide the StatusBar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // StatusBar is hidden, now hide ActionBar
        getSupportActionBar().hide();
    }

    private void startSplash()
    {
        //Thread that will run for a certain time and then launch the credentials activity
        final Thread timer = new Thread()
        {
            public void run()
            {
                Looper.prepare();

                try
                {
                    sleep(splashTime);
                }

                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                finally
                {
                    //start credentials activity and destroy splash activity
                    startActivity(activityCredentials);
                    finish();
                }

                Looper.loop();
            }
        };

        //start the thread
        timer.start();
    }

}

