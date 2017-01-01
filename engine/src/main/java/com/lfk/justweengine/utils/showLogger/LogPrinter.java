package com.lfk.justweengine.utils.showLogger;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lfk.justweengine.info.UIdefaultData;
import com.lfk.justweengine.utils.logger.LogLevel;
import com.lfk.justweengine.utils.logger.LogParser;
import com.lfk.justweengine.utils.tools.DisplayUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by liufengkai on 16/4/15.
 */
public class LogPrinter {

    public static Map<LogLevel, Integer> colorMap = new HashMap<>();

    private CopyOnWriteArrayList<LogHandler.Line> logLinesList;

    // 多少行弹幕
    private int logListSize;

    private int left_X;

    private int left_Y;

    private int line_width;

    private Paint textPaint;

    static {
        colorMap.put(LogLevel.VERBOSE, 0XFF717171);
        colorMap.put(LogLevel.DEBUG, 0XFF2980b9);
        colorMap.put(LogLevel.INFO, 0XFF27ae60);
        colorMap.put(LogLevel.WARN, 0XFFf39c12);
        colorMap.put(LogLevel.ERROR, 0XFFc0392b);
        colorMap.put(LogLevel.FATAL, 0XFFc0392b);
        colorMap.put(LogLevel.ASSERT, 0XFFc0392b);
    }


    public LogPrinter(int d_time) {
        logLinesList = new CopyOnWriteArrayList<>();
        init(d_time);
    }

    private void init(int d_time) {

        this.left_X = (int) (DisplayUtils.dip2px(2));
        this.left_Y = (int) (DisplayUtils.dip2px(2));

        this.textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.textPaint.setColor(colorMap.get(LogLevel.VERBOSE));
        this.textPaint.setTextSize(20);

        float[] widths = new float[1];
        textPaint.getTextWidths("H", widths);

        this.logListSize =
                (int) ((UIdefaultData.screenHeight - DisplayUtils.dip2px(4))
                        / widths[0] + DisplayUtils.dip2px(4));

        this.line_width = (int) (widths[0] + DisplayUtils.dip2px(2));

        Log.e("listX", left_X + " ");

        LogHandler.init(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case LogHandler.LOGCAT:
                        while (logLinesList.size() >= logListSize)
                            logLinesList.remove(0);
                        logLinesList.add((LogHandler.Line) msg.obj);
                        break;
                }
            }
        }, d_time);
        LogHandler.getInstance();
    }


    public void draw(Canvas engine) {
        int index = 0;
        for (LogHandler.Line line : logLinesList) {
            if (index == logListSize) index = 0;
            if (line != null) {
                textPaint.setColor(colorMap.get(LogParser.parseLev(line.lev)));
                engine.drawText(line.line,
                        left_X,
                        left_Y + index * line_width,
                        textPaint);
                index++;
            }
        }
    }
}
