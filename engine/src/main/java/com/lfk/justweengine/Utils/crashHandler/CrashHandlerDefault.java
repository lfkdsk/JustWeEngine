package com.lfk.justweengine.Utils.crashHandler;

import android.os.Environment;

import java.io.File;

/**
 * Created by liufengkai on 16/1/16.
 */
public class CrashHandlerDefault {
    public static final String Log_Default_Path =
            Environment.getExternalStorageDirectory() + "/CrashLog";

    public static void init(){
        File file = new File(CrashHandlerDefault.Log_Default_Path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
