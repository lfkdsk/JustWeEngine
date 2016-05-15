package com.lfk.justweengine.Engine.Layer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.renderscript.Float2;
import android.renderscript.Float3;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import com.lfk.justweengine.Engine.GameTimer;
import com.lfk.justweengine.Engine.TouchMode;
import com.lfk.justweengine.Utils.logger.LogLevel;
import com.lfk.justweengine.Utils.logger.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Screen SurfaceView封装视图
 * 负责绘制
 * Created by liufengkai on 16/5/5.
 */
public class Screen extends SurfaceView implements Runnable, View.OnTouchListener {
    public static final int RESULT_OK = -1;

    private Canvas e_canvas;
    // 主循环
    private Thread e_thread;
    // 循环控制
    private boolean e_running, e_paused;
    private int e_pauseCount;
    private Paint e_paintDraw, e_paintFont;
    private int e_numPoints;
    private long e_prefferredFrameRate, e_sleepTime;
    private Typeface e_typeface;
    private Point[] e_touchPoints;
    private boolean e_touchModesAble;
    // 多点触控数
    private int e_touchNum;
    private int e_backgroundColor;
    private boolean e_isFrameOpen;
    private TouchMode e_touch_Mode;

    private ArrayList<Layer> e_layers;
    private boolean isOpenDebug;
    private ScreenListener screenListener;
    private Context e_engine;

    public interface ScreenListener {
        void Init();

        void Load();

        void Update();

        void Touch(MotionEvent event);
    }

    public Screen(Context context, ScreenListener screenListener) {
        super(context);
        this.e_engine = context;
        this.setLayerListener(screenListener);
        init();
    }

    public Screen(Context context, ScreenListener screenListener, boolean isOpenDebug) {
        super(context);
        this.isOpenDebug = isOpenDebug;
        if (!isOpenDebug) {
            Logger.init().logLevel(LogLevel.NONE);
        } else {
            Logger.init();
        }
        this.setLayerListener(screenListener);
        init();
    }

    private void init() {
        e_canvas = null;
        e_thread = null;
        e_running = false;
        e_paused = false;
        e_paintDraw = null;
        e_pauseCount = 0;
        e_paintFont = null;
        e_numPoints = 0;
        e_prefferredFrameRate = 40;
        e_sleepTime = 1000 / e_prefferredFrameRate;
        e_typeface = null;
        e_touchModesAble = true;
        e_touchNum = 5;
        e_isFrameOpen = true;
        e_touch_Mode = TouchMode.SINGLE;
        e_backgroundColor = Color.BLACK;

        e_layers = new ArrayList<>();
        this.setOnTouchListener(this);
        this.destroyDrawingCache();
        this.setDrawingCacheEnabled(false);
        this.setClickable(false);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        this.setLongClickable(false);
    }

    /**
     * 默认要放在onCreate()里面
     * 负责创建绘制页面
     */
    public void createScreen() {
        screenListener.Init();

        // draw paint
        e_paintDraw = new Paint();
        e_paintDraw.setColor(Color.WHITE);
        // font paint
        e_paintFont = new Paint();
        e_paintFont.setColor(Color.WHITE);
        e_paintFont.setTextSize(24);

        screenListener.Load();

        // init thread
        e_running = true;
        e_thread = new Thread(this);
        e_thread.start();

    }

    @Override
    public void run() {
        Logger.d("engine run start");

        GameTimer frameTimer = new GameTimer();
        int frameCount = 0;
        int frameRate = 0;
        long startTime;
        long timeDiff;

        while (e_running) {
            if (e_paused) continue;

            frameCount++;
            startTime = frameTimer.getElapsed();

            // frame rate
            if (frameTimer.stopWatch(1000)) {

                frameRate = frameCount;

                frameCount = 0;

                // reset points
                e_numPoints = 0;
            }

            // update
            screenListener.Update();

            for (int i = 0; i < e_layers.size(); i++) {
                e_layers.get(i).layerUpdate();
                if (e_layers.get(i).getLayerListener() != null)
                    e_layers.get(i).getLayerListener().Update();
            }

            for (int i = 0; i < e_layers.size(); i++) {
                // lock canvas

                Layer layer = e_layers.get(i);

                if (beginDrawing(layer.getLayerField())) {
                    e_canvas.drawColor(e_backgroundColor);

                    e_canvas.drawColor(layer.layerBackgroundColor);
                    // draw
                    // ahead of draw concrete sub

                    layer.layerDraw();

                    // user method draw
                    if (layer.getLayerListener() != null) {
                        layer.getLayerListener().Draw();
                    }

                    if (e_isFrameOpen && isOpenDebug) {
                        int x = e_canvas.getWidth() - 150;
                        e_canvas.drawText("engine", x, 20, e_paintFont);
                        e_canvas.drawText(toString(frameRate) + "fps", x, 40, e_paintFont);
                        e_canvas.drawText("pauses:" + toString(e_pauseCount), x, 60, e_paintFont);
                    }

                    // unlock the canvas
                    endDrawing();
                }

                // new collision
                layer.layerCollision();
            }

            // lock frame
            timeDiff = frameTimer.getElapsed() - startTime;
            long updatePeriod = e_sleepTime - timeDiff;
//            Logger.v("period", updatePeriod);
            if (updatePeriod > 0) {
                try {
                    Thread.sleep(updatePeriod);
                } catch (InterruptedException e) {
//                    Logger.e("engine run start error:" + e);
                }
            }
        }
        Logger.d("engine run end");
        System.exit(RESULT_OK);
    }


