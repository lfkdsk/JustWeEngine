package com.lfk.justweengine.utils.music;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * SoundPlayer
 *
 * @author liufengkai
 *         Created by liufengkai on 16/2/5.
 */
public class SoundPlayer {
    // get SoundManager from
    private SoundManager manager;
    // musicID list
    private ArrayList<String> musicList;
    // Sound clock
    private SoundClock soundClock;

    private boolean isPlaying;

    private long delay;

    private int numTicks;

    /**
     * Sound player
     *
     * @param manager  a soundManager have a soundPool.
     * @param delay    The number of milliseconds to delay between ticks.
     * @param numTicks Starts the clock running for a specific number of ticks.
     */
    public SoundPlayer(SoundManager manager,
                       long delay,
                       int numTicks) {
        this.manager = manager;
        this.musicList = new ArrayList<>();
        this.isPlaying = false;
        this.delay = delay;
        this.numTicks = numTicks;
        this.soundClock = new SoundClock(delay, numTicks);
    }

    /**
     * add a sound to player
     *
     * @param soundName soundName
     */
    public void addSound(String soundName) {
        if (manager.containSound(soundName)) {
            musicList.add(soundName);
        }
    }

    /**
     * remove a sound from player
     *
     * @param soundName soundName
     */
    public void removeSound(String soundName) {
        if (manager.containSound(soundName)) {
            musicList.remove(soundName);
        }
    }

    public void play() {
        if (!musicList.isEmpty() &&
                !isPlaying) {
            if (soundClock.getTickNumber() == 0) {
//                soundClock.restart();
                soundClock = null;
                soundClock = new SoundClock(delay, numTicks);
                soundClock.start();
            } else
                soundClock.start();
            isPlaying = true;
        }
    }

    private class SoundClock extends TimerTask {
        // timer
        private Timer timer;
        // Starts the clock running for a specific number of ticks.
        private int tickLeft;

        private int tickLeftSave;

        // The number of milliseconds to delay between ticks.
        private long delay;

        public SoundClock(long delay, int numTicks) {
            init();
            this.tickLeft = numTicks;
            this.tickLeftSave = numTicks;
            this.delay = delay;
        }

        private void init() {
            this.timer = new Timer();
        }

        // tick 心跳
        private void tick() {
            if (tickLeft == 0)
                this.stop();
            else {
                for (int i = 0; i < musicList.size(); i++) {
                    manager.play(musicList.get(i));
                }
                if (tickLeft > 0)
                    tickLeft--;
            }
        }

        @Override
        public void run() {
            tick();
        }

        public void start(int numTicks) {
            this.tickLeft = numTicks;
            this.timer.schedule(this, 0, delay);
        }

        public void start() {
            if (tickLeft != 0)
                start(tickLeft);
        }

        public void stop() {
            this.cancel();
            this.timer.cancel();
            isPlaying = false;
        }

        public int getTickNumber() {
            return tickLeft;
        }

        public void restart() {
            this.tickLeft = tickLeftSave;
            this.timer.purge();
            this.timer = null;
            init();
            start();
        }
    }

}
