package com.example.hype;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Gavin on 29-Sep-15.
 */

public class Fragment_Canvas extends Fragment implements View.OnClickListener
{
    //declare xml elements here
    Button fragment_Canvas_Button_Back,
            fragment_Canvas_Button_Palette,
            fragment_Canvas_Button_Undo,
            fragment_Canvas_Button_Ok,
            fragment_Canvas_Button_PaletteBlue,
            fragment_Canvas_Button_PaletteRed,
            fragment_Canvas_Button_PaletteYellow,
            fragment_Canvas_Button_PaletteGreen;
    ImageView fragment_Canvas_ImageView_Image;
    LinearLayout fragment_Canvas_LinearLayout_Dropdown;

    //instance of the interface for this fragment
    Fragment_Canvas_ButtonClickListener fragment_Canvas_ButtonClickListener;

    //reference to the view of fragment
    View view;

    //global variables
    _DrawingCanvas drawView;
    Bitmap canvasBitmap, overlayBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Creates a reference to the xml layout since it is a fragment and not an activity.
        view = inflater.inflate(R.layout.fragment_canvas, container, false);

        init();

        return view;
    }

    //initialise global variables where possible
    public void init()
    {
        //initialise the Button and onClickListener
        fragment_Canvas_Button_Back = (Button) view.findViewById(R.id.Fragment_Canvas_Button_Back);
        fragment_Canvas_Button_Back.setOnClickListener(this);
        fragment_Canvas_Button_Palette = (Button) view.findViewById(R.id.Fragment_Canvas_Button_Palette);
        fragment_Canvas_Button_Palette.setOnClickListener(this);
        fragment_Canvas_Button_Undo = (Button) view.findViewById(R.id.Fragment_Canvas_Button_Undo);
        fragment_Canvas_Button_Undo.setOnClickListener(this);
        fragment_Canvas_Button_Ok = (Button) view.findViewById(R.id.Fragment_Canvas_Button_Ok);
        fragment_Canvas_Button_Ok.setOnClickListener(this);

        fragment_Canvas_Button_PaletteBlue = (Button) view.findViewById(R.id.Fragment_Canvas_Button_PaletteBlue);
        fragment_Canvas_Button_PaletteBlue.setOnClickListener(this);
        fragment_Canvas_Button_PaletteRed = (Button) view.findViewById(R.id.Fragment_Canvas_Button_PaletteRed);
        fragment_Canvas_Button_PaletteRed.setOnClickListener(this);
        fragment_Canvas_Button_PaletteYellow = (Button) view.findViewById(R.id.Fragment_Canvas_Button_PaletteYellow);
        fragment_Canvas_Button_PaletteYellow.setOnClickListener(this);
        fragment_Canvas_Button_PaletteGreen = (Button) view.findViewById(R.id.Fragment_Canvas_Button_PaletteGreen);
        fragment_Canvas_Button_PaletteGreen.setOnClickListener(this);

        //initialise the LinearLayout
        fragment_Canvas_LinearLayout_Dropdown = (LinearLayout) view.findViewById(R.id.Fragment_Canvas_LinearLayout_Dropdown);

        //initialise the SurfaceView
        fragment_Canvas_ImageView_Image = (ImageView) view.findViewById(R.id.Fragment_Canvas_ImageView_Image);
        fragment_Canvas_ImageView_Image.setImageBitmap(((Activity_Main) getActivity()).getPicTaken());

        //global variables
        drawView = (_DrawingCanvas) view.findViewById(R.id.Fragment_Canvas_Hype_DrawingCanvas);
        drawView.setBackgroundColor(Color.TRANSPARENT); //so the image can be seen behind it

        //set the symbol's font
        setSymbolsAndFont();
    }

    //sets the font of the symbols so they work
    public void setSymbolsAndFont()
    {
        fragment_Canvas_Button_Back.setTypeface(((Activity_Main) getContext()).hypeFont);
        fragment_Canvas_Button_Palette.setTypeface(((Activity_Main) getContext()).hypeFont);
        fragment_Canvas_Button_Undo.setTypeface(((Activity_Main) getContext()).hypeFont);
        fragment_Canvas_Button_Ok.setTypeface(((Activity_Main) getContext()).hypeFont);
        fragment_Canvas_Button_PaletteBlue.setTypeface(((Activity_Main) getContext()).hypeFont);
        fragment_Canvas_Button_PaletteRed.setTypeface(((Activity_Main) getContext()).hypeFont);
        fragment_Canvas_Button_PaletteYellow.setTypeface(((Activity_Main) getContext()).hypeFont);
        fragment_Canvas_Button_PaletteGreen.setTypeface(((Activity_Main) getContext()).hypeFont);
    }

    //overlays bmp2 on top of bmp1
    public Bitmap overlayImages(Bitmap bmp1, Bitmap bmp2)
    {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        return bmOverlay;
    }

    //when the fragment is visible on screen
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            //initialising fragment interface
            fragment_Canvas_ButtonClickListener = (Fragment_Canvas_ButtonClickListener) context;
        }
        catch(Exception ex)
        {}
    }

    //button clicks
    @Override
    public void onClick(View v)
    {
        //which button was clicked
        switch (v.getId())
        {
            case R.id.Fragment_Canvas_Button_Back:
                fragment_Canvas_ButtonClickListener.fragment_Canvas_BackButtonClick();
                break;

            case R.id.Fragment_Canvas_Button_Palette:
                fragment_Canvas_ButtonClickListener.fragment_Canvas_PaletteButtonClick();
                break;

            case R.id.Fragment_Canvas_Button_Undo:
                drawView.drawUndo();
                break;


            case R.id.Fragment_Canvas_Button_Ok:
                drawView.setDrawingCacheEnabled(true);
                canvasBitmap = drawView.getDrawingCache();

                Bitmap b = canvasBitmap.createScaledBitmap(drawView.getDrawingCache(), ((Activity_Main) getActivity()).getPicTaken().getWidth(), ((Activity_Main) getActivity()).getPicTaken().getHeight(), false);

                Bitmap bmpOverlay = overlayImages(((Activity_Main) getActivity()).getPicTaken(), b);


                overlayBitmap = bmpOverlay;

                ((Activity_Main) getActivity()).setOverlayedImage(overlayBitmap);

                fragment_Canvas_ButtonClickListener.fragment_Canvas_OkButtonClick();
                break;

            case R.id.Fragment_Canvas_Button_PaletteBlue:
                drawView.mPaint.setColor(getResources().getColor(R.color.hype_blue)); //change paint colour
                fragment_Canvas_Button_Palette.setTextColor(getResources().getColor(R.color.hype_blue));
                fragment_Canvas_ButtonClickListener.fragment_Canvas_BlueButtonClick();
                break;

            case R.id.Fragment_Canvas_Button_PaletteRed:
                drawView.mPaint.setColor(getResources().getColor(R.color.hype_red)); //change paint colour
                fragment_Canvas_Button_Palette.setTextColor(getResources().getColor(R.color.hype_red));
                fragment_Canvas_ButtonClickListener.fragment_Canvas_RedButtonClick();
                break;

            case R.id.Fragment_Canvas_Button_PaletteYellow:
                drawView.mPaint.setColor(getResources().getColor(R.color.hype_yellow)); //change paint colour
                fragment_Canvas_Button_Palette.setTextColor(getResources().getColor(R.color.hype_yellow));
                fragment_Canvas_ButtonClickListener.fragment_Canvas_YellowButtonClick();
                break;

            case R.id.Fragment_Canvas_Button_PaletteGreen:
                drawView.mPaint.setColor(getResources().getColor(R.color.hype_green)); //change paint colour
                fragment_Canvas_Button_Palette.setTextColor(getResources().getColor(R.color.hype_green));
                fragment_Canvas_ButtonClickListener.fragment_Canvas_GreenButtonClick();
                break;
        }
    }

    //interface that the Main Activity implements to talk to fragment
    public interface Fragment_Canvas_ButtonClickListener
    {
        //method for each button click
        void fragment_Canvas_BackButtonClick();
        void fragment_Canvas_PaletteButtonClick();
        void fragment_Canvas_OkButtonClick();
        void fragment_Canvas_BlueButtonClick();
        void fragment_Canvas_RedButtonClick();
        void fragment_Canvas_YellowButtonClick();
        void fragment_Canvas_GreenButtonClick();

    }
}

