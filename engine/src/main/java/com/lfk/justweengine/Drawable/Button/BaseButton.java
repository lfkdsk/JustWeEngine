package com.lfk.justweengine.Drawable.Button;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Info.UIdefaultData;

/**
 * Created by liufengkai on 15/12/2.
 */
public abstract class BaseButton {
    protected Engine b_engine;
    protected Canvas b_canvas;
    protected int b_width, b_height;
    public Point b_position;
    protected Paint paint;
    public Rect b_rect;

    public BaseButton(Engine b_engine) {
        this(b_engine, 0, 0);
    }

    public BaseButton(Engine b_engine, int b_width, int b_height) {
        this.b_engine = b_engine;
        this.b_width = b_width;
        this.b_height = b_height;
    }

    private void init() {
        paint = new Paint();
        paint.setColor(UIdefaultData.sprite_default_color_paint);
    }

    public abstract void draw();

    public abstract void animation();

}
