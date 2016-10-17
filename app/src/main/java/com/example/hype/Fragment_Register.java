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
public class Fragment_Register extends Fragment implements View.OnClickListener
{
    //declare xml elements here
    EditText fragment_Register_EditText_Username,
            fragment_Register_EditText_Password,
            fragment_Register_EditText_ConfirmPassword,
            fragment_Register_EditText_Phone,
            fragment_Register_EditText_Email,
            fragment_Register_EditText_ConfirmEmail;
    Button fragment_Register_Button_Register,
            fragment_Register_Button_HaveAnAccount;

    //instance of the interface for this fragment
    Fragment_Register_ButtonClickListener fragment_Register_ButtonClickListener;

    //reference to the view of fragment
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Creates a reference to the xml layout since it is a fragment and not an activity.
        view = inflater.inflate(R.layout.fragment_register, container, false);

        init();

        return view;
    }

    //initialise global variables where possible
    public void init()
    {
        //initialise the EditTexts
        fragment_Register_EditText_Username = (EditText) view.findViewById(R.id.Fragment_Register_EditText_Username);
        fragment_Register_EditText_Password = (EditText) view.findViewById(R.id.Fragment_Register_EditText_Password);
        fragment_Register_EditText_ConfirmPassword = (EditText) view.findViewById(R.id.Fragment_Register_EditText_ConfirmPassword);
        fragment_Register_EditText_Phone = (EditText) view.findViewById(R.id.Fragment_Register_EditText_Phone);
        fragment_Register_EditText_Email = (EditText) view.findViewById(R.id.Fragment_Register_EditText_Email);
        fragment_Register_EditText_ConfirmEmail = (EditText) view.findViewById(R.id.Fragment_Register_EditText_ConfirmEmail);

        //initialise the Buttons and set their onClickListener's
        fragment_Register_Button_Register = (Button) view.findViewById(R.id.Fragment_Register_Button_Register);
        fragment_Register_Button_Register.setOnClickListener(this);
        fragment_Register_Button_HaveAnAccount = (Button) view.findViewById(R.id.Fragment_Register_Button_HaveAnAccount);
        fragment_Register_Button_HaveAnAccount.setOnClickListener(this);
    }

    //when the fragment is visible on screen
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            //initialising fragment interface
            fragment_Register_ButtonClickListener = (Fragment_Register_ButtonClickListener) context;
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
            case R.id.Fragment_Register_Button_Register:
                //register button was clicked
                fragment_Register_ButtonClickListener.fragment_Register_RegisterButtonClick(fragment_Register_EditText_Username.getText().toString(),
                        fragment_Register_EditText_Password.getText().toString(),
                        fragment_Register_EditText_ConfirmPassword.getText().toString(),
                        fragment_Register_EditText_Phone.getText().toString(),
                        fragment_Register_EditText_Email.getText().toString(),
                        fragment_Register_EditText_ConfirmEmail.getText().toString());
                break;

            case R.id.Fragment_Register_Button_HaveAnAccount:
                //forgot password button was clicked
                fragment_Register_ButtonClickListener.fragment_Register_HaveAnAccountButtonClick();
                break;

        }

    }

    //interface that the Credentials Activity implements to talk to register fragment
    public interface Fragment_Register_ButtonClickListener
    {
        //method for each button click
        void fragment_Register_RegisterButtonClick(String username,
                                                   String password,
                                                   String confirmPassword,
                                                   String phone,
                                                   String email,
                                                   String confirmEmail);
        void fragment_Register_HaveAnAccountButtonClick();
    }

}
