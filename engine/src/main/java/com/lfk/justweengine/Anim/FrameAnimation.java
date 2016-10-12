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
    private int interval;//帧动画切换时间间隔，单位ms
    private GameTimer timer;

    public FrameAnimation(int firstFrame, int lastFrame, int direction) {
        this.firstFrame = firstFrame;
        this.lastFrame = lastFrame;
        this.direction = direction;
        this.animType = AnimType.FRAME;
        this.animating = true;
        this.interval = 0;
        timer = new GameTimer();
    }
    
    public FrameAnimation(int firstFrame, int lastFrame, int direction, int interval) {
        this.firstFrame = firstFrame;
        this.lastFrame = lastFrame;
        this.direction = direction;
        this.animType = AnimType.FRAME;
        this.animating = true;
        this.interval = interval;
        timer = new GameTimer();
    }

    @Override
    public int adjustFrame(int ori) {
        int modified = ori;
        if (timer.stopWatch(interval)) {
            modified += direction;
            if (modified < firstFrame) {
                modified = lastFrame;
            } else if (modified > lastFrame) {
                modified = firstFrame;
            }
        }
        return modified;
    }
}
