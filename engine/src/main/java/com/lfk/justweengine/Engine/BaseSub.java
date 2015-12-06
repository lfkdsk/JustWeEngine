package com.lfk.justweengine.Engine;

import android.graphics.RectF;

/**
 * 游戏中的抽象基类
 *
 * @author liufengkai
 *         Created by liufengkai on 15/12/4.
 */
public abstract class BaseSub {
    public abstract boolean getAlive();

    public abstract void draw();

    public abstract void animation();

    public abstract boolean isCollidable();

    public abstract boolean isCollided();

    public abstract void setCollidable(boolean s_collidable);

    public abstract void setCollided(boolean s_collided);

    public abstract BaseSub getOffender();

    public abstract void setOffender(BaseSub e_offender);

    public abstract RectF getBounds();

    public abstract int getIdentifier();

    public abstract void setAlive(boolean s_alive);

}
