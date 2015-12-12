package com.lfk.justweengine.Drawable.Button;

import com.lfk.justweengine.Anim.AnimType;

/**
 * Created by liufengkai on 15/12/12.
 */
public class ColorAnimation extends BaseButtonAnimation {
    private int pressed_color, unpressed_color;

    public ColorAnimation(int unpressed_color, int pressed_color) {
        this.pressed_color = pressed_color;
        this.unpressed_color = unpressed_color;
        animating = false;
        animType = AnimType.COLOR;
    }

    @Override
    public int adjustButtonBackGround(int ori, boolean type) {
        if (type) {
            return unpressed_color;
        } else {
            return pressed_color;
        }
    }
}
