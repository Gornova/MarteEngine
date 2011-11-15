package it.marteEngine;

import org.newdawn.slick.Music;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.Log;

/**
 * Handles Sounds and Music
 */
public class SFX {
  private static final SoundStore soundStore = SoundStore.get();

  /**
   * Pause or resume the music depending on it's current state
   */
  public static void toggleMusic(Music music) {
    if (music.playing()) {
      music.pause();
    } else {
      music.resume();
    }
  }

  /**
   * Play a sound once
   *
   * @param soundName The sound to play as defined in the ResourceManager
   */
  public static void playSound(String soundName) {
    if (ResourceManager.hasSound(soundName)) {
      ResourceManager.getSound(soundName).play();
    } else {
      Log.warn("No sound for " + soundName);
    }
  }

  /**
   * Play music, Only one piece of music can play at any given time
   * if another music piece is already playing it is stopped
   *
   * @param songName The music to play as defined in the ResourceManager
   */
  public static void playMusic(String songName) {
    if (ResourceManager.hasSound(songName)) {
      ResourceManager.getSound(songName).play();
    } else {
      Log.warn("No music for " + songName);
    }
  }

  public static void toggleMusic() {
    soundStore.setMusicOn(SoundStore.get().isMusicOn());
  }

  /**
   * @param volume 0=Min 1=Max
   */
  public static void setMusicVolume(float volume) {
    soundStore.setMusicVolume(volume);
  }

  /**
   * @param volume 0=Min 1=Max
   */
  public static void setSfxVolume(float volume) {
    soundStore.setSoundVolume(volume);
  }
}