package com.lfk.justweengine.Anim;

/**
 * 旋转动画
 *
 * @author liufengkai
 *         Created by liufengkai on 15/11/29.
 */
public class SpinAnimation extends BaseAnim {
    private float angleDist, velocity;

    public SpinAnimation(float velocity) {
        animating = true;
        angleDist = 0.0f;
        this.velocity = velocity;
    }

    @Override
    public float adjustRotation(float ori) {
        float modified = ori;
        float fullCircle = (float) (2.0 * Math.PI);
        angleDist += velocity;
        if (angleDist > fullCircle)
            animating = false;
        modified += velocity;
        return modified;
    }
}
