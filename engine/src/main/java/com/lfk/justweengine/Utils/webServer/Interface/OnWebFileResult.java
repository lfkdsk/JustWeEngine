package com.lfk.justweengine.Utils.webServer.Interface;

import java.io.File;

/**
 * OnWebFileResult return file
 *
 * @author liufengkai
 *         Created by liufengkai on 16/1/6.
 */
public interface OnWebFileResult extends OnWebResult {
    /**
     * return file
     *
     * @return such as : html / js / css and other file
     */
    File returnFile();
}
