package com.lfk.justweengine.Engine;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;
import android.view.View;

/**
 * Engine 核心类
 *
 * @author liufengkai
 *         Created by liufengkai on 15/11/26.
 */
public abstract class Engine extends Activity implements Runnable, View.OnTouchListener {
    private SurfaceView e_surfaceView;
    private Canvas e_canvas;
    private boolean e_running, e_paused;
    private int e_pauseCount;
    private Paint e_paintDraw, e_paintFont;
    private int e_numPoints;
    private long e_prefferredFrameRate, e_sleepTime;

    /**
     * engine constructor
     */
    public Engine() {

    }
}
