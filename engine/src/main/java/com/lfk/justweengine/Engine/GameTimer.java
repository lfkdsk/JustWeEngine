package com.lfk.justweengine.Engine;

/**
 * 计时器
 *
 * 控制帧数\节律
 * @author liufengkai
 *         Created by liufengkai on 15/11/27.
 */
public class GameTimer {
    private long e_start;
    private long e_stopWatchStart;

    public GameTimer() {
        e_start = System.currentTimeMillis();
        e_stopWatchStart = 0;
    }

    /**
     * get time from start to now
     *
     * @return time from start to now
     */
    public long getElapsed() {
        return System.currentTimeMillis() - e_start;
    }

    /**
     * rest time
     *
     * @param ms
     */
    public void rest(int ms) {
        long start = getElapsed();
        while (start + ms > getElapsed()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
//                Logger.e("engine gameTimer error:" + e);
            }
        }
    }

    public void resetStop() {
        e_stopWatchStart = getElapsed();
    }


    public boolean stopWatch(long ms) {
        if (getElapsed() > e_stopWatchStart + ms) {
            resetStop();
            return true;
        } else
            return false;
    }
}
