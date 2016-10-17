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
public class Fragment_ForgotPassword extends Fragment implements View.OnClickListener
{
    //declare xml elements here
    EditText fragment_ForgotPassword_EditText_Username,
            fragment_ForgotPassword_EditText_Phone,
            fragment_ForgotPassword_EditText_Email;
    Button fragment_ForgotPassword_Button_Submit,
            fragment_ForgotPassword_Button_Cancel;

    //instance of the interface for this fragment
    Fragment_ForgotPassword_ButtonClickListener fragment_ForgotPassword_ButtonClickListener;

    //reference to the view of fragment
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Creates a reference to the xml layout since it is a fragment and not an activity.
        view = inflater.inflate(R.layout.fragment_forgotpassword, container, false);

        init();

        return view;
    }

    //initialise global variables where possible
    public void init()
    {
        //initialise the EditTexts
        fragment_ForgotPassword_EditText_Username = (EditText) view.findViewById(R.id.Fragment_ForgotPassword_EditText_Username);
        fragment_ForgotPassword_EditText_Phone = (EditText) view.findViewById(R.id.Fragment_ForgotPassword_EditText_Phone);
        fragment_ForgotPassword_EditText_Email = (EditText) view.findViewById(R.id.Fragment_ForgotPassword_EditText_Email);

        //initialise the Buttons and set their onClickListener's
        fragment_ForgotPassword_Button_Submit = (Button) view.findViewById(R.id.Fragment_ForgotPassword_Button_Submit);
        fragment_ForgotPassword_Button_Submit.setOnClickListener(this);
        fragment_ForgotPassword_Button_Cancel = (Button) view.findViewById(R.id.Fragment_ForgotPassword_Button_Cancel);
        fragment_ForgotPassword_Button_Cancel.setOnClickListener(this);
    }

    //when the fragment is visible on screen
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            //initialising fragment interface
            fragment_ForgotPassword_ButtonClickListener = (Fragment_ForgotPassword_ButtonClickListener) context;
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
            case R.id.Fragment_ForgotPassword_Button_Submit:
                //submit button was clicked
                fragment_ForgotPassword_ButtonClickListener.fragment_ForgotPassword_SubmitButtonClick(fragment_ForgotPassword_EditText_Username.getText().toString(),
                        fragment_ForgotPassword_EditText_Phone.getText().toString(),
                        fragment_ForgotPassword_EditText_Email.getText().toString());
                break;

            case R.id.Fragment_ForgotPassword_Button_Cancel:
                //cancel button was clicked
                fragment_ForgotPassword_ButtonClickListener.fragment_ForgotPassword_CancelButtonClick();
                break;
        }

    }

    //interface that the Credentials Activity implements to talk to forgotPassword fragment
    public interface Fragment_ForgotPassword_ButtonClickListener
    {
        //method for each button click
        void fragment_ForgotPassword_SubmitButtonClick(String username,
                                                       String phone,
                                                       String email);
        void fragment_ForgotPassword_CancelButtonClick();
    }

}