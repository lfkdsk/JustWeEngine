package com.lfk.justweengine.utils.joystick;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lfk.justweengine.R;


/**
 * Joystick
 *
 * @author liufengkai
 *         Created by liufengkai on 2016/12/16.
 */

public class JoystickView extends View implements Runnable {

    ///////////////////////////////////////////////////////////////////////////
    // interface
    ///////////////////////////////////////////////////////////////////////////

    public interface OnMovedListener {
        /**
         * CallBack For Moved
         *
         * @param angle  angle of the button
         * @param length the length of you moved
         */
        void onMoved(int angle, int length);
    }

    private OnMovedListener mOnMovedListener;

    private int mPosX = 0;
    private int mPosY = 0;
    /**
     * the center of the button
     */
    private int mCenterX = 0;
    private int mCenterY = 0;

    /**
     * button radius
     */
    private int mButtonRadius;
    /**
     * boarder radius
     */
    private int mBorderRadius;

    private int mBackgroundColor = Color.WHITE;

    private boolean isCircleWithBackground = false;

    private Bitmap mCircleBackground = null;

    private Paint mPaintBackground;
    private Paint mPaintCircleBorder;
    private Paint mPaintBitmapBackground;
    private Paint mPaintCircleButton;

    private int mCurrentPointAction = MotionEvent.ACTION_CANCEL;

    /**
     * border default color
     */
    private static final int DEFAULT_COLOR = Color.GREEN;

    /**
     * background default color
     */
    private static final int DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT;

    /**
     * default view size
     */
    private static final int DEFAULT_SIZE = 200;

    /**
     * default button size
     */
    private static final double RATIO_SIZE_BUTTON = 0.25;

    /**
     * default border size
     */
    private static final double RATIO_SIZE_BORDER = 0.75;

    /**
     * default border 's width
     */
    private static final int DEFAULT_WIDTH_BORDER = 3;

    private static final int DEFAULT_LOOP_INTERVAL = 50; // in milliseconds

    private OnMovedListener mCallback = null;

    private long mLoopInterval = DEFAULT_LOOP_INTERVAL;

    private boolean isRunning = false;

    private Thread mThread = null;

    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);

        int buttonColor = DEFAULT_COLOR;
        int borderColor = DEFAULT_COLOR;
        int backgroundColor = DEFAULT_BACKGROUND_COLOR;
        int borderWidth = DEFAULT_WIDTH_BORDER;

        if (attrs != null) {
            TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.JoystickView,
                    0, 0
            );


            buttonColor = styledAttributes.getColor(R.styleable.JoystickView_JV_buttonColor, DEFAULT_COLOR);
            borderColor = styledAttributes.getColor(R.styleable.JoystickView_JV_borderColor, DEFAULT_COLOR);
            backgroundColor = styledAttributes.getColor(R.styleable.JoystickView_JV_backgroundColor, DEFAULT_BACKGROUND_COLOR);
            borderWidth = styledAttributes.getDimensionPixelSize(R.styleable.JoystickView_JV_borderWidth, DEFAULT_WIDTH_BORDER);
//            mFixedCenter = styledAttributes.getBoolean(R.styleable.JoystickView_JV_fixedCenter, DEFAULT_FIXED_CENTER);

            styledAttributes.recycle();
        }

        mPaintCircleButton = new Paint();
        mPaintCircleButton.setAntiAlias(true);
        mPaintCircleButton.setColor(buttonColor);
        mPaintCircleButton.setStyle(Paint.Style.FILL);

        mPaintCircleBorder = new Paint();
        mPaintCircleBorder.setAntiAlias(true);
        mPaintCircleBorder.setColor(borderColor);
        mPaintCircleBorder.setStyle(Paint.Style.STROKE);
        mPaintCircleBorder.setStrokeWidth(borderWidth);

        mPaintBackground = new Paint();
        mPaintBackground.setAntiAlias(true);
        mPaintBackground.setColor(backgroundColor);
        mPaintBackground.setStyle(Paint.Style.FILL);
    }

    public JoystickView(Context context) {
        this(context, null);
    }


    ///////////////////////////////////////////////////////////////////////////
    // initial message
    ///////////////////////////////////////////////////////////////////////////

    private void initial() {
        mCenterX = mPosX = getWidth() / 2;
        mCenterY = mPosY = getWidth() / 2;
        downPosX = 0;
        downPosY = 0;
        deltaX = 0;
        deltaY = 0;
        setRunning(false);
    }


    ///////////////////////////////////////////////////////////////////////////
    // draw
    ///////////////////////////////////////////////////////////////////////////

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw background color
        canvas.drawColor(mBackgroundColor);
