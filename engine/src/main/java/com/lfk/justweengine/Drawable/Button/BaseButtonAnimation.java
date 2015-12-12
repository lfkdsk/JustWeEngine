package com.lfk.justweengine.Drawable.Button;

import android.graphics.Rect;

import com.lfk.justweengine.Anim.AnimType;
import com.lfk.justweengine.Anim.BaseAnim;

/**
 * Created by liufengkai on 15/12/12.
 */
public abstract class BaseButtonAnimation extends BaseAnim {
    // Is it running ?
    public boolean animating;
    // anim type
    public AnimType animType;

    public static final int UP = 1;
    public static final int DOWN = -1;
    public static final int NORMAL = 0;

    // init
    public BaseButtonAnimation() {
        animating = false;
        animType = AnimType.NULL;
    }

    // center zoom
    public Rect adjustButtonRect(Rect rect, boolean type) {
        return rect;
    }

    public int adjustButtonBackGround(int ori, boolean type) {
        return ori;
    }

}
