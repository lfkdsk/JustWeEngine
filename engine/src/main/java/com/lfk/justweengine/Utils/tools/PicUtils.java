package com.lfk.justweengine.Utils.tools;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片处理工具类
 *
 * @author liufengkai
 */
public class PicUtils {

    /**
     * 将Bitmap转换成字符串
     *
     * @param bitmap 传入图片的bitmap
     * @return String
     */
    public static String bitmapToString(Bitmap bitmap) {
        String bitmapString = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        bitmapString = Base64.encodeToString(bytes, Base64.DEFAULT);
        return bitmapString;
    }

    /**
     * 将字符串转换成Bitmap类型
     *
     * @param string 传入图片转出的字符串
     * @return Bitmap
     */
    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 生成截图,不包含ActionBar
     *
     * @param context 传入Context
     * @return Bitmap
     */
    public static Bitmap printScreenWithOutActionBar(Activity context) {
        // 获取屏幕
        View view = context.getWindow().getDecorView();
        Display display = context.getWindowManager().getDefaultDisplay();
        view.layout(0, 0, display.getWidth(), display.getHeight());
        view.setDrawingCacheEnabled(true);
        // 以一个矩形进行截图
        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        // 获取高度
        int statusBarHeight = frame.top;
        int width = context.getWindowManager().getDefaultDisplay().getWidth();
        int height = context.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 获取ActionBar的高度
        TypedArray actionbarSizeTypedArray = context.obtainStyledAttributes(new int[]{
                android.R.attr.actionBarSize});
        int h = (int) actionbarSizeTypedArray.getDimension(0, 0);
        // 生成Bitmap
        return Bitmap.createBitmap(view.getDrawingCache(), 0, statusBarHeight + h, width, height
                - statusBarHeight - h);
        // 生成文件
//        File f = new File(Environment.getExternalStorageDirectory(),
//                "output_image.jpg");
//        FileOutputStream fOut = null;
//        try {
//            fOut = new FileOutputStream(f);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//            fOut.flush();
//            fOut.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return Uri.fromFile(f);
    }

    /**
     * 截取整个屏幕
     *
     * @param context
     * @return Bitmap
     */
    public Bitmap printAllScreen(Activity context) {
        View view = context.getWindow().getDecorView();
        Display display = context.getWindowManager().getDefaultDisplay();
        view.layout(0, 0, display.getWidth(), display.getHeight());
        view.setDrawingCacheEnabled(true);
        return Bitmap.createBitmap(view.getDrawingCache());
    }


    /**
     * 合成图层
     *
     * @param background 背景
     * @param foreground 前景
     * @return Bitmap
     */
    private Bitmap toConformBitmap(Bitmap background, Bitmap foreground) {
        if (background == null || foreground == null) {
            return null;
        }
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(bitmap);
        cv.drawBitmap(background, 0, 0, null);
        cv.drawBitmap(foreground, 0, 0, null);
        // 保存
        cv.save(Canvas.ALL_SAVE_FLAG);
        // 存储
        cv.restore();
        return bitmap;
    }

