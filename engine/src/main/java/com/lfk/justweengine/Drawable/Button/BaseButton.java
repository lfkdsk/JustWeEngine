package com.lfk.justweengine.Drawable.Button;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Engine.GameTextPrinter;
import com.lfk.justweengine.Info.UIdefaultData;

/**
 * Button 基类
 *
 * @author liufengkai
 *         Created by liufengkai on 15/12/2.
 */
public abstract class BaseButton extends GameTextPrinter {
    protected Engine b_engine;
    protected int b_width, b_height;
    public Point b_position;
    protected Paint paint;
    public Rect b_rect;
    protected String b_name;
    protected OnClickListener onClickListener;

    public BaseButton(Engine b_engine) {
        this(b_engine, 0, 0);
    }

    public BaseButton(Engine b_engine, int b_width, int b_height) {
        this.b_engine = b_engine;
        this.b_width = b_width;
        this.b_height = b_height;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(UIdefaultData.sprite_default_color_paint);
    }

    public abstract void draw();

    public abstract void animation();

    public String getName() {
        return b_name;
    }

    public void setName(String b_name) {
        this.b_name = b_name;
    }

    public Rect getRect() {
        return b_rect;
    }

    public void onClick(boolean flag) {
        if (flag) {
            onClickListener.onClick();
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
