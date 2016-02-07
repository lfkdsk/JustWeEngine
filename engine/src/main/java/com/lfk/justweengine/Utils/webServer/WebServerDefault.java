package com.lfk.justweengine.Utils.webServer;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * WebServerDefault Default message about Web
 *
 * @author liufengkai
 *         Created by liufengkai on 16/1/6.
 */
public class WebServerDefault {
    // default file local
    public static final String WebServerFiles = Environment
            .getExternalStorageDirectory() + "/JustWeWebServer";

    public static final String WebServerServiceConnected = "Service connected";

    public static final String WebServerServiceDisconnected = "Service disconnected";

    // default port
    public static final int WebDefaultPort = 8080;

    public static String getWebServerFiles() {
        return WebServerFiles;
    }

    // get local IP
    public static String WebServerIp;

    public static Context context;

    public static void init(Context context) {
        WebServerDefault.context = context;
        File file = new File(WebServerFiles);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * translate int to  IP
     *
     * @param i Ip int
     * @return IP
     */
    public static String intToIp(int i) {
        return ((i) & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    public static void setWebServerIp(String webServerIp) {
        WebServerIp = webServerIp;
    }
}
