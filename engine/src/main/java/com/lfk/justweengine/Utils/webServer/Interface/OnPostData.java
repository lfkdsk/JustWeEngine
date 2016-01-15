package com.lfk.justweengine.Utils.webServer.Interface;

import java.util.HashMap;

/**
 * Created by liufengkai on 16/1/14.
 */
public interface OnPostData extends OnWebResult {
    String OnPostData(HashMap<String, String> hashMap);
}
