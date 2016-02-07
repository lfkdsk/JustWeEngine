package com.lfk.justweengine.Drawable.Button;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.renderscript.Float2;

import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Utils.tools.DisplayUtils;

/**
 * TextButton
 *
 * @author liufengkai
 *         Created by liufengkai on 15/12/12.
 */
public class TextButton extends BaseButton {
    // text color / button color
    private int b_text_Color, b_button_Color;
    // text
    private String b_text;
    // zoom in center
    private boolean b_zoomCenter, b_firstInit;
    private Paint b_textPaint;
    private float b_textWidth, b_singleWidth;

    /**
     * TextButton
     *
     * @param b_engine engine context
     * @param name     textButton name
     */
    public TextButton(Engine b_engine, String name) {
        super(b_engine, name);
        init();
    }

    /**
     * TextButton
     *
     * @param b_engine engine context
     * @param b_width  w
     * @param b_height h
     * @param name     textButton name
     */
    public TextButton(Engine b_engine, int b_width, int b_height, String name) {
        super(b_engine, b_width, b_height, name);
        init();
    }

    private void init() {
        b_text = "";
        b_text_Color = Color.WHITE;
        b_button_Color = Color.TRANSPARENT;
        b_zoomCenter = false;
        b_firstInit = false;
        b_position = new Point(110, 110);
        b_scale = new Float2(1.0f, 1.0f);

        b_textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        b_textPaint.setColor(b_text_Color);
        b_textPaint.setTextSize(40);

        paint.setColor(b_button_Color);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void draw() {
        e_canvas = b_engine.getCanvas();
        if (b_width == 0 || b_height == 0) {
            float[] widths = new float[1];
            // 获取单个汉字的宽度
            b_textPaint.getTextWidths("蛤", widths);
            b_text = b_text != null ? b_text : "";
            b_textWidth = widths[0] * b_text.length();
            b_singleWidth = widths[0];
            b_width = (int) (b_text.length() * widths[0] + 2 * DisplayUtils.dip2px(16));
            b_height = (int) (widths[0] + 2 * DisplayUtils.dip2px(8));
        }
        int x = b_position.x;
        int y = b_position.y;
        int w = (int) (b_width * b_scale.x);
        int h = (int) (b_height * b_scale.y);
        if (!b_firstInit) {
            b_rect = new Rect(x, y, x + w, y + h);
            b_firstInit = true;
        }

        if (!b_zoomCenter) {
            b_rect = new Rect(x, y, x + w, y + h);
        }
        e_canvas.drawRect(b_rect, paint);
        e_canvas.drawText(b_text, x + (b_width / 2 - b_textWidth / 2),
                y + (b_height / 2 + b_singleWidth / 2), b_textPaint);
    }

    @Override
    public void animation() {
        if (b_baseAnim != null && b_baseAnim.animating) {
            doAnimation();
        }
    }

    private void doAnimation() {
        switch (b_baseAnim.animType) {
            case COLOR:
                paint.setColor(b_baseAnim.adjustButtonBackGround(b_button_Color, b_normal));
                break;
        }
    }

    public void setZoomCenter(boolean zoomCenter) {
        this.b_zoomCenter = zoomCenter;
    }

    public void setTextColor(int text_Color) {
        this.b_text_Color = text_Color;
    }

    public void setButtonColor(int button_Color) {
        this.b_button_Color = button_Color;
        this.paint.setColor(button_Color);
    }


    @Override
    public void setNormal(boolean b_normal) {
        if (b_baseAnim != null) {
            this.b_baseAnim.animating = true;
            this.b_normal = b_normal;
        }
    }

    public void setAnimation(BaseButtonAnimation anim) {
        this.b_baseAnim = anim;
    }

    @Override
    public void setText(String b_text) {
        this.b_text = b_text;
    }

    public void setPosition(int x, int y) {
        this.b_position.x = x;
        this.b_position.y = y;
    }
}
