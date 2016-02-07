package com.lfk.justweengine.Utils.webServer.Interface;

import java.util.HashMap;

/**
 * OnPostData get post data
 * Created by liufengkai on 16/1/14.
 */
public interface OnPostData extends OnWebResult {
    /**
     * get post data
     *
     * @param hashMap return post data
     * @return return state code
     */
    String OnPostData(HashMap<String, String> hashMap);
}
