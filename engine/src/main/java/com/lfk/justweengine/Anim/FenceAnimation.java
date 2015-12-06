package com.lfk.justweengine.Anim;

import android.graphics.Rect;
import android.renderscript.Float2;

/**
 * 围栏使用动画防止出界
 *
 * @author liufengkai
 *         Created by liufengkai on 15/12/4.
 */
public class FenceAnimation extends BaseAnim {
    private Rect fence;

    public FenceAnimation(Rect fence) {
        this.fence = fence;
        animating = true;
        animType = AnimType.POSITION;
    }

    @Override
    public Float2 adjustPosition(Float2 ori) {
        if (ori.x < fence.left)
            ori.x = fence.left;
        else if (ori.x > fence.right)
            ori.x = fence.right;
        else if (ori.y < fence.top)
            ori.y = fence.top;
        else if (ori.y > fence.bottom)
            ori.y = fence.bottom;
        return ori;
    }
}
