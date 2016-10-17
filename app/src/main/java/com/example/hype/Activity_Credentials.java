package com.example.hype;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Activity_Credentials extends AppCompatActivity implements Fragment_Login.Fragment_Login_ButtonClickListener,
                                                                        Fragment_Register.Fragment_Register_ButtonClickListener,
                                                                        Fragment_ForgotPassword.Fragment_ForgotPassword_ButtonClickListener
{

    //declare xml elements here
    TextView textView_HypeLogo;

    //fragment manager, fragment transaction and individual fragments
    FragmentManager fragmentManager; //allows you to interact with fragments
    FragmentTransaction fragmentTransaction; //allows you to switch between fragments
    Fragment_Login fragment_Login;
    Fragment_Register fragment_Register;
    Fragment_ForgotPassword fragment_ForgotPassword;

    //intent for main activity
    Intent activityMain;

    //global variables
    Typeface hypeFont;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);

        init();
    }

    //for initializing global variable where possible
    public void init()
    {
        fullscreenMode();

        setSymbolsAndFont();

        //initialise the intents
        activityMain = new Intent(this, Activity_Main.class);

        //initialise and change the font of the textview
        textView_HypeLogo = (TextView) findViewById(R.id.Activity_Credentials_TextView_HypeLogo);

        //fragment manager/ fragment transaction and individual fragments
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragment_Login = new Fragment_Login();
        fragment_Register = new Fragment_Register();
        fragment_ForgotPassword = new Fragment_ForgotPassword();

        //setting the initial fragment as login fragment and commit transaction
        fragmentTransaction.add(R.id.Activity_Credentials_LinearLayout_FragmentContainer, fragment_Login);
        fragmentTransaction.commit();
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

    public void setSymbolsAndFont()
    {
        hypeFont = Typeface.createFromAsset(getAssets(), "fonts/gav.ttf");
    }





    /* *********************************************************************************************************
     *
     * Methods to deal with Button clicks on the Login Fragment via Interface
     *
     ***********************************************************************************************************/

    //login button click
    @Override
    public void fragment_Login_LoginButtonClick(String username,
                                                String password)
    {
        /**
         * check authentication
         * if true load MainActivity
         * else display error
         */

        startActivity(activityMain);
    }

    //forgot password button click
    @Override
    public void fragment_Login_ForgotPasswordButtonClick()
    {
        /**
         * remove login fragment and replace it with forgot password fragment
         */

        // change to forgot password fragment
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Activity_Credentials_LinearLayout_FragmentContainer, fragment_ForgotPassword);
        fragmentTransaction.commit();
    }

    //register button click
    @Override
    public void fragment_Login_RegisterButtonClick()
    {
        /**
         * remove login fragment and replace it with register fragment
         */

        //change to register fragment
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Activity_Credentials_LinearLayout_FragmentContainer, fragment_Register);
        fragmentTransaction.commit();
    }





    /* *********************************************************************************************************
     *
     * Methods to deal with Button clicks on the Register Fragment via Interface
     *
     ***********************************************************************************************************/

    //register button click
    @Override
    public void fragment_Register_RegisterButtonClick(String username,
                                                      String password,
                                                      String confirmPassword,
                                                      String phone,
                                                      String email,
                                                      String confirmEmail)
    {
        /**
         * check username isn't taken
         * register user into database
         * log them in
         */

        startActivity(activityMain);
    }

    //have an account button click
    @Override
    public void fragment_Register_HaveAnAccountButtonClick()
    {
        /**
         * remove register fragment and replace it with login fragment
         */

        //change to login fragment
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Activity_Credentials_LinearLayout_FragmentContainer, fragment_Login);
        fragmentTransaction.commit();
    }





    /* *********************************************************************************************************
     *
     * Methods to deal with Button clicks on the Forgot Password Fragment via Interface
     *
     ***********************************************************************************************************/

    //submit button click
    @Override
    public void fragment_ForgotPassword_SubmitButtonClick(String username,
                                                          String phone,
                                                          String email)
    {
        /**
         * check credentials
         * send out sms / email
         * go to login screen
         */

        //change to login fragment
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Activity_Credentials_LinearLayout_FragmentContainer, fragment_Login);
        fragmentTransaction.commit();
    }

    //cancel button click
    @Override
    public void fragment_ForgotPassword_CancelButtonClick()
    {
        /**
         * remove forgot password fragment and replace it with login fragment
         */

        //change to login fragment
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Activity_Credentials_LinearLayout_FragmentContainer, fragment_Login);
        fragmentTransaction.commit();
    }
}