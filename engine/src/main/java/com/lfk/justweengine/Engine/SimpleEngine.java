package com.lfk.justweengine.engine;

import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.renderscript.Float2;
import android.renderscript.Float3;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;

import com.lfk.justweengine.drawable.Button.BaseButton;
import com.lfk.justweengine.drawable.Sprite.BaseSub;
import com.lfk.justweengine.utils.logger.LogLevel;
import com.lfk.justweengine.utils.logger.Logger;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Engine nucleus
 *
 * @author liufengkai
 *         Created by liufengkai on 15/11/26.
 */
public abstract class SimpleEngine extends Engine implements Runnable, View.OnTouchListener {
    private SurfaceView e_surfaceView;
    private Canvas e_canvas;
    // 主循环
    private Thread e_thread;
    // 循环控制
    private boolean e_running, e_paused;
    private int e_pauseCount;
    private Paint e_paintDraw, e_paintFont;
    private int e_numPoints;
    private long e_preferredFrameRate, e_sleepTime;
    private Typeface e_typeface;
    private Point[] e_touchPoints;
    private boolean e_touchModesAble;
    // 多点触控数
    private int e_touchNum;
    private int e_backgroundColor;
    private boolean e_isFrameOpen;
    private boolean isOpenDebug;
    private boolean e_openAllDebug;
    private TouchMode e_touch_Mode;
    private CopyOnWriteArrayList<BaseSub> e_sprite_group;
    private CopyOnWriteArrayList<BaseSub> e_sprite_recycle_group;
    // for buttons
    private HashMap<String, BaseButton> e_button_group;
    private boolean e_is_hit_button;
    private BaseButton e_hit_button = null;


    /**
     * engine constructor
     */
    public SimpleEngine() {
        // init logger
        Logger.init().logLevel(LogLevel.NONE);
        Engine();
    }

    public SimpleEngine(boolean isOpenDebug) {
        this.isOpenDebug = isOpenDebug;
        if (!isOpenDebug) {
            Logger.init().logLevel(LogLevel.NONE);
        } else {
            Logger.init();
        }
        Engine();
    }

    private void Engine() {
        e_surfaceView = null;
        e_canvas = null;
        e_thread = null;
        e_running = false;
        e_paused = false;
        e_paintDraw = null;
        e_pauseCount = 0;
        e_paintFont = null;
        e_numPoints = 0;
        e_preferredFrameRate = 40;
        e_sleepTime = 1000 / e_preferredFrameRate;
        e_typeface = null;
        e_touchModesAble = true;
        e_touchNum = 5;
        e_openAllDebug = false;
        e_isFrameOpen = true;
        e_touch_Mode = TouchMode.SINGLE;
        e_backgroundColor = Color.BLACK;
        e_sprite_group = new CopyOnWriteArrayList<>();
        e_sprite_recycle_group = new CopyOnWriteArrayList<>();
        e_button_group = new HashMap<>();
        e_is_hit_button = false;
        e_hit_button = null;
        Logger.d("Engine constructor");
    }

    public abstract void init();

    public abstract void load();

    public abstract void draw();

    public abstract void update();

    public abstract void touch(MotionEvent event);

    public abstract void collision(BaseSub baseSub);

    /**
     * engine onCreate
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("engine onCreate start");
        // disable title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // default landscape
//        setScreenOrientation(ScreenMode.LANDSCAPE);

        // without it, screen will be black when init
        this.getWindow().setFormat(PixelFormat.TRANSLUCENT);

        init();

        // init surfaceView
        e_surfaceView = new SurfaceView(this);

        // set content view
        setContentView(e_surfaceView);

        // touch listener
        e_surfaceView.setOnTouchListener(this);

        // init touch mode
//        if (e_touchModesAble) {
//            e_touchPoints = new Point[e_touchNum];
//            for (int i = 0; i < e_touchNum; i++) {
//                e_touchPoints[i] = new Point(0, 0);
//            }
//        }

        // draw paint
        e_paintDraw = new Paint();
        e_paintDraw.setColor(Color.WHITE);
        // font paint
        e_paintFont = new Paint();
        e_paintFont.setColor(Color.WHITE);
        e_paintFont.setTextSize(24);

        load();

        // init thread
        e_running = true;
        e_thread = new Thread(this);
        e_thread.start();

        Logger.d("engine onCreate end");
    }

    /**
     * engine onResume
     */
    @Override
    protected void onResume() {
        super.onResume();
        Logger.d("engine onResume");
        e_paused = false;
        // need add...
    }

