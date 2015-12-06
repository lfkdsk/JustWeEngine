package com.lfk.justweengine.Anim;

import android.renderscript.Float2;

import com.lfk.justweengine.Info.UIdefaultData;

/**
 * Created by liufengkai on 15/12/5.
 */
public class ShootAnimation extends BaseAnim {
    private float speed;
    private float y;

    public ShootAnimation(float y, float v) {
        this.speed = v;
        this.y = y;
        animating = true;
        animType = AnimType.SHOOT;
    }

    @Override
    public Float2 adjustPosition(Float2 original) {
        y = original.y += speed;
        return original;
    }

    @Override
    public boolean adjustAlive(boolean ori) {
        if (y < 0 || y > UIdefaultData.screenHeight) {
            ori = false;
        }
        return ori;
    }
}
