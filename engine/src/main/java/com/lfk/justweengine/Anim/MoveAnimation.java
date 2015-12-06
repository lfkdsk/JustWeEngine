package com.lfk.justweengine.Anim;

import android.renderscript.Float2;
import android.util.Log;

/**
 * Created by liufengkai on 15/12/3.
 */
public class MoveAnimation extends BaseAnim {
    private Float2 velocity;
    private float toX;
    private float toY;

    public MoveAnimation(
            float toX, float toY,
            Float2 velocity) {
        this.toX = toX;
        this.toY = toY;
        this.velocity = velocity;
        animating = true;
        animType = AnimType.POSITION;
    }

    @Override
    public Float2 adjustPosition(Float2 ori) {
        if (ori.x != toX) {
            if (ori.x > toX)
                ori.x -= velocity.x;
            else
                ori.x += velocity.x;
        }
        if (ori.y != toY) {
            Log.d("ori.y" + ori.y, "toY" + toY);
            if (ori.y > toY)
                ori.y -= velocity.y;
            else
                ori.y += velocity.y;
        }
        if (Math.abs(ori.x - toX) < velocity.x &&
                Math.abs(ori.y - toY) < velocity.y) {
            animating = false;
        }
        return ori;
    }
}
