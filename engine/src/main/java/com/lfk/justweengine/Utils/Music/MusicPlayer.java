package com.lfk.justweengine.Utils.music;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * MusicPlayer
 *
 * @author liufengkai
 *         Created by liufengkai on 16/2/5.
 */
public class MusicPlayer implements Music, MediaPlayer.OnCompletionListener {
    private MediaPlayer player;
    // 准备好?
    private boolean isPrepared;
    // 是否循环
    private boolean isLooping;
    // 音量
    private float volume;

    public MusicPlayer(Context context, String fileName) {
        player = new MediaPlayer();
        // init
        initMusicPlayer();
        try {
            // get file descriptor
            AssetFileDescriptor descriptor = context.getAssets().openFd(fileName);
            // setData
            player.setDataSource(descriptor.getFileDescriptor(),
                    descriptor.getStartOffset(),
                    descriptor.getLength());
            player.prepare();
            isPrepared = true;
            player.setOnCompletionListener(this);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't load music");
        }
    }

    private void initMusicPlayer() {
        volume = 10;
        isPrepared = false;
        isLooping = false;
    }

    @Override
    public void play() {
        if (player.isPlaying())
            return;

        try {
            synchronized (this) {
                if (isPrepared) {
                    player.prepare();
                }
                player.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {
        player.stop();
        synchronized (this) {
            isPrepared = false;
        }
    }

    @Override
    public void pause() {
        if (player.isPlaying())
            player.pause();
    }

    @Override
    public void setLooping(boolean isLooping) {
        this.isLooping = isLooping;
    }

    @Override
    public void setVolume(float volume) {
        this.volume = volume;
    }

    @Override
    public float getVolume() {
        return volume;
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public boolean isLooping() {
        return isLooping;
    }

    @Override
    public void dispose() {
        if (player.isPlaying()) {
            player.stop();
        }
        player.release();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        synchronized (this) {
            isPrepared = false;
        }
    }
}
