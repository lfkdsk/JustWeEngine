package com.lfk.justweengine.Utils.webServer.Interface;

import java.io.File;

/**
 * OnPermissionFile set permission path,
 * the path's all father paths can get.
 *
 * @author liufengkai
 *         Created by liufengkai on 16/1/14.
 */
public interface OnPermissionFile extends OnWebResult {
    File OnPermissionFile(String name);
}
