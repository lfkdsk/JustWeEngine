package com.lfk.justweengine.Utils.io;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * GameIO
 * Read File
 *
 * @author liufengkai
 *         Created by liufengkai on 16/1/17.
 */
public class GameIO implements FileIO {
    private Context context;
    private AssetManager manager;
    private String externalPath;

    public GameIO(Context context) {
        this.context = context;
        this.manager = context.getAssets();
        this.externalPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator;
    }

    @Override
    public InputStream readAsset(String name) throws IOException {
        return manager.open(name);
    }

    @Override
    public InputStream readFile(String name) throws IOException {
        return new FileInputStream(externalPath + name);
    }

    @Override
    public OutputStream writeFile(String name) throws IOException {
        return new FileOutputStream(externalPath + name);
    }
}
