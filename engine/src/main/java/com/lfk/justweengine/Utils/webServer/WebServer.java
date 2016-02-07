package com.lfk.justweengine.Utils.webServer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.lfk.justweengine.Utils.logger.Logger;
import com.lfk.justweengine.Utils.webServer.Interface.OnLogResult;
import com.lfk.justweengine.Utils.webServer.Interface.OnPermissionFile;
import com.lfk.justweengine.Utils.webServer.Interface.OnWebResult;

import java.io.File;
import java.util.HashMap;

/**
 * WebServer
 *
 * @author liufengkai
 *         Created by liufengkai on 16/1/6.
 */
public class WebServer {
    private Activity engine;
    private static HashMap<String, OnWebResult> webServerRule;
    private OnLogResult logResult;
    private WebServerService webServerService;
    private Integer webPort = null;
    private ServiceConnection serviceConnection;
    private final int ERROR = -1;
    private final int LOG = 1;

    public WebServer(Activity engine) {
        this.engine = engine;
        init();
    }

    public WebServer(Activity engine, OnLogResult logResult) {
        this.engine = engine;
        this.logResult = logResult;
        init();
    }

    public WebServer(Activity engine, OnLogResult logResult, int webPort) {
        this.engine = engine;
        this.logResult = logResult;
        this.webPort = webPort;
        init();
    }

    private void init() {
        webServerRule = new HashMap<>();

        WebServerService.init(engine);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                webServerService = ((WebServerService.LocalBinder) service).getService();
                if (logResult != null)
                    logResult.OnResult(WebServerDefault.WebServerServiceConnected);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                webServerService = null;
                if (logResult != null)
                    logResult.OnResult(WebServerDefault.WebServerServiceDisconnected);
            }
        };

    }


    public void startWebService() {
        if (webServerService != null) {
            webServerService.startServer(new MessageHandler(),
                    (webPort == null) ? WebServerDefault.WebDefaultPort : webPort);
        }
    }

    public void stopWebService() {
        if (webServerService != null) {
            webServerService.stopServer();
        }
    }

    public void initWebService() {
        WebServerDefault.init(engine.getApplicationContext());
        // 绑定Service
        engine.bindService(new Intent(engine, WebServerService.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE
        );
    }

    public void callOffWebService() {
        engine.unbindService(serviceConnection);
    }

    public void apply(String rule, OnWebResult result) {
        webServerRule.put(rule, result);
    }

    public void apply(final String rule) {
        webServerRule.put(rule, new OnPermissionFile() {
            @Override
            public File OnPermissionFile(String name) {
                Logger.e(WebServerDefault.WebServerFiles  + rule + name);
                return new File(WebServerDefault.WebServerFiles  + rule + name);
            }
        });
    }

    public static OnWebResult getRule(String rule) {
        return webServerRule.get(rule);
    }

    public void setLogResult(OnLogResult logResult) {
        this.logResult = logResult;
    }

    public class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOG:
                    logResult.OnResult(msg.obj.toString());
                    break;
                case ERROR:
                    logResult.OnError(msg.obj.toString());
                    break;
            }
        }

        public void OnError(String str) {
            Message message = this.obtainMessage();
            message.what = ERROR;
            message.obj = str;
            sendMessage(message);
        }

        public void OnResult(String str) {
            Message message = this.obtainMessage();
            message.what = LOG;
            message.obj = str;
            sendMessage(message);
        }
    }
}
