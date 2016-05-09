package com.lfk.justweengine.Engine.Layer;

import android.graphics.Rect;
import android.view.MotionEvent;

import com.lfk.justweengine.Drawable.Button.BaseButton;

import java.util.HashMap;

/**
 * Created by liufengkai on 16/5/8.
 */
public class ButtonLayer extends Layer {
    // 按钮组
    private HashMap<String, BaseButton> e_button_group;

    private boolean e_is_hit_button;
    private BaseButton e_hit_button = null;

    public ButtonLayer(Screen layerScreen, Rect layerField) {
        super(LayerType.Button, layerScreen, layerField);
        init();
    }

    public ButtonLayer(Screen layerScreen, int x, int y, int w, int h) {
        super(LayerType.Button, layerScreen, new Rect(x, y, w, h));
        init();
    }

    private void init() {
        e_is_hit_button = false;
        e_hit_button = null;
        e_button_group = new HashMap<>();
    }

    @Override
    protected void layerUpdate() {

    }

    @Override
    protected void layerDraw() {
        for (String o : e_button_group.keySet()) {
            BaseButton button = e_button_group.get(o);
            button.animation();
            button.draw();
        }
    }

    @Override
    protected void layerCollision() {

    }


    @Override
    protected void layerClick(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (String name : e_button_group.keySet()) {
                    BaseButton button = e_button_group.get(name);
                    if (button.getRect().contains((int) event.getX(),
                            (int) event.getY())) {
                        button.setNormal(false);
                        e_is_hit_button = true;
                        e_hit_button = button;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (e_hit_button != null && e_is_hit_button &&
                        e_hit_button.getRect().contains((int) event.getX(),
                                (int) event.getY())) {
                    e_hit_button.setNormal(true);
                    e_hit_button.onClick(true);
                    resetHitButton();
                } else if (e_is_hit_button) {
                    if (e_hit_button != null) {
                        e_hit_button.setNormal(true);
                        resetHitButton();
                    }
                }
                break;
        }
    }

    public void addToButtonGroup(BaseButton button) {
        e_button_group.put(button.getName(), button);
    }

    public void removeButtonFromGroup(String name) {
        e_button_group.remove(name);
    }

    private void resetHitButton() {
        e_is_hit_button = false;
        e_hit_button = null;
    }
}
