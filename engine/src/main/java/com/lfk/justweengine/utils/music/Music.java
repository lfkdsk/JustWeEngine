package com.lfk.justweengine.utils.music;

/**
 * music interface
 *
 * @author liufengkai
 *         Created by liufengkai on 16/2/5.
 */
public interface Music {
    void play();

    void stop();

    void pause();

    void setLooping(boolean isLooping);

    void setVolume(float volume);

    float getVolume();

    boolean isPlaying();

    boolean isLooping();

    void dispose();

}
