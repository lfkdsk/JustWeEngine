package com.lfk.justweengine.Anim;

import com.lfk.justweengine.Engine.GameTimer;

/**
 * 判断生死
 *
 * @author liufengkai
 *         Created by liufengkai on 15/12/5.
 */
public class AliveAnimation extends BaseAnim {
    private int liftTime;
    private GameTimer timer;

    public AliveAnimation(int liftTime) {
        this.liftTime = liftTime;
        this.timer = new GameTimer();
        animating = true;
        animType = AnimType.ALIVE;
    }

    @Override
    public boolean adjustAlive(boolean ori) {
        if (liftTime > 0 && timer.stopWatch(liftTime)) {
            ori = false;
        }
        return ori;
    }
}
