package com.lfk.justweengine.Drawable.Bone;

import com.lfk.justweengine.Engine.Engine;

import java.util.HashMap;
import java.util.Map;

/**
 * 骨骼精灵组
 * Created by Administrator on 2016/4/30.
 */
public class BoneGroupSprite {

    private Map<String, BoneSprite> spriteMap;

    private Map<String, BoneBuilder.BoneAnimGroup> boneAnimGroup;

    private BoneBuilder.BoneBuilderConfig config;

    public Map<String, BoneSprite> getSpriteMap() {
        return spriteMap;
    }

    public BoneSprite getBoneSprite(String name) {
        return spriteMap.get(name);
    }

    public void runFixedAnimation(String groupName) {
        BoneBuilder.BoneAnimGroup group = boneAnimGroup.get(groupName);
        for (int i = 0; i < group.group.length; i++) {
            BoneBuilder.BoneAnimItem item = group.group[i];
            item.boneSprite.fixedAnimation(item.animName);
        }
    }

    /**
     * 骨骼构建内部类
     */
    public static class BoneBuilder {
        public class BoneBuilderConfig {
            float positionX, positionY;

        }

        public class BoneAnimGroup {
            BoneAnimItem[] group;
            String groupName;

            public BoneAnimGroup(String groupName, BoneAnimItem... items) {
                this.groupName = groupName;
                this.group = new BoneAnimItem[items.length];
                for (int i = 0; i < group.length; i++) {
                    this.group[i] = items[i];
                }
            }
        }

        public class BoneAnimItem {
            BoneSprite boneSprite;
            String animName;

            public BoneAnimItem(BoneSprite boneSprite, String animName) {
                this.boneSprite = boneSprite;
                this.animName = animName;
            }
        }

        public final Engine engine;

        private int currentNum;

        private BoneBuilderConfig config;

        private Map<String, BoneSprite> boneSprites;
        private Map<String, BoneAnimGroup> boneAnimGroup;

        public BoneBuilder(Engine engine) {
            this.engine = engine;
            this.currentNum = 0;
            this.boneSprites = new HashMap<>();
            this.boneAnimGroup = new HashMap<>();
            this.config = new BoneBuilderConfig();
        }

        public BoneBuilder setPosition(float x, float y) {
            this.config.positionX = x;
            this.config.positionY = y;
            return this;
        }

        public BoneBuilder addBone(String name, BoneSprite sprite) {
            sprite.s_position.x += config.positionX;
            sprite.s_position.y += config.positionY;
            this.boneSprites.put(name, sprite);
            this.currentNum++;
            return this;
        }

        public BoneBuilder deleteBone(String name) {
            this.boneSprites.remove(name);
            this.currentNum--;
            return this;
        }

        public BoneBuilder addBoneAnimGroup(String name, BoneAnimGroup group) {
            this.boneAnimGroup.put(name, group);
            return this;
        }

        public BoneGroupSprite create() {
            BoneGroupSprite sprite = new BoneGroupSprite();
            for (String name : boneSprites.keySet()) {
                boneSprites.get(name).setParentGroup(sprite);
            }
            sprite.spriteMap = new HashMap<>(boneSprites);
            sprite.boneAnimGroup = new HashMap<>(boneAnimGroup);
            sprite.config = config;
            return sprite;
        }
    }
}
