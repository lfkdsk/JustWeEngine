package com.lfk.justweengine.Engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lfk.justweengine.Utils.io.GameIO;

import java.io.IOException;
import java.io.InputStream;

/**
 * 封装图片的获取
 * Created by liufengkai on 15/11/27.
 */
public class GameTexture {
    private Context e_context;
    private Bitmap e_bitmap;
    private GameIO gameIO;

    public GameTexture(Context e_context) {
        this.e_context = e_context;
        this.e_bitmap = null;
        this.gameIO = new GameIO(e_context);
    }

    public Bitmap getBitmap() {
        return e_bitmap;
    }

    public GameTexture(Bitmap e_bitmap) {
        this.e_bitmap = e_bitmap;
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
            inputStream = gameIO.readAsset(filename);
            e_bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * get bitmap from a big bitmap
     *
     * @param filename
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public boolean loadFromAssetStripFrame(String filename,
                                           int x, int y,
                                           int width, int height) {
        InputStream inputStream;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            inputStream = gameIO.readAsset(filename);
            Bitmap temp = BitmapFactory.decodeStream(inputStream, null, options).copy(Bitmap.Config.ARGB_8888, true);
            inputStream.close();
            e_bitmap = Bitmap.createBitmap(temp, x, y, width, height);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Bitmap loadFromBigBitmap(GameTexture texture, int x, int y
            , int w, int h) {
        return Bitmap.createBitmap(texture.getBitmap(), x, y, w, h);
    }

    public void setBitmap(Bitmap e_bitmap) {
        this.e_bitmap = e_bitmap;
    }

    /**
     * 从大图取出小图
     * @param x 横轴
     * @param y 纵轴
     * @param w 宽
     * @param h 高
     * @return
     */
    public GameTexture getFrameFromBitmap(int x, int y,
                                          int w, int h) {
        if (e_bitmap != null) {
            Bitmap bitmap =
                    Bitmap.createBitmap(e_bitmap, x, y, w, h);
            return new GameTexture(bitmap);
        }
        return null;
    }
}
