package com.lfk.justweengine.Drawable.Sprite;

import com.lfk.justweengine.Anim.BaseAnim;
import com.lfk.justweengine.Engine.Engine;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liufengkai on 16/1/15.
 */
public class StateSprite extends BaseSprite {
    private ConcurrentHashMap<StateFinder, BaseAnim> b_state;

    public StateSprite(Engine engine) {
        super(engine);
        initState();
    }

    public StateSprite(Engine engine, int w, int h, FrameType type) {
        super(engine, w, h, type);
        initState();
    }

    public StateSprite(Engine engine, int w, int h, int columns) {
        super(engine, w, h, columns);
        initState();
    }


    @Override
    public void animation() {
        for (StateFinder state : b_state.keySet()) {
            if (state.isContent(this)) {
                BaseAnim anim = b_state.get(state);
                if (anim.animating) {
                    doAnimation(anim);
                }
            }
        }
    }

    protected void initState() {
        b_state = new ConcurrentHashMap<>();
    }

    public void addState(StateFinder finder, BaseAnim anim) {
        b_state.put(finder, anim);
    }

    public void removeState(StateFinder finder) {
        b_state.remove(finder);
    }
}
