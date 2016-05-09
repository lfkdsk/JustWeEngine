package com.lfk.justweengine.Engine.Layer;

import android.graphics.Rect;
import android.view.MotionEvent;

import com.lfk.justweengine.Drawable.Sprite.BaseSub;

/**
 * Layer 绘制分层
 * 负责数据处理
 * Created by liufengkai on 16/5/8.
 */
public abstract class Layer {
    public enum LayerType {
        Button,
        Default
    }

    public interface LayerListener {
        boolean Touch(MotionEvent event);

        void Collision(BaseSub baseSub);

        void Update();

        void Draw();
    }


    protected LayerType layerType;

    protected String layerName;
    // Layer的刷新范围也是绘制范围
    protected Rect layerField;

    protected Screen layerScreen;

    protected int layerBackgroundColor;

    protected LayerListener layerListener;

    protected abstract void layerUpdate();

    protected abstract void layerDraw();

    protected abstract void layerCollision();

    protected abstract void layerClick(MotionEvent event);

    public Layer(LayerType layerType, Screen layerScreen, Rect layerField) {
        this.setLayerType(layerType);
        this.layerField = layerField;
        this.layerScreen = layerScreen;
    }

    public Rect getLayerField() {
        return layerField;
    }

    public void setLayerField(Rect layerField) {
        this.layerField = layerField;
    }

    public LayerType getLayerType() {
        return layerType;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public void setLayerType(LayerType layerType) {
        this.layerType = layerType;
    }

    public int getLayerBackgroundColor() {
        return layerBackgroundColor;
    }

    public void setLayerBackgroundColor(int layerBackgroundColor) {
        this.layerBackgroundColor = layerBackgroundColor;
    }

    public LayerListener getLayerListener() {
        return layerListener;
    }

    public void setLayerListener(LayerListener layerListener) {
        this.layerListener = layerListener;
    }
}
