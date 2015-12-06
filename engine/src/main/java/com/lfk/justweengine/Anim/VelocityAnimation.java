

package com.lfk.justweengine.Anim;

import android.renderscript.Float2;

import com.lfk.justweengine.Engine.GameTimer;

public class VelocityAnimation extends BaseAnim {
    private double p_angle;
    private double p_multiplier;
    private double p_velX, p_velY;
    private int p_lifetime;
    private GameTimer p_timer;

    public VelocityAnimation(double angleDegrees, float speedMultiplier,
                             int lifetime) {
        animating = true;
        animType = AnimType.SHOOT;
        p_lifetime = lifetime;
        p_timer = new GameTimer();
        p_angle = angleDegrees;
        p_multiplier = speedMultiplier;
        double angleRadians = Math.toRadians(p_angle);
        //calculate X velocity
        p_velX = Math.cos(angleRadians) * p_multiplier;
        //calculate Y velocity
        p_velY = Math.sin(angleRadians) * p_multiplier;
    }

    @Override
    public Float2 adjustPosition(Float2 original) {
        Float2 modified = new Float2(original.x, original.y);
        modified.x += p_velX;
        modified.y += p_velY;
        return modified;
    }

    @Override
    public boolean adjustAlive(boolean original) {
        boolean modified = original;
        if (p_lifetime > 0) {
            if (p_timer.stopWatch(p_lifetime)) {
                modified = false;
            }
        }
        return modified;
    }
}

