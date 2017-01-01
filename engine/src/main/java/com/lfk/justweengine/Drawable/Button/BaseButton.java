package com.lfk.justweengine.drawable.Button;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.renderscript.Float2;
import android.util.Log;

import com.lfk.justweengine.engine.Engine;
import com.lfk.justweengine.engine.GameTextPrinter;
import com.lfk.justweengine.info.UIdefaultData;
import com.lfk.justweengine.utils.tools.DisplayUtils;

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
    protected Float2 b_scale;
    protected OnClickListener onClickListener;
    protected BaseButtonAnimation b_baseAnim;
    //    protected int b_buttonType;
    protected boolean b_normal;

    public BaseButton(Engine b_engine, String name) {
        this(b_engine, 0, 0, name);
    }

    public BaseButton(Engine b_engine, int b_width, int b_height, String name) {
        this.b_engine = b_engine;
        this.b_width = b_width;
        this.b_height = b_height;
        this.b_name = name;
        init();
    }

    private void init() {
        b_normal = false;
//        this.b_buttonType = BaseButtonAnimation.NORMAL;
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

    public void setScale(Float2 scale) {
        b_scale = scale;
        Log.e("scale", b_scale.x + ":" + b_scale.y);
    }

    public void setScale(float x, float y) {
        this.b_scale.x = x;
        this.b_scale.y = y;
    }

    public void setDipWidth(int w) {
        this.b_scale.x = DisplayUtils.dip2px(w);
    }

    public void setDipHeight(int h) {
        this.b_scale.y = DisplayUtils.dip2px(h);
    }

    public BaseButtonAnimation getAnimation() {
        return b_baseAnim;
    }

    public void setNormal(boolean b_normal) {
    }
}
