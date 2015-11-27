package com.lfk.justweengine.Utils.tools;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.lfk.basicutils.R;

/**
 * 简化Toast的定制工具类
 * @author liufengkai
 */
public class ToastUtils {
    private Context context;
    private Builder mBuilder;
    private Toast mToast;
    // 背景
    private Drawable mBackgroundDrawable;
    // 背景Id
    private int mBackgroundResId;
    /**
     * Show the view or text notification for a short period of time.  This time
     * could be user-definable.  This is the default.
     *
     * @see #setDuration
     */
    public static final int LENGTH_SHORT = 0;

    /**
     * Show the view or text notification for a long period of time.  This time
     * could be user-definable.
     *
     * @see #setDuration
     */
    public static final int LENGTH_LONG = 1;

    /**
     * 初始化Toast加载默认样式
     * @param context
     */
    public ToastUtils(Context context) {
        this.context = context;
        mBuilder = new Builder();
//        Log.e("===>","toastutils");
    }

    /**
     * Builder
     */
    private class Builder {
        private LayoutInflater inflater = LayoutInflater.from(context);
        private View view;
        private TextView textView;

        private Builder() {
//            Log.e("===>","builder");
            view = inflater.inflate(R.layout.toast_item, null);
            textView = (TextView) view.findViewById(R.id.toast_text);
            mToast = new Toast(context);
            mToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 0);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setView(view);
        }

        public void setText(String text) {
            textView.setText(text);
//            Log.e("===>", "text");
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void setBackground(Drawable drawable) {
            view.setBackground(drawable);
//            mToast.setView(view);
        }

        public void setBackground(int resId) {
            view.setBackgroundResource(resId);
//            mToast.setView(view);
        }

        public void setGravity(int gravity, int xOffset, int yOffset) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }

        public void setDuration(int duration) {
            mToast.setDuration(duration);
        }

        public void setView(View view) {
            this.view = view;
            mToast.setView(view);
        }

        public void setView(View view, int res) {
            this.view = view;
            this.textView = (TextView) view.findViewById(res);
            mToast.setView(view);
        }

        public void setColor(int color) {
            view.setBackgroundColor(color);
//            Log.e("===>", "color");
        }

        public void setTextColor(int color) {
            textView.setTextColor(color);
        }
    }

    public ToastUtils show() {
//        Log.e("===>","show");
        if (mBuilder == null)
            mBuilder = new Builder();
        mToast.show();
        return this;
    }

    public ToastUtils setBackground(Drawable drawable) {
        mBackgroundDrawable = drawable;
        if (mBuilder != null) {
            mBuilder.setBackground(mBackgroundDrawable);
        } else {
            mBuilder = new Builder();
            mBuilder.setBackground(mBackgroundDrawable);
        }
        return this;
    }

    public ToastUtils setBackground(int resId) {
        mBackgroundResId = resId;
        if (mBuilder != null) {
            mBuilder.setBackground(resId);
        } else {
            mBuilder = new Builder();
            mBuilder.setBackground(resId);
        }
        return this;
    }

    public ToastUtils setText(String text) {
        if (mBuilder != null) {
            mBuilder.setText(text);
        } else {
            mBuilder = new Builder();
            mBuilder.setText(text);
        }
        return this;
    }

    public ToastUtils setGravity(int gravity, int xOffset, int yOffset) {
        if (mBuilder != null) {
            mBuilder.setGravity(gravity, xOffset, yOffset);
        } else {
            mBuilder = new Builder();
            mBuilder.setGravity(gravity, xOffset, yOffset);
        }
        return this;
    }

    public ToastUtils setDuration(int duration) {
        if (mBuilder != null) {
            mBuilder.setDuration(duration);
        } else {
            mBuilder = new Builder();
            mBuilder.setDuration(duration);
        }
        return this;
    }

    public ToastUtils setView(View view) {
        if (mBuilder != null) {
            mBuilder.setView(view);
        } else {
            mBuilder = new Builder();
            mBuilder.setView(view);
        }
        return this;
    }

    /**
     * 设定View和与之对应的资源文件
     * @param view
     * @param res
     * @return
     */
    public ToastUtils setView(View view, int res) {
        if (mBuilder != null) {
            mBuilder.setView(view, res);
        } else {
            mBuilder = new Builder();
            mBuilder.setView(view, res);
        }
        return this;
    }

    public ToastUtils setColor(int color) {
        if (mBuilder != null) {
            mBuilder.setColor(color);
        } else {
            mBuilder = new Builder();
            mBuilder.setColor(color);
        }
        return this;
    }

    public ToastUtils setTextColor(int color) {
        if (mBuilder != null) {
            mBuilder.setTextColor(color);
        } else {
            mBuilder = new Builder();
            mBuilder.setTextColor(color);
        }
        return this;
    }
}