    /**
     * 将View转换为图片
     *
     * @param view 传入一个View
     * @return Bitmap
     */
    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    /**
     * 自动压缩图片
     *
     * @param image 传入图片bitmap
     * @return bitmap
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baoS = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baoS中
        int options = 100;
        image.compress(Bitmap.CompressFormat.JPEG, 100, baoS);
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baoS.toByteArray().length / 1024 > 100) {
            // 重置baoS即清空baoS
            baoS.reset();
            // 压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baoS);
            options -= 10;
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baoS.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        return BitmapFactory.decodeStream(isBm, null, null);
    }

    /**
     * 指定压缩的质量压缩图片
     *
     * @param image   传入图片的Bitmap
     * @param options 压缩图片质量
     * @return bitmap
     */
    public static Bitmap compressImage(Bitmap image, int options) {
        ByteArrayOutputStream baoS = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, options, baoS);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baoS.toByteArray());
        return BitmapFactory.decodeStream(isBm, null, null);
    }

    /**
     * 按照比例进行缩放
     *
     * @param image  传入的Bitmap
     * @param width  传入的宽度
     * @param height 传入的高度
     * @return Bitmap
     */
    private static Bitmap compressWithMeasure(Bitmap image, int width, int height) {
        ByteArrayOutputStream baoS = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baoS);
        // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
        if (baoS.toByteArray().length / 1024 > 1024) {
            // 重置baoS即清空baoS
            baoS.reset();
            // 这里压缩50%，把压缩后的数据存放到baoS中
            image.compress(Bitmap.CompressFormat.JPEG, 50, baoS);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baoS.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        // be = 1表示不缩放
        int be = 1;
        if (w > h && w > width) {
            // 如果宽度大的话根据宽度固定大小缩放
            be = newOpts.outWidth / width;
        } else if (w < h && h > height) {
            // 如果高度高的话根据宽度固定大小缩放
            be = newOpts.outHeight / height;
        }
        if (be <= 0)
            be = 1;
        // 设置缩放比例
        newOpts.inSampleSize = be;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baoS.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        // 压缩好比例大小后再进行质量压缩
        return compressImage(bitmap);
    }

    /**
     * 按照原比例进行缩放
     *
     * @param image
     * @return Bitmap
     */
    public static Bitmap compressWithMeasure(Bitmap image) {
        return compressWithMeasure(image, image.getWidth(), image.getHeight());
    }

    /**
     * 获取网络上的图片
     *
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            myFileURL = new URL(url);

            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();

            conn.setConnectTimeout(6000);// 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setDoInput(true);// 连接设置获得数据流
            conn.setUseCaches(false);// 不使用缓存
            InputStream is = conn.getInputStream();// 得到数据流，这里会有调用conn.connect();

            // 解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            is.close();// 关闭数据流
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 从资源中获取Bitmap
     *
     * @param context
     * @param resid   例如这样：R.drawable.icon
     * @return
     */
    public static Bitmap getBitmapFromResources(Context context, int resid) {
        Resources res = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, resid);
        return bitmap;
    }

    /**
     * Bitmap转字节
     *
     * @param bitmap
     * @return
     */
    public byte[] Bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 字节转Bitmap
     *
     * @param bytes
     * @return
     */
    public Bitmap Bytes2Bimap(byte[] bytes) {
        if (bytes.length != 0) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }

    /**
     * Bitmap缩放，可能比例要注意一下
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    /**
     * 将Drawable转化为Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;

        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);

        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);

        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap转换成Drawable
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static Drawable Bitmap2Drawable(Context context, Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(context.getResources(), bitmap);
        return bd;
    }

    /**
     * 获得圆角图片
     *
     * @param bitmap
     * @param roundPx
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 获得带倒影的图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage;
        reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w, h / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint defaultPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, defaultPaint);

        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight()
                + reflectionGap, 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight() + reflectionGap, paint);

        return bitmapWithReflection;
    }

    public static class ImagePiece {
        public Bitmap bitmap = null;
        public int index = 0;

        public ImagePiece(Bitmap bitmap) {
            this.bitmap = bitmap;
            this.index = 0;
        }

        public ImagePiece(Bitmap bitmap, int index) {
            this.bitmap = bitmap;
            this.index = index;
        }
    }

    /**
     * 截图分片
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static List<ImagePiece> spilt(Context context, Bitmap bitmap) {
        int screenHeight = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getHeight();
        List<ImagePiece> list = new ArrayList<>();
        if (bitmap.getHeight() <= screenHeight) {
            list.add(new ImagePiece(bitmap));
            return list;
        }

        int pageNum = Math.round(bitmap.getHeight() * 1.0f / screenHeight);
        for (int i = 0; i < pageNum; i++) {
            int xValue = 0;
            int yValue = i * screenHeight + DisplayUtils.dp2px(context, 16);
            list.add(new ImagePiece(
                    Bitmap.createBitmap(bitmap, xValue, yValue,
                            bitmap.getWidth(), screenHeight),
                    i));
        }

        return list;
    }

    /**
     * 截取ScrollView
     *
     * @param v
     * @return
     */
    public static Bitmap createBitmap(Context context, ScrollView v) {
        int width = 0, height = 0;
        for (int i = 0; i < v.getChildCount(); i++) {
            width += v.getChildAt(i).getWidth();
            height += v.getChildAt(i).getHeight();
        }
        if (width <= 0 || height <= 0) {
            return null;
        }
        int h = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        if (height < h)
            height = h;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    /**
     * 制作圆形图片
     *
     * @param rect
     */
    public static Bitmap rectBitmapToCircle(Bitmap rect) {
        Shader mBitmapShader = new BitmapShader(rect, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(mBitmapShader);
        Bitmap output = Bitmap.createBitmap(rect.getWidth(), rect.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawCircle(rect.getWidth() / 2,
                rect.getHeight() / 2,
                Math.min(rect.getWidth(), rect.getHeight()) / 2
                , paint);
        return output;
    }
}
