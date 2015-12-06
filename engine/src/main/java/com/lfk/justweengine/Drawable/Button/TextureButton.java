package com.lfk.justweengine.Drawable.Button;

import android.graphics.Point;
import android.graphics.Rect;
import android.renderscript.Float2;

import com.lfk.justweengine.Anim.BaseAnim;
import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Engine.GameTexture;

/**
 * 图片Button
 *
 * @author liufengkai
 *         Created by liufengkai on 15/12/2.
 */
public class TextureButton extends BaseButton {
    private GameTexture texture;
    private Float2 b_scale;
    private BaseAnim b_baseAnim;
    private int b_alpha;

    public TextureButton(Engine b_engine) {
        super(b_engine);
        init();
    }


    public TextureButton(Engine b_engine, int b_width, int b_height) {
        super(b_engine, b_width, b_height);
        init();
    }

    private void init() {
        b_alpha = 255;
        b_canvas = null;
        texture = new GameTexture(b_engine);
        b_position = new Point(0, 0);
        b_scale = new Float2(1.0f, 1.0f);

    }

    @Override
    public void draw() {
        b_canvas = b_engine.getCanvas();

        if (b_width == 0 || b_height == 0) {
            b_width = texture.getBitmap().getWidth();
            b_height = texture.getBitmap().getHeight();
        }

        Rect src = new Rect(0, 0, b_width, b_height);

        int x = b_position.x;
        int y = b_position.y;
        int w = (int) (b_width * b_scale.x);
        int h = (int) (b_height * b_scale.y);

        b_rect = new Rect(x, y, w, h);

        b_canvas.drawBitmap(texture.getBitmap(), src, b_rect, paint);
    }

    @Override
    public void animation() {

    }

    public void setPosition(Point b_position) {
        this.b_position = b_position;
    }

    public void setPosition(int x, int y) {
        b_position.x = x;
        b_position.y = y;
    }
}
