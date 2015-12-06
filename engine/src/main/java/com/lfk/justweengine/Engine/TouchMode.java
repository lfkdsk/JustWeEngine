package com.lfk.justweengine.Engine;

/**
 * Created by liufengkai on 15/12/2.
 */
public enum TouchMode {
    SINGLE(0),
    FULL(5);
    int mode;

    TouchMode(int type) {
        this.mode = type;
    }
}