    /**
     * engine onTouch
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (e_touch_Mode) {
            case SINGLE:
                screenListener.Touch(event);
                break;
            case FULL:
                stuffTouchPoints(event);
                break;
            case BUTTON:
                findTouchButton(event);
                break;
            case SINGLE_BUTTON:
                if (!findTouchButton(event))
                    screenListener.Touch(event);
                break;
            case SINGLE_LAYER:
                distributeTouchEventInLayer(event);
                break;
        }
        return true;
    }

    /**
     * begin draw
     *
     * @return
     */
    private boolean beginDrawing(Rect rect) {
        if (!this.getHolder().getSurface().isValid()) {
            return false;
        }
        e_canvas = this.getHolder().lockCanvas(rect);
        return true;
    }

    /**
     * end draw
     */
    private void endDrawing() {
        this.getHolder().unlockCanvasAndPost(e_canvas);
    }

    /**
     * open frame count
     *
     * @param e_isFrameOpen
     */
    public void setIsFrameOpen(boolean e_isFrameOpen) {
        this.e_isFrameOpen = e_isFrameOpen;
    }

    /**
     * background color
     *
     * @param e_backgroundColor
     */
    public void setBackgroundColor(int e_backgroundColor) {
        this.e_backgroundColor = e_backgroundColor;
    }

    /**
     * set touch number
     *
     * @param e_touchNum
     */
    public void setTouchNum(int e_touchNum) {
        this.e_touchNum = e_touchNum;
    }

    /**
     * set touch mode able
     *
     * @param e_touchModesAble
     */
    public void setTouchModesAble(boolean e_touchModesAble) {
        this.e_touchModesAble = e_touchModesAble;
    }

    /**
     * shortcut method to duplicate existing  Android methods
     *
     * @param msg
     */
    public void fatalError(String msg) {
        Logger.e("engine fatal error:" + msg);
        System.exit(0);
    }

    /**
     * draw text
     *
     * @param text
     * @param x
     * @param y
     */
    public void drawText(String text, int x, int y) {
        e_canvas.drawText(text, x, y, e_paintFont);
    }

    /**
     * engine helper
     */

    /**
     * @return surface
     */
    public SurfaceView getSurfaceView() {
        return this;
    }

    /**
     * @return canvas
     */
    public Canvas getCanvas() {
        return e_canvas;
    }

    /**
     * set frame rete
     *
     * @param rate
     */
    public void setFrameRate(int rate) {
        e_prefferredFrameRate = rate;
        e_sleepTime = 1000 / e_prefferredFrameRate;
    }

    /**
     * get touch puts
     *
     * @return
     */
    public int getTouchPoints() {
        return e_numPoints;
    }

    /**
     * get touch point
     *
     * @param n
     * @return
     */
    public Point getTouchPoint(int n) {
        if (n < 0) {
            Logger.e("error", "without point" + n);
            n = 0;
        }
        if (n > e_touchNum) {
            n = e_touchNum;
        }
        return e_touchPoints[n];
    }

    /**
     * set paint color
     *
     * @param color
     */
    public void setDrawColor(int color) {
        e_paintDraw.setColor(color);
    }

    /**
     * set text color
     *
     * @param color
     */
    public void setTextColor(int color) {
        e_paintFont.setColor(color);
    }

    /**
     * set text size
     *
     * @param size
     */
    public void setTextSize(int size) {
        e_paintFont.setTextSize(size);
    }

    public void setTextSize(float size) {
        e_paintFont.setTextSize((int) size);
    }

