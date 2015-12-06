package com.lfk.justweengine.Anim;

/**
 * 逐帧动画
 *
 * @author liufengkai
 *         Created by liufengkai on 15/11/29.
 */
public class FrameAnimation extends BaseAnim {
    private int firstFrame;
    private int lastFrame;
    private int direction;

    public FrameAnimation(int firstFrame, int lastFrame, int direction) {
        this.firstFrame = firstFrame;
        this.lastFrame = lastFrame;
        this.direction = direction;
        this.animType = AnimType.FRAME;
        this.animating = true;
    }

    @Override
    public int adjustFrame(int ori) {
        int modified = ori + direction;
        if (modified < firstFrame) {
            modified = lastFrame;
        } else if (modified > lastFrame) {
            modified = firstFrame;
        }
        return modified;
    }
}
