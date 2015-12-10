package com.lfk.justweengine.Engine;

/**
 * Created by liufengkai on 15/12/2.
 */
public enum TouchMode {
    SINGLE(0),
    BUTTON(2),
    FULL(4),
    SINGLE_BUTTON(6);
    int mode;

    TouchMode(int type) {
        this.mode = type;
    }
}
