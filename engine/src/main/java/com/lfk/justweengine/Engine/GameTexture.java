package com.lfk.justweengine.Engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * 封装图片的获取
 * Created by liufengkai on 15/11/27.
 */
public class GameTexture {
    private Context e_context;
    private Bitmap e_bitmap;

    public GameTexture(Context e_context) {
        this.e_context = e_context;
        e_bitmap = null;
    }

    public Bitmap getBitmap() {
        return e_bitmap;
    }

    /**
     * get bitmap form assets
     *
     * @param filename
     * @return
     */
    public boolean loadFromAsset(String filename) {
        InputStream inputStream;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            inputStream = e_context.getAssets().open(filename);
            e_bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
