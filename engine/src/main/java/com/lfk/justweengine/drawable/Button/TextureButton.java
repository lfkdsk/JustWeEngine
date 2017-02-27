package com.lfk.justweengine.drawable.button;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.renderscript.Float2;

import com.lfk.justweengine.engine.Engine;
import com.lfk.justweengine.engine.GameTexture;
import com.lfk.justweengine.utils.tools.DisplayUtils;

/**
 * 图片Button
 *
 * @author liufengkai
 *         Created by liufengkai on 15/12/2.
 */
public class TextureButton extends BaseButton {
    private GameTexture texture;
    private int b_alpha;
    private boolean zoomCenter;
    private boolean firstInit;

    public TextureButton(Engine b_engine, String name) {
        super(b_engine, name);
        init();
    }


    public TextureButton(Engine b_engine, String name, int b_width, int b_height) {
        super(b_engine, b_width, b_height, name);
        init();
    }

    private void init() {
        b_alpha = 255;
        zoomCenter = false;
        firstInit = false;
        texture = new GameTexture(b_engine);
        b_position = new Point(0, 0);
        b_scale = new Float2(1.0f, 1.0f);
    }

    @Override
    public void draw() {
        e_canvas = b_engine.getCanvas();
        if (b_width == 0 || b_height == 0) {
            b_width = texture.getBitmap().getWidth();
            b_height = texture.getBitmap().getHeight();
        }

        Rect src = new Rect(0, 0, b_width, b_height);

        int x = b_position.x;
        int y = b_position.y;
        int w = (int) (b_width * b_scale.x);
        int h = (int) (b_height * b_scale.y);

        if (!firstInit) {
            b_rect = new Rect(x, y, x + w, y + h);
            firstInit = true;
        }

        if (!zoomCenter) {
            b_rect = new Rect(x, y, x + w, y + h);
        }

        paint.setAlpha(b_alpha);
        e_canvas.drawBitmap(texture.getBitmap(), src, b_rect, paint);

        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void animation() {
        if (b_baseAnim != null && b_baseAnim.animating) {
            doAnimation();
        }
    }

    private void doAnimation() {
        switch (b_baseAnim.animType) {
            case ZOOM_CENTER:
                b_rect = b_baseAnim.adjustButtonRect(b_rect, b_normal);
                break;
            case MASK:

                break;
        }
    }

    public void setPosition(Point b_position) {
        this.b_position = b_position;
    }

    public void setPosition(int x, int y) {
        this.b_position.x = x;
        this.b_position.y = y;
    }

    public void setZoomCenter(boolean zoomCenter) {
        this.zoomCenter = zoomCenter;
    }

    public void setTexture(GameTexture texture) {
        this.texture = texture;
    }

    public void setAnimation(BaseButtonAnimation anim) {
        this.b_baseAnim = anim;
    }

    public void setDipScale(int dipW, int dipH) {
        if (b_width == 0 || b_height == 0) {
            b_width = texture.getBitmap().getWidth();
            b_height = texture.getBitmap().getHeight();
        }
        setScale(new Float2(DisplayUtils.dip2px(dipW) * 1.0f / b_width,
                DisplayUtils.dip2px(dipH) * 1.0f / b_height));
    }

    @Override
    public void setNormal(boolean b_normal) {
        this.b_normal = b_normal;
        this.b_baseAnim.animating = true;
    }
}