    /**
     * engine onPause
     */
    @Override
    protected void onPause() {
        super.onPause();
        Logger.d("engine onPause");
        e_paused = true;
        e_pauseCount++;
        // need add...
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
                touch(event);
                break;
            case FULL:
                stuffTouchPoints(event);
                break;
            case BUTTON:
                findTouchButton(event);
                break;
            case SINGLE_BUTTON:
                if (!findTouchButton(event))
                    touch(event);
                break;
        }
        return true;
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
            update();

            for (int i = 0; i < e_sprite_group.size(); i++) {
                BaseSub A = e_sprite_group.get(i);
                if (!A.getAlive()) continue;

                if (!A.isCollidable()) continue;

                if (A.isCollided()) continue;

                for (int j = 0; j < e_sprite_group.size(); j++) {
                    BaseSub B = e_sprite_group.get(j);
                    if (!B.getAlive()) continue;

                    if (!B.isCollidable()) continue;

                    if (B.isCollided()) continue;

                    if (A == B) continue;

                    if (A.getIdentifier() ==
                            B.getIdentifier())
                        continue;

                    if (collisionCheck(A, B)) {
                        A.setCollided(true);
                        A.setOffender(B);
                        B.setCollided(true);
                        B.setOffender(A);
                        break;
                    }
                }
            }

