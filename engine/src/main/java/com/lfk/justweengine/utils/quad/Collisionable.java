package com.lfk.justweengine.utils.quad;

/**
 * Created by liufengkai on 16/9/18.
 */
public interface Collisionable<T> {
    boolean isCollision(T object);
}