    public enum FontStyles {
        NORMAL(Typeface.NORMAL),
        BOLD(Typeface.BOLD),
        ITALIC(Typeface.ITALIC),
        BOLD_ITALIC(Typeface.BOLD_ITALIC),;
        int value;

        FontStyles(int value) {
            this.value = value;
        }
    }

    /**
     * set type face
     *
     * @param style
     */
    public void setFontStyle(FontStyles style) {
        e_typeface = Typeface.create(Typeface.DEFAULT, style.value);
        e_paintFont.setTypeface(e_typeface);
    }

    public void setFontStyle(Typeface style) {
        e_paintFont.setTypeface(style);
    }

    /**
     * to string
     *
     * @param value
     * @return
     */
    public String toString(int value) {
        return Integer.toString(value);
    }

    public String toString(float value) {
        return Float.toString(value);
    }

    public String toString(double value) {
        return Double.toString(value);
    }

    public String toString(Float2 value) {
        return "X:" + round(value.x) + "," + "Y:" + round(value.y);
    }

    public String toString(Float3 value) {
        return "X:" + round(value.x) + "," + "Y:" + round(value.y)
                + "Z:" + round(value.z);
    }

    public String toString(Rect value) {
        RectF rectF = new RectF(value.left, value.top, value.right, value.bottom);
        return toString(rectF);
    }

    public String toString(RectF value) {
        return "{" + round(value.left) + "," +
                round(value.top) + "," +
                round(value.right) + "," +
                round(value.bottom) + "}";
    }

    /**
     * double round
     *
     * @param value
     * @return
     */
    public double round(double value) {
        return round(value, 2);
    }

    private double round(double value, int precision) {
        try {
            BigDecimal bd = new BigDecimal(value);
            BigDecimal rounded = bd.setScale(precision, BigDecimal.ROUND_HALF_UP);
            return rounded.doubleValue();
        } catch (Exception e) {
            Logger.e("engine", " round error:" + e);
        }
        Logger.e("engine", " round D:" + 0);
        return 0;
    }

    private void stuffTouchPoints(MotionEvent event) {
        e_numPoints = event.getPointerCount();
        if (e_numPoints > e_touchNum) {
            e_numPoints = e_touchNum;
        }

        for (int n = 0; n < e_numPoints; n++) {
            Logger.v("engine", e_touchNum + ":" + e_numPoints);
            e_touchPoints[n].x = (int) event.getX(n);
            e_touchPoints[n].y = (int) event.getY(n);
        }
    }

    private boolean findTouchButton(MotionEvent event) {
        for (int i = 0; i < e_layers.size(); i++) {
            if (e_layers.get(i).getLayerType() == Layer.LayerType.Button
                    && e_layers.get(i).getLayerField()
                    .contains((int) event.getX(), (int) event.getY())) {
                e_layers.get(i).layerClick(event);
                return true;
            }
        }
        return false;
    }

    private void distributeTouchEventInLayer(MotionEvent event) {
        for (int i = 0; i < e_layers.size(); i++) {
            if (e_layers.get(i).getLayerField()
                    .contains((int) event.getX(), (int) event.getY())) {
                e_layers.get(i).layerListener.Touch(event);
                break;
            }
        }
    }


    public void debugDraw(RectF bound) {
        if (isOpenDebug) {
            e_paintDraw.setColor(Color.RED);
            e_paintDraw.setStyle(Paint.Style.STROKE);
            e_canvas.drawRect(bound, e_paintDraw);
        }
    }

    public void setLayerListener(ScreenListener Listener) {
        this.screenListener = Listener;
    }

    public void setTouchMode(TouchMode e_touch_Mode) {
        this.e_touch_Mode = e_touch_Mode;
    }

    public ScreenListener getLayerListener() {
        return screenListener;
    }

    public void addLayer(Layer layer) {
        this.e_layers.add(layer);
    }

    public void addLayerBefore(Layer layer, String name) {
        int index = 0;
        for (; index < e_layers.size(); index++) {
            if (e_layers.get(index).getLayerName().equals(name)) {
                break;
            }
        }
        this.e_layers.add(index, layer);
    }

    public void addLayerAfter(Layer layer, String name) {
        int index = 0;
        for (; index < e_layers.size(); index++) {
            if (e_layers.get(index).getLayerName().equals(name)) {
                index++;
                break;
            }
        }
        this.e_layers.add(index, layer);
    }


    public void restart() {
        this.e_paused = false;
    }

    public void pause() {
        this.e_paused = true;
    }

    public void addPauseTime() {
        this.e_pauseCount++;
    }
}
