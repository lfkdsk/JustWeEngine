package com.lfk.justweengine.Drawable.Button;

import android.graphics.Rect;

import com.lfk.justweengine.Anim.AnimType;

/**
 * Created by liufengkai on 15/12/12.
 */
public class ZoomCenterButtonAnim extends BaseButtonAnimation {
    private int from;
    private int to;
    private int frame;
    private float speed;
    private int currentFrame;
    private boolean start;
    private Rect rect;

    public ZoomCenterButtonAnim(int from, int to, int frame) {
        this.from = from;
        this.to = to;
        this.frame = frame;
        this.currentFrame = 0;
        this.speed = (to - from) * 1.0f / frame;
        animating = false;
        animType = AnimType.ZOOM_CENTER;
        start = true;
    }

    @Override
    public Rect adjustButtonRect(Rect ori, boolean touchType) {
        Rect modify = new Rect(ori);
        if (start) {
            rect = new Rect(modify);
            start = false;
        }

        if (touchType) {
            animating = false;
            currentFrame = 0;
            return rect;
        }

        if (currentFrame <= frame) {
            modify.top -= speed;
            modify.left -= speed;
            modify.right += speed;
            modify.bottom += speed;
            currentFrame++;
        } else {
            animating = false;
            currentFrame = 0;
        }
        return modify;
    }
}
