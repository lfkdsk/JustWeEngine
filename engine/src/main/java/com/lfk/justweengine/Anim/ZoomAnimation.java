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
        start = false;
        animating = false;
        changeType = false;
        animType = AnimType.ZOOM;
    }

    @Override
    public Float2 adjustScale(Float2 ori) {
        if (!start) {
            from = ori.x;
            start = true;
        }

        if (changeType) {
            animating = false;
            changeType = false;
            return ori;
        }

        if (ori.x == to && ori.y == to) {
            touchType = -touchType;
            float temp = to;
            to = from;
            from = temp;
            changeType = true;
            return ori;
        }

        if (from < to) { // 放大
            if (ori.x + speed <= to) {
                ori.x += speed;
                ori.y += speed;
            } else {
                ori.x = to;
                ori.y = to;
            }
        } else if (from > to) { // 缩小
            if (ori.x - speed >= to) {
                ori.x -= speed;
                ori.x -= speed;
            } else {
                ori.x = to;
                ori.y = to;
            }
        }
        return ori;
    }

    @Override
    public float adjustTag(float ori) {
        return touchType;
    }
}
