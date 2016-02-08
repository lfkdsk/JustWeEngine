package com.lfk.justweengine.Utils.music;

import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Info.UIdefaultData;
import com.lfk.justweengine.Utils.logger.Logger;

import java.io.IOException;
import java.util.HashMap;

/**
 * SoundManager sound manger
 *
 * @author liufengkai
 *         Created by liufengkai on 16/2/5.
 */
public class SoundManager {
    private AssetManager assetManager;
    // sound pool
    private SoundPool soundPool;

    // musicName musicID
    private HashMap<String, Integer> musicMap;

    public SoundManager(Engine engine, int size) {
        engine.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // init
        this.assetManager = engine.getAssets();
        this.soundPool = new SoundPool(size, AudioManager.STREAM_MUSIC, 0);
        this.musicMap = new HashMap<>();
    }

    public void addSound(String musicName) {
        try {
            musicMap.put(musicName, soundPool.load(assetManager.openFd(musicName), 0));
        } catch (IOException e) {
            e.printStackTrace();
            Logger.e("Couldn't found music");
        }
    }

    public void removeSound(String musicName) {
        if (musicMap.containsKey(musicName)) {
            soundPool.unload(musicMap.get(musicName));
            musicMap.remove(musicName);
        } else {
            Logger.e("Couldn't found music");
        }
    }

    /**
     * play sound
     *
     * @param musicName name in assets
     * @param volume    sound's volume
     */
    public void play(String musicName, float volume) {
        if (musicMap.containsKey(musicName)) {
            soundPool.play(musicMap.get(musicName), volume, volume, 0, 0, 1);
        }
    }

    /**
     * play sound with defaultMusicVolume
     *
     * @param musicName name in assets
     */
    public void play(String musicName) {
        if (musicMap.containsKey(musicName)) {
            soundPool.play(musicMap.get(musicName),
                    UIdefaultData.defaultMusicVolume,
                    UIdefaultData.defaultMusicVolume,
                    0, 0, 1);
        }
    }

    /**
     * play sound with musicID in SoundPool
     *
     * @param musicID musicID in SoundPool
     */
    public void play(int musicID) {
        if (musicMap.containsValue(musicID)) {
            soundPool.play(musicID,
                    UIdefaultData.defaultMusicVolume,
                    UIdefaultData.defaultMusicVolume,
                    0, 0, 1);
        }
    }

    /**
     * play sound with musicID in SoundPool
     *
     * @param musicID musicID in SoundPool
     * @param volume  volume
     */
    public void play(int musicID, float volume) {
        if (musicMap.containsValue(musicID)) {
            soundPool.play(musicID,
                    volume,
                    volume,
                    0, 0, 1);
        }
    }

    /**
     * Is music in map?
     *
     * @param soundName soundName
     * @return Is music in map?
     */
    public boolean containSound(String soundName) {
        return musicMap.containsKey(soundName);
    }

    /**
     * Is music in map?
     *
     * @param soundID soundID
     * @return Is music in map?
     */
    public boolean containSoundID(int soundID) {
        return musicMap.containsValue(soundID);
    }

    /**
     * get Music from Name
     *
     * @param soundName soundName
     * @return soundID
     */
    public int getSoundID(String soundName) {
        return musicMap.get(soundName);
    }


}
