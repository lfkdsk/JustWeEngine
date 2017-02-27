package com.lfk.justweengine.drawable.bone;

import com.lfk.justweengine.drawable.sprite.BaseSprite;
import com.lfk.justweengine.drawable.sprite.FrameType;
import com.lfk.justweengine.engine.Engine;

/**
 * 骨骼精灵
 * 坐标为相对坐标
 * Created by Administrator on 2016/4/30.
 */
public class BoneSprite extends BaseSprite {

    public BoneGroupSprite parentGroup;

    /**
     * @param engine
     * @param w
     * @param h
     * @param columns
     */
    public BoneSprite(Engine engine, int w, int h, int columns) {
        super(engine, w, h, columns);

    }

    public BoneSprite(Engine engine) {
        super(engine);

    }

    public BoneSprite(Engine engine, int w, int h, FrameType type) {
        super(engine, w, h, type);

    }

    public void setParentGroup(BoneGroupSprite parentGroup) {
        this.parentGroup = parentGroup;
    }
}
