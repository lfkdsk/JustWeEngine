package com.lfk.justweengine.Anim;

import android.graphics.Point;
import android.graphics.Rect;
import android.renderscript.Float2;

/**
 * Created by liufengkai on 15/11/29.
 */
public class WrapMoveAnimation extends BaseAnim {
    private Rect bounds;
    private Float2 velocity;
    private Point size;

    public WrapMoveAnimation(Rect bounds, Point size, Float2 velocity) {
        animating = true;
        animType = AnimType.WRAPMOVE;
        this.bounds = bounds;
        this.size = size;
        this.velocity = velocity;
    }

    @Override
    public Float2 adjustPosition(Float2 ori) {
        ori.x += velocity.x;
        ori.y += velocity.y;

        if (ori.x < bounds.left) {
            ori.x = bounds.right - size.x;
        } else if (ori.x > bounds.right - size.x) {
            ori.x = bounds.left;
        }
        if (ori.y < bounds.top) {
            ori.y = bounds.bottom - size.y;
        } else if (ori.y > bounds.bottom - size.y) {
            ori.y = bounds.top;
        }
        return ori;
    }
}