            // lock canvas
            if (beginDrawing()) {
                e_canvas.drawColor(e_backgroundColor);

                for (String o : e_button_group.keySet()) {
                    BaseButton button = e_button_group.get(o);
                    button.animation();
                    button.draw();
                }
                // draw ahead of draw concrete sub
                draw();

                for (int k = 0; k < e_sprite_group.size(); k++) {
                    BaseSub baseSub = e_sprite_group.get(k);
                    if (baseSub.getAlive()) {
                        baseSub.animation();
                        baseSub.draw();
                    }
                    if (isOpenDebug && baseSub.isCollidable() && baseSub.isCollided()) {
                        drawDebugLine(baseSub.getBounds());
                    } else if (e_openAllDebug) {
                        drawDebugLine(baseSub.getBounds());
                    }
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
            for (int l = 0; l < e_sprite_group.size(); l++) {
                BaseSub baseSub = e_sprite_group.get(l);
                if (!baseSub.getAlive()) {
                    e_sprite_recycle_group.add(baseSub);
                    e_sprite_group.remove(baseSub);
                    continue;
                }

                if (baseSub.isCollidable()) {
                    if (baseSub.isCollided()) {
                        // Is it a valid object ?
                        if (baseSub.getOffender() != null) {
                            // collision
                            collision(baseSub);
                            // reset offender
                            baseSub.setOffender(null);
                        }
                        baseSub.setCollided(false);
                    }
                }

                baseSub.setCollided(false);
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
     * begin draw
     *
     * @return
     */
    private boolean beginDrawing() {
        if (!e_surfaceView.getHolder().getSurface().isValid()) {
            return false;
        }
        e_canvas = e_surfaceView.getHolder().lockCanvas();
        return true;
    }

    /**
     * end draw
     */
    private void endDrawing() {
        e_surfaceView.getHolder().unlockCanvasAndPost(e_canvas);
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

    public enum ScreenMode {
        LANDSCAPE(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE),
        PORTRAIT(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        int value;

        ScreenMode(int mode) {
            this.value = mode;
        }
    }

    /**
     * set screenOrientation
     *
     * @param mode
     */
    public void setScreenOrientation(ScreenMode mode) {
        setRequestedOrientation(mode.value);
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
     * draw helper
     */

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
        return e_surfaceView;
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
        e_preferredFrameRate = rate;
        e_sleepTime = 1000 / e_preferredFrameRate;
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

    private boolean collisionCheck(BaseSub A, BaseSub B) {
        return RectF.intersects(A.getBounds(), B.getBounds());
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
     * add BaseSub to group
     *
     * @param sprite
     */
    public void addToSpriteGroup(BaseSub sprite) {
        e_sprite_group.add(sprite);
    }

    /**
     * remove from group
     *
     * @param sprite
     */
    protected void removeFromSpriteGroup(BaseSub sprite) {
        e_sprite_group.remove(sprite);
    }

    protected void removeFromSpriteGroup(int index) {
        e_sprite_group.remove(index);
    }

    /**
     * get size
     *
     * @return size
     */
    protected int getSpriteGroupSize() {
        return e_sprite_group.size();
    }

    protected int getRecycleGroupSize() {
        return e_sprite_recycle_group.size();
    }

    protected void addToRecycleGroup(BaseSub baseSub) {
        e_sprite_recycle_group.add(baseSub);
    }

    protected void removeFromRecycleGroup(int index) {
        e_sprite_recycle_group.remove(index);
    }

    protected void removeFromRecycleGroup(BaseSub baseSub) {
        e_sprite_recycle_group.remove(baseSub);
    }

    protected boolean isRecycleGroupEmpty() {
        return e_sprite_recycle_group.isEmpty();
    }

    protected BaseSub recycleSubFromGroup(int id) {
        for (BaseSub baseSub : e_sprite_recycle_group) {
            if (baseSub.getIdentifier() == id) {
                return baseSub;
            }
        }
        return null;
    }

    protected int getTypeSizeFromRecycleGroup(int id) {
        int num = 0;
        for (BaseSub baseSub : e_sprite_recycle_group) {
            if (baseSub.getIdentifier() == id) {
                num++;
            }
        }
        Log.e("num" + num, "id" + id);
        return num;
    }

    protected void addToButtonGroup(BaseButton button) {
        e_button_group.put(button.getName(), button);
    }

    protected void removeButtonFromGroup(String name) {
        e_button_group.remove(name);
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
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (String name : e_button_group.keySet()) {
                    BaseButton button = e_button_group.get(name);
                    if (button.getRect().contains((int) event.getX(),
                            (int) event.getY())) {
                        button.setNormal(false);
                        e_is_hit_button = true;
                        e_hit_button = button;
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (e_hit_button != null && e_is_hit_button &&
                        e_hit_button.getRect().contains((int) event.getX(),
                                (int) event.getY())) {
                    e_hit_button.setNormal(true);
                    e_hit_button.onClick(true);
                    resetHitButton();
                    return true;
                } else if (e_is_hit_button) {
                    if (e_hit_button != null) {
                        e_hit_button.setNormal(true);
                        resetHitButton();
                    }
                    return true;
                }
                break;
        }
        return false;
    }

    private void resetHitButton() {
        e_is_hit_button = false;
        e_hit_button = null;
    }

    public void setTouchMode(TouchMode e_touch_Mode) {
        this.e_touch_Mode = e_touch_Mode;
    }

    public boolean isOpenDebug() {
        return isOpenDebug;
    }

    public void setOpenDebug(boolean openDebug) {
        isOpenDebug = openDebug;
    }

    /**
     * 绘制debug模式下的边框
     *
     * @param bound RectF轮廓
     */
    public void drawDebugLine(RectF bound) {
        if (isOpenDebug()) {
            e_paintDraw.setColor(Color.RED);
            e_paintDraw.setStyle(Paint.Style.STROKE);
            e_canvas.drawRect(bound, e_paintDraw);
        }
    }

    public boolean isOpenAllDebug() {
        return e_openAllDebug;
    }

    public void setOpenAllDebug(boolean e_openAllDebug) {
        this.e_openAllDebug = e_openAllDebug;
    }
}
