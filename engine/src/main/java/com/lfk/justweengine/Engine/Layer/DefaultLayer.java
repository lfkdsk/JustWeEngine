package com.lfk.justweengine.Engine.Layer;

import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import com.lfk.justweengine.Drawable.Bone.BoneGroupSprite;
import com.lfk.justweengine.Drawable.Sprite.BaseSub;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 默认实现的绘制层
 *
 * @author liufengkai
 *         Created by liufengkai on 16/5/8.
 */
public class DefaultLayer extends Layer {
    // 对象绘制组
    private CopyOnWriteArrayList<BaseSub> l_sprite_group;
    // 对象回收组
    private CopyOnWriteArrayList<BaseSub> l_sprite_recycle_group;

    public DefaultLayer(Screen screen, Rect layerField) {
        super(LayerType.Default, screen, layerField);
        init();
    }

    public DefaultLayer(LayerType type, Screen screen, int x, int y, int w, int h) {
        super(type, screen, new Rect(x, y, w, h));
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        l_sprite_group = new CopyOnWriteArrayList<>();
        l_sprite_recycle_group = new CopyOnWriteArrayList<>();
    }

    @Override
    protected void layerUpdate() {
        for (BaseSub A : l_sprite_group) {
            if (!A.getAlive()) continue;

            if (!A.isCollidable()) continue;

            if (A.isCollided()) continue;

            for (BaseSub B : l_sprite_group) {
                if (!B.getAlive()) continue;

                if (!B.isCollidable()) continue;

                if (B.isCollided()) continue;

                if (A == B) continue;

                if (A.getIdentifier() ==
                        B.getIdentifier())
                    continue;

                if (collisionCheck(A, B)) {
                    A.setCollided(true);
                    A.setOffender(B);
                    B.setCollided(true);
                    B.setOffender(A);
                    break;
                }
            }
        }
    }

    @Override
    protected void layerDraw() {
        for (BaseSub baseSub : l_sprite_group) {
            if (baseSub.getAlive()) {
                baseSub.animation();
                baseSub.draw();
            }
            baseSub.debugDraw();
        }
    }

    @Override
    protected void layerCollision() {
        // new collision
        for (BaseSub baseSub : l_sprite_group) {
            if (!baseSub.getAlive()) {
                l_sprite_recycle_group.add(baseSub);
                l_sprite_group.remove(baseSub);
                continue;
            }

            if (baseSub.isCollidable()) {
                if (baseSub.isCollided()) {
                    // Is it a valid object ?
                    if (baseSub.getOffender() != null) {
                        // collision
                        if (layerListener != null)
                            layerListener.Collision(baseSub);
                        // reset offender
                        baseSub.setOffender(null);
                    }
                    baseSub.setCollided(false);
                }
            }

            baseSub.setCollided(false);
        }

    }

    @Override
    protected void layerClick(MotionEvent event) {

    }

    /**
     * 检测碰撞
     *
     * @param A a 物体
     * @param B b 物体
     * @return 是否碰撞
     */
    private boolean collisionCheck(BaseSub A, BaseSub B) {
        return RectF.intersects(A.getBounds(), B.getBounds());
    }

    /**
     * add BaseSub to group
     *
     * @param sprite
     */
    public void addToSpriteGroup(BaseSub sprite) {
        l_sprite_group.add(sprite);
    }

    /**
     * add boneSprite
     *
     * @param sprite
     */
    public void addToSpriteGroup(BoneGroupSprite sprite) {
        for (String name : sprite.getSpriteMap().keySet()) {
            addToSpriteGroup(sprite.getBoneSprite(name));
        }
    }

    /**
     * remove from group
     *
     * @param sprite
     */
    public void removeFromSpriteGroup(BaseSub sprite) {
        l_sprite_group.remove(sprite);
    }

    public void removeFromSpriteGroup(int index) {
        l_sprite_group.remove(index);
    }

    /**
     * get size
     *
     * @return size
     */
    public int getSpriteGroupSize() {
        return l_sprite_group.size();
    }

    public int getRecycleGroupSize() {
        return l_sprite_recycle_group.size();
    }

    public void addToRecycleGroup(BaseSub baseSub) {
        l_sprite_recycle_group.add(baseSub);
    }

    public void removeFromRecycleGroup(int index) {
        l_sprite_recycle_group.remove(index);
    }

    public void removeFromRecycleGroup(BaseSub baseSub) {
        l_sprite_recycle_group.remove(baseSub);
    }

    public boolean isRecycleGroupEmpty() {
        return l_sprite_recycle_group.isEmpty();
    }

    public BaseSub recycleSubFromGroup(int id) {
        for (BaseSub baseSub : l_sprite_recycle_group) {
            if (baseSub.getIdentifier() == id) {
                return baseSub;
            }
        }
        return null;
    }


    public int getTypeSizeFromRecycleGroup(int id) {
        int num = 0;
        for (BaseSub baseSub : l_sprite_recycle_group) {
            if (baseSub.getIdentifier() == id) {
                num++;
            }
        }
        Log.e("num" + num, "id" + id);
        return num;
    }

}
