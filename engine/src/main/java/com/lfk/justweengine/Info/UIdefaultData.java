package com.lfk.justweengine.Info;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

/**
 * UI 默认数据类 待修改
 *
 * @author liufengkai
 *         Created by liufengkai on 15/11/27.
 */
public class UIdefaultData {

    private static Context context;

    public static void init(Context context) {
        UIdefaultData.context = context;
        scale = context.getResources().getDisplayMetrics().density;
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        center = new Point(screenWidth / 2, screenHeight / 2);
        centerInHorizontalX = screenWidth / 2;
        Log.e("h" + screenHeight, "w" + screenWidth);
    }

    public static final int sprite_default_color_paint = Color.WHITE;

    public static int screenWidth;

    public static int screenHeight;

    public static int centerInHorizontalX;

    public static float scale;

    public static Point center;
}
