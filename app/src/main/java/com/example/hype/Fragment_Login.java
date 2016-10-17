package com.example.hype;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Gavin on 21/09/2015.
 */
public class Fragment_Login extends Fragment implements View.OnClickListener
{
    //declare xml elements here
    EditText fragment_Login_EditText_Username,
            fragment_Login_EditText_Password;
    Button fragment_Login_Button_Login,
            fragment_Login_Button_ForgotPassword,
            getFragment_Login_Button_Register;

    //instance of the interface for this fragment
    Fragment_Login_ButtonClickListener fragment_Login_ButtonClickListener;

    //reference to the view of fragment
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Creates a reference to the xml layout since it is a fragment and not an activity.
        view = inflater.inflate(R.layout.fragment_login, container, false);

        init();

        return view;
    }

    //initialise global variables where possible
    public void init()
    {
        //initialise the EditTexts
        fragment_Login_EditText_Username = (EditText) view.findViewById(R.id.Fragment_Login_EditText_Username);
        fragment_Login_EditText_Password = (EditText) view.findViewById(R.id.Fragment_Login_EditText_Password);

        //initialise the Buttons and set their onClickListener's
        fragment_Login_Button_Login = (Button) view.findViewById(R.id.Fragment_Login_Button_Login);
        fragment_Login_Button_Login.setOnClickListener(this);
        fragment_Login_Button_ForgotPassword = (Button) view.findViewById(R.id.Fragment_Login_Button_ForgotPassword);
        fragment_Login_Button_ForgotPassword.setOnClickListener(this);
        getFragment_Login_Button_Register = (Button) view.findViewById(R.id.Fragment_Login_Button_Register);
        getFragment_Login_Button_Register.setOnClickListener(this);
    }

    //when the fragment is visible on screen
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            //initialising fragment interface
            fragment_Login_ButtonClickListener = (Fragment_Login_ButtonClickListener) context;
        }
        catch(Exception ex)
        {}
    }

    //button clicks
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.Fragment_Login_Button_Login:
                //login button was clicked
                fragment_Login_ButtonClickListener.fragment_Login_LoginButtonClick(fragment_Login_EditText_Username.getText().toString(),
                        fragment_Login_EditText_Password.getText().toString());
                break;

            case R.id.Fragment_Login_Button_ForgotPassword:
                //forgot password button was clicked
                fragment_Login_ButtonClickListener.fragment_Login_ForgotPasswordButtonClick();
                break;

            case R.id.Fragment_Login_Button_Register:
                //register button was clicked
                fragment_Login_ButtonClickListener.fragment_Login_RegisterButtonClick();
                break;
        }

    }

    //interface that the Credentials Activity implements to talk to login fragment
    public interface Fragment_Login_ButtonClickListener
    {
        //method for each button click
        void fragment_Login_LoginButtonClick(String username,
                                             String password);
        void fragment_Login_ForgotPasswordButtonClick();
        void fragment_Login_RegisterButtonClick();
    }

}
