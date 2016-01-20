package com.lfk.justweengine.Utils.blueTooth;

import java.util.ArrayList;

/**
 * Created by liufengkai on 16/1/20.
 */
public interface OnMessageBack {
    void getMessage(String msg);

    void sendMessage(String msg);

    void getDevice(ArrayList<String> msg);
}
