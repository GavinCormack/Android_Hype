package com.example.hype;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Gavin on 01-Oct-15.
 */

public class _DrawingCanvas extends View
{
    //Variables for drawing
    private Canvas mCanvas; //the canvas used to draw on
    private Path mPath; //the current path the user is drawing
    private Paint mBitmapPaint;
    private Paint circlePaint; //the colour of the ring around user's finger
    private Path circlePath; //the path of the circle around the user's finger
    static Paint mPaint; //the colour of the path drawn

    //To keep track of the lines and colors for undo
    ArrayList<Path> paths = new ArrayList<Path>(); //keeps track of the paths the user has drawn
    ArrayList<Paint> cols = new ArrayList<Paint>(); //keeps track of the colours of the paths

    //global variable to keep track of x,y of screen touch
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4; //minimum distance to move finger before it is drawn


    //Constructor for the drawingcanvas
    public _DrawingCanvas(Context c, AttributeSet attrs)
    {
        super(c, attrs);

        //initialise the variables for drawing
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        circlePath = new Path();
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(getResources().getColor(R.color.hype_blue));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(6);
    }

    //if the canvas size has changed (i.e screen orientation)
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        mCanvas = new Canvas();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        //draws each path inside the paths list
        for (int i = 0; i < paths.size(); i++)
        {
            canvas.drawPath(paths.get(i), cols.get(i));
        }

        //draw the new path
        canvas.drawPath(mPath, mPaint);

        //draw the circle around finger
        canvas.drawPath(circlePath, circlePaint);
    }

    //undo the previous path drawn
    public void drawUndo()
    {
        //checks if a path exists
        if (paths.size() > 0)
        {
            //remove the path for its list and colour from its list
            paths.remove(paths.size()-1);
            cols.remove(cols.size()-1);
            invalidate(); //redraw the canvas
        }
    }

    //where the screen was first touched
    private void touch_start(float x, float y)
    {
        mPath.reset();
        mPath.moveTo(x, y); //start drawing the path here
        mX = x; //update the global version of x
        mY = y; //update the global version of y
    }

    //where the touch has moved to
    private void touch_move(float x, float y)
    {
        //get the absolute distance of the new position
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        //check if the finger has moved far enough to update drawing
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE)
        {
            //move to this position
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;

            //draw a new cirlce around the finger
            circlePath.reset();
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }
    }

    //when the finger lifts off the screen
    private void touch_up()
    {
        //draw a line to this position
        mPath.lineTo(mX, mY);

        //stop drawing the circle around the finger
        circlePath.reset();

        //finish the drawing
        mCanvas.drawPath(mPath, mPaint);

        //add the new path and colour to their respective lists
        cols.add(mPaint);
        paths.add(mPath);

        //prepare a new colour and path
        mPaint = new Paint(mPaint);
        mPath = new Path();
    }

    //when the screen is touched
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //get the coordinates of the touch
        float x = event.getX();
        float y = event.getY();

        //check which kind of touch it was and call the necessary method
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN: //finger touched screen
                touch_start(x, y); //coordinates of event
                invalidate(); //redraw
                break;
            case MotionEvent.ACTION_MOVE: //finger moved along screen
                touch_move(x, y); //coordinates of event
                invalidate(); //redraw
                break;
            case MotionEvent.ACTION_UP: //finger lifted off screen
                touch_up(); //coordinates of event
                invalidate(); //redraw
                break;
        }

        return true;
    }

}
