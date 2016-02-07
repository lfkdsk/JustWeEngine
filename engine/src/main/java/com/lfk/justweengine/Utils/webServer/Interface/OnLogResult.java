package com.lfk.justweengine.Utils.webServer.Interface;

/**
 * OnLogResult 接收Log信息和Error信息
 *
 * @author liufengkai
 *         Created by liufengkai on 16/1/6.
 */
public interface OnLogResult {
    /**
     * OnResult 接收Log信息
     *
     * @param log log信息
     */
    void OnResult(String log);

    /**
     * OnError Error信息
     *
     * @param error error信息
     */
    void OnError(String error);
}
