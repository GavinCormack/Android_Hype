package com.example.hype;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;

/**
 * Created by Gavin on 29-Sep-15.
 */

public class Fragment_Camera extends Fragment implements View.OnClickListener,
                                                            TextureView.SurfaceTextureListener
{
    //declare xml elements here
    Button fragment_Camera_Button_Back,
            fragment_Camera_Button_Rotate,
            fragment_Camera_Button_Ok;
    TextureView fragment_Camera_TextureView_CameraPreview;

    //instance of the interface for this fragment
    Fragment_Camera_ButtonClickListener fragment_Camera_ButtonClickListener;

    //reference to the view of fragment
    View view;

    //global variables
    public SurfaceTexture surface;
    android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
    Camera cam;
    byte[] array;
    Context context;
    Bitmap bmp, newBmp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Creates a reference to the xml layout since it is a fragment and not an activity.
        view = inflater.inflate(R.layout.fragment_camera, container, false);

        init();

        return view;
    }

    //initialise global variables where possible
    public void init()
    {
        //initialise the Button and onClickListener
        fragment_Camera_Button_Back = (Button) view.findViewById(R.id.Fragment_Camera_Button_Back);
        fragment_Camera_Button_Back.setOnClickListener(this);
        fragment_Camera_Button_Rotate = (Button) view.findViewById(R.id.Fragment_Camera_Button_Rotate);
        fragment_Camera_Button_Rotate.setOnClickListener(this);
        fragment_Camera_Button_Ok = (Button) view.findViewById(R.id.Fragment_Camera_Button_Ok);
        fragment_Camera_Button_Ok.setOnClickListener(this);

        //initialise the TextureView
        fragment_Camera_TextureView_CameraPreview = (TextureView) view.findViewById(R.id.Fragment_Camera_TextureView_CameraPreview);
        fragment_Camera_TextureView_CameraPreview.setSurfaceTextureListener(this);

        //initialise global variables
        context = getActivity().getApplicationContext();
        cam = null;

        //hide tabbar
        ((Activity_Main) getContext()).hideTabbar();

        //set the symbol's font
        setSymbolsAndFont();
    }

    //sets the font of the symbols so they work
    public void setSymbolsAndFont()
    {
        fragment_Camera_Button_Back.setTypeface(((Activity_Main) getContext()).hypeFont);
        fragment_Camera_Button_Rotate.setTypeface(((Activity_Main) getContext()).hypeFont);
        fragment_Camera_Button_Ok.setTypeface(((Activity_Main) getContext()).hypeFont);
    }

    //takes a picture using the camera
    public void takePic()
    {
        Camera.ShutterCallback mySC = new Camera.ShutterCallback()
        {
            @Override
            public void onShutter()
            { }
        };

        Camera.PictureCallback myPC_Raw = new Camera.PictureCallback()
        {
            @Override
            public void onPictureTaken(byte[] data, Camera camera)
            {}
        };

        Camera.PictureCallback myPC_Jpeg = new Camera.PictureCallback()
        {
            //setting up camera for picture
            @Override
            public void onPictureTaken(byte[] data, Camera camera)
            {
                //saves image as byte array
                array = data;

                //makes sure the image can be saved
                if(bmp != null)
                {
                    bmp.recycle();
                    bmp = null;
                }

                //makes the image useable
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inMutable = true;
                opt.inTempStorage = new byte[16 * 1024];

                //settings for camera
                Camera.Parameters parameters = camera.getParameters();
                Camera.Size size = parameters.getPictureSize();

                //checks camera dimensions
                int height11 = size.height;
                int width11 = size.width;
                float mb = (width11 * height11) / 1024000;

                if (mb > 4f)
                {
                    opt.inSampleSize = 4;
                }
                else if (mb > 3f)
                {
                    opt.inSampleSize = 2;
                }

                //converts image from byte array to bitmap
                bmp = BitmapFactory.decodeByteArray(data, 0, data.length, opt);

                //rotates image 90 degrees so it faces upright
                Matrix m = new Matrix();
                m.setRotate(90);

                //create new image with new rotation
                newBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), m, false);

                //save image to a global variable and start the canvas fragment
                ((Activity_Main) getActivity()).setPicTaken(newBmp);
                ((Activity_Main) getActivity()).changeToFragment(((Activity_Main) getActivity()).fragment_Canvas);

            }
        };

        //takes the picture
        cam.takePicture(mySC, myPC_Raw, null, myPC_Jpeg);

    }

    //sets up the camera
    public void preview()
    {
        //releases the camera so it can be used again
        if(cam != null)
        {
            cam.stopPreview();
            cam.release();
            cam = null;
        }

        //requests to use the camera (provided that it is free to use)
        if(cam == null)
        {
            cam = Camera.open();
        }

        //start camera preview
        try
        {
            cam.setPreviewTexture(surface);
            cam.startPreview();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        //camera focus setting
        cam.getParameters().setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        //rotates the camera so it faces upright
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation)
        {
            case Surface.ROTATION_0: degrees = 270; break;
            case Surface.ROTATION_90: degrees = 0; break;
            case Surface.ROTATION_180: degrees = 90; break;
            case Surface.ROTATION_270: degrees = 180; break;
        }

        int result;

        //display the camera in upright position
        result = (info.orientation - degrees + 360) % 360;
        cam.setDisplayOrientation(result);

    }





    /***********************************************************************************************************
     *
     * Methods to deal with Texture View for the Camera
     *
     ***********************************************************************************************************/

    // when the view is available to use
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height)
    {
        this.surface = surface;

        preview(); //setup the camera
    }

    // view has changed size (i.e screen orientation)
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height)
    {

    }

    //view was destroyed, different app opened or fragment destroyed
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface)
    {
        cam.setPreviewCallback(null);
        cam.stopPreview();
        cam.release();
        cam = null;

        return false;
    }

    //when the view is updated
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface)
    {

    }





    /***********************************************************************************************************
     *
     * Android OnClick method for button clicks
     *
     ***********************************************************************************************************/

    //button clicks
    @Override
    public void onClick(View v)
    {
        //which button was clicked
        switch (v.getId())
        {
            case R.id.Fragment_Camera_Button_Back:
                fragment_Camera_ButtonClickListener.fragment_Camera_BackButtonClick();
                break;

            case R.id.Fragment_Camera_Button_Rotate:
                fragment_Camera_ButtonClickListener.fragment_Camera_RotateButtonClick();
                break;

            case R.id.Fragment_Camera_Button_Ok:
                takePic(); //take the picture
                break;
        }
    }



    //interface that the Main Activity implements to talk to fragment
    public interface Fragment_Camera_ButtonClickListener
    {
        //method for each button click
        void fragment_Camera_BackButtonClick();
        void fragment_Camera_RotateButtonClick();
        void fragment_Camera_OkButtonClick();

    }

}

