package com.lfk.justweengine.Engine;

/**
 * TouchMode
 *
 * @author liufengkai
 *         Created by liufengkai on 15/12/2.
 */
public enum TouchMode {
    // Single touch
    SINGLE(0),
    // Single button
    BUTTON(2),
    // Touches
    FULL(4),
    // Single touch + button
    SINGLE_BUTTON(6),
    // distribute Touch Event In Layer
    SINGLE_LAYER(8);
    int mode;

    TouchMode(int type) {
        this.mode = type;
    }
}