//        if (isCircleWithBackground && mCircleBackground != null) {
//
//        }
        // border background
        canvas.drawCircle(mCenterX, mCenterY, mBorderRadius, mPaintBackground);
        // circle border background
        canvas.drawCircle(mCenterX, mCenterY, mBorderRadius, mPaintCircleBorder);
        // circle button
        canvas.drawCircle(mPosX, mPosY, mButtonRadius, mPaintCircleButton);
    }

    ///////////////////////////////////////////////////////////////////////////
    // measure size
    ///////////////////////////////////////////////////////////////////////////

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int d = Math.min(measure(widthMeasureSpec), measure(heightMeasureSpec));
        setMeasuredDimension(d, d);
    }

    private int measure(int measureSpec) {
        if (MeasureSpec.getMode(measureSpec) == MeasureSpec.UNSPECIFIED) {
            // if no bounds are specified return a default size (200)
            return DEFAULT_SIZE;
        } else {
            // As you want to fill the available space
            // always return the full available bounds.
            return MeasureSpec.getSize(measureSpec);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        initial();

        // radius based on smallest size : height OR width
        int d = Math.min(w, h);
        mButtonRadius = (int) (d / 2 * RATIO_SIZE_BUTTON);
        mBorderRadius = (int) (d / 2 * RATIO_SIZE_BORDER);
    }

    ///////////////////////////////////////////////////////////////////////////
    // onTouchEvent
    ///////////////////////////////////////////////////////////////////////////

    int downPosX = 0, downPosY = 0;
    int deltaX = 0, deltaY = 0;
    boolean absLargeFlag = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // to move the button according to the finger coordinate
        mCurrentPointAction = event.getAction();

        int currentPosX = (int) (event.getX());
        int currentPosY = (int) (event.getY());

        switch (mCurrentPointAction) {
            case MotionEvent.ACTION_DOWN:
                downPosX = currentPosX;
                downPosY = currentPosY;

                if (mThread == null) {
                    mThread = new Thread(this);
                    setRunning(true);
                    mThread.start();
                }

                break;
            case MotionEvent.ACTION_MOVE:
                deltaX = currentPosX - downPosX;
                deltaY = currentPosY - downPosY;

                mPosX += deltaX;
                mPosY += deltaY;

                double abs = getAbs(mPosX, mPosY);

                if (absLargeFlag && abs > mBorderRadius) {
                    mPosX = currentPosX;
                    mPosY = currentPosY;
                    abs = getAbs(mPosX, mPosY);
                }

                if (abs > mBorderRadius) {
                    absLargeFlag = true;

                    mPosX = (int) ((mPosX - mCenterX) * mBorderRadius / abs + mCenterX);
                    mPosY = (int) ((mPosY - mCenterY) * mBorderRadius / abs + mCenterY);

                    downPosX = mPosX;
                    downPosY = mPosY;
                    break;
                } else {
                    absLargeFlag = false;
                }

                downPosX = currentPosX;
                downPosY = currentPosY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                initial();
                invalidate();
                return true;
        }

        invalidate();

        return true;
    }

    private double getAbs(int posX, int posY) {
        return Math.sqrt((posX - mCenterX) * (posX - mCenterX)
                + (posY - mCenterY) * (posY - mCenterY));
    }

    /**
     * Process the angle following the 360° counter-clock protractor rules.
     *
     * @return the angle of the button
     */
    private int getAngle() {
        int angle = (int) Math.toDegrees(Math.atan2(mCenterY - mPosY, mPosX - mCenterX));
        return angle < 0 ? angle + 360 : angle; // make it as a regular counter-clock protractor
    }


    /**
     * Process the strength as a percentage of the distance between the center and the border.
     *
     * @return the strength of the button
     */
    private int getStrength() {
        return (int) (100 * Math.sqrt((mPosX - mCenterX)
                * (mPosX - mCenterX) + (mPosY - mCenterY)
                * (mPosY - mCenterY)) / mBorderRadius);
    }

    ///////////////////////////////////////////////////////////////////////////
    // getter && setter
    ///////////////////////////////////////////////////////////////////////////

    public OnMovedListener getOnMovedListener() {
        return mOnMovedListener;
    }

    public void setOnMovedListener(OnMovedListener onMovedListener) {
        mOnMovedListener = onMovedListener;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    public void setCircleBackground(Bitmap circleWithBackground) {
        setCircleWithBackground(true);
        this.mCircleBackground = circleWithBackground;
    }

    public void setCircleWithBackground(boolean circleWithBackground) {
        isCircleWithBackground = circleWithBackground;
    }

    public OnMovedListener getCallback() {
        return mCallback;
    }

    public void setCallback(OnMovedListener callback) {
        mCallback = callback;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    private Runnable runnable = new Runnable() {
        public void run() {
            if (mCallback != null)
                mCallback.onMoved(getAngle(), getStrength());
        }
    };

    @Override
    public void run() {
        while (isRunning) {

            post(runnable);

            try {
                Thread.sleep(mLoopInterval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}