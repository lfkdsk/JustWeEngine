package com.lfk.justweengine.utils.showLogger;

import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;

import com.lfk.justweengine.utils.logger.LogCat;
import com.lfk.justweengine.utils.logger.LogLevel;
import com.lfk.justweengine.utils.logger.Options;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * LogPrintHandler
 * <p>
 * Created by liufengkai on 16/4/15.
 */
public class LogHandler {

    private static LogHandler mInstance = null;

    private static int darningTime = 3;

    public static final int LOGCAT = 100;

    private static Handler sentHandler;

    private ScheduledExecutorService service;

    public class Line implements Parcelable {
        String line;
        String lev;
        String time;
        String tag;

        public Line() {

        }

        protected Line(Parcel in) {
            line = in.readString();
            lev = in.readString();
            time = in.readString();
            tag = in.readString();
        }

        public final Creator<Line> CREATOR = new Creator<Line>() {
            @Override
            public Line createFromParcel(Parcel in) {
                return new Line(in);
            }

            @Override
            public Line[] newArray(int size) {
                return new Line[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(line);
            dest.writeString(lev);
            dest.writeString(time);
            dest.writeString(tag);
        }
    }


    public static LogHandler getInstance() {
        if (mInstance == null) {
            synchronized (LogHandler.class) {
                if (mInstance == null)
                    mInstance = new LogHandler();
            }
        }
        return mInstance;
    }

    private class GetLogger implements Runnable {
        @Override
        public void run() {
            getLogger();
        }
    }

    private void getLogger() {
        Process process = LogCat.getInstance()
                .options(Options.DUMP)
                .withTime()
                .recentLines(1000)
                .filter("", LogLevel.VERBOSE)
                .commit();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (sentHandler != null) {
                    Message msg = sentHandler.obtainMessage();
                    msg.obj = getLine(line);
                    msg.what = LOGCAT;
                    sentHandler.sendMessage(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Line getLine(String line) {
        Line log = new Line();
        int tagStart = line.indexOf("/");
        int msgStart = line.indexOf("):");
        if (msgStart == -1 || tagStart == -1) {
            return null;
        }
        log.tag = line.substring(tagStart + 1, msgStart + 1);
        log.line = line.substring(msgStart + 2);
        log.lev = line.substring(tagStart - 1, tagStart);
        log.time = line.substring(0, tagStart - 2);
        return log;
    }


    public LogHandler() {
        service = Executors.newSingleThreadScheduledExecutor();

        service.scheduleAtFixedRate(new GetLogger(), 0, darningTime, TimeUnit.SECONDS);
    }

    public static void init(Handler handler, int time) {
        sentHandler = handler;
        darningTime = time;
    }

}
