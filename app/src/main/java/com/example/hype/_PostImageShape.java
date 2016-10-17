package com.example.hype;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.widget.ImageView;

/**
 * Created by Gavin on 01-Oct-15.
 */
public class _PostImageShape extends ImageView
{
    //global variables
    static Paint mPaint;
    static Context context;
    static int radius = 60;
    static Bitmap postImage;

    //constructor
    public _PostImageShape(Context context)
    {
        super(context);

        this.context = context;
    }

    //creates the Post Image Circle (RGB or Grayscale)
    public static Bitmap createPostImageShape(Bitmap bmp, boolean viewed, int viewCount)
    {
        postImage = bmp;

        //scaled version of the input image
        Bitmap image = Bitmap.createScaledBitmap(bmp, radius, radius, false);

        //image that will be returned
        Bitmap output = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        //create a new paint
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, image.getWidth(), image.getHeight());

        //make the image smooth
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        //giving the image default colours
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));

        //drawing the image into a circle
        canvas.drawCircle(image.getWidth() / 2 + 0.7f, image.getHeight() / 2 + 0.7f, image.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(image, rect, rect, paint);

        //if the image was viewed, put a viewCount on top of it
        if(viewed)
        {
            mPaint = new Paint();
            mPaint.setColor(context.getResources().getColor(R.color.hype_white));
            mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            mPaint.setTextSize(30);
            mPaint.setFakeBoldText(true);

            //TODO x and y values will have to change dynamically depending on number of digits in viewCount and textSize
            canvas.drawText(String.valueOf(viewCount), canvas.getWidth()*0.25f, canvas.getWidth()*0.75f, mPaint);
        }
        else //grayscale image
        {
            for(int x = 0; x < output.getWidth(); ++x)
            {
                for(int y = 0; y < output.getHeight(); ++y)
                {
                    // get one pixel color
                    int pixel = output.getPixel(x, y);

                    // retrieve color of all channels
                    int a = Color.alpha(pixel);
                    int r = Color.red(pixel);
                    int g = Color.green(pixel);
                    int b = Color.blue(pixel);

                    // take conversion up to one single value
                    r = g = b = (int)(0.299 * r + 0.587 * g + 0.114 * b);

                    // set new pixel color to output bitmap
                    output.setPixel(x, y, Color.argb(a, r, g, b));
                }
            }
        }

        paint.setColor(context.getResources().getColor(R.color.hype_black));
        paint.setShadowLayer(120, 1, 1, context.getResources().getColor(R.color.hype_black));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        canvas.drawCircle(output.getWidth()/2, output.getHeight()/2, 120, paint);

        return output;
    }



}
