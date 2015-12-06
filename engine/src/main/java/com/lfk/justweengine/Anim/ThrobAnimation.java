package com.lfk.justweengine.Anim;

import android.renderscript.Float2;

/**
 * 跳跃动画
 *
 * @author liufengkai
 *         Created by liufengkai on 15/11/29.
 */
public class ThrobAnimation extends BaseAnim {
    private float startScale, endScale, speed;
    private boolean started;

    public ThrobAnimation(float startScale, float endScale, float speed) {
        this.startScale = startScale;
        this.endScale = endScale;
        this.speed = speed;
        animType = AnimType.SCALE;
        started = false;
        animating = true;
    }

    @Override
    public Float2 adjustScale(Float2 ori) {
        if (!started) {
            ori.x = startScale;
            ori.y = endScale;
            started = true;
        }
        ori.x += speed;
        ori.y += speed;
        if (ori.x >= endScale) {
            speed *= -1;
        } else if (ori.x <= startScale) {
            animating = false;
        }
        return ori;
    }
}
