package com.lfk.justweengine.anim;

import android.renderscript.Float2;


/**
 * 按钮放大缩小动画
 *
 * @author liufengkai
 *         Created by liufengkai on 15/12/10.
 */
public class ZoomAnimation extends BaseAnim {
    private float from;
    private float to;
    private float speed;
    private boolean start;
    public int touchType;
    private boolean changeType;

    // speed永为正值   
    public ZoomAnimation(float from, float to, float speed) {
        this.from = from;
        this.to = to;
        this.speed = speed;
        this.start = false;
        this.animating = true;
        changeType = false;
        animType = AnimType.ZOOM;
    }

    @Override
    public Float2 adjustScale(Float2 ori) {
        if (!start) {
            start = true;
        }

        if (changeType) {
            animating = false;
            changeType = false;
            return ori;
        }

        if (from < to) { // 放大
            if (from + speed < to) {
                from += speed;
            } else {
                from = to;
            }
        } else if (from > to) { // 缩小
            if (from - speed > to) {
                from -= speed;
            } else {
                from = to;
            }
        }

        if (from == to) {
            touchType = -touchType;
            changeType = true;
        }

        ori.x = from;
        ori.y = from;
        return ori;
    }

    @Override
    public float adjustTag(float ori) {
        return touchType;
    }
}
