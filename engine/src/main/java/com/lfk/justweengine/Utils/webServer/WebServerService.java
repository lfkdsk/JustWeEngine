package com.lfk.justweengine.Utils.webServer;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;

import com.lfk.justweengine.R;


/**
 * WebServerService for Android
 *
 * @author liufengkai
 *         Created by liufengkai on 16/1/6.
 */
public class WebServerService extends Service {
    // threads translate through this handler
    private WebServer.MessageHandler logResult;
    // two follows about notification
    private NotificationManager notificationManager;
    private Notification notification;
    private final IBinder mBinder = new LocalBinder();
    // socket thread object
    private Servers webServers;
    // engine context
    private static Activity engine;
    private PendingIntent contentIntent;
    private boolean IsRunning;

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        updateNotification("Open Service");
    }

    public static void init(Activity engine) {
        WebServerService.engine = engine;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * update notification
     *
     * @param text update message on notification
     */
    private void updateNotification(String text) {
        contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, engine.getClass()), 0);
        Notification.Builder builder = new Notification.Builder(engine)
                .setContentTitle("WebServer")
                .setContentText(text)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis());
        notification = builder.getNotification();
        notificationManager.notify(0, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public class LocalBinder extends Binder {
        WebServerService getService() {
            return WebServerService.this;
        }
    }

    /**
     * start server
     *
     * @param logResult handler to translate message
     * @param port      port in socket
     */
    public void startServer(WebServer.MessageHandler logResult, int port) {
        this.logResult = logResult;
        // running
        setIsRunning(true);

        // get WifiManager
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        // set IP
        WebServerDefault.setWebServerIp(WebServerDefault.intToIp(wifiInfo.getIpAddress()));
        // if not in wifi
        if (wifiInfo.getSupplicantState() != SupplicantState.COMPLETED) {
            this.logResult.OnError("Please connect to a WIFI-network.");
            try {
                throw new Exception("Please connect to a WIFI-network.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Start socket thread
        webServers = new Servers(engine.getApplicationContext(), logResult, port);
        webServers.start();

        updateNotification("running on " +
                WebServerDefault.WebServerIp + ":" + port);
    }

    public void stopServer() {
        setIsRunning(false);
        if (webServers != null) {
            webServers.stopServer();
            webServers.interrupt();
        }
    }

    public void setIsRunning(boolean isRunning) {
        this.IsRunning = isRunning;
    }

    public boolean isRunning() {
        return IsRunning;
    }
}
