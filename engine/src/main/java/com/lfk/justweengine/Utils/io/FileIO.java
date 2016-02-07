package com.lfk.justweengine.Utils.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * FileIO
 * get File from asset / File
 *
 * @author liufengkai
 *         Created by liufengkai on 16/1/17.
 */
public interface FileIO {
    InputStream readAsset(String name) throws IOException;

    InputStream readFile(String name) throws IOException;

    OutputStream writeFile(String name) throws IOException;
}
