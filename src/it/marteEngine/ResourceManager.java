package it.marteEngine;

import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A single instance to all resources used in a game.
 * Each of these resources is mapped to a key eg "SELECT_SOUND" -> Sound object.
 */
public class ResourceManager {
  private static final Map<String, Music> songs = new HashMap<String, Music>();
  private static final Map<String, Sound> sounds = new HashMap<String, Sound>();
  private static final Map<String, Image> images = new HashMap<String, Image>();
  private static final Map<String, SpriteSheet> sheets = new HashMap<String, SpriteSheet>();
  private static final Map<String, Animation> animations = new HashMap<String, Animation>();
  private static final Map<String, Font> fonts = new HashMap<String, Font>();
  private static final Map<String, String> parameters = new HashMap<String, String>();
  private static final Map<String, TiledMap> tiledMaps = new HashMap<String, TiledMap>();

  private ResourceManager() {
  }

  public static void loadResources(String ref) throws IOException {
    loadResources(ResourceLoader.getResourceAsStream(ref));
  }

  public static void loadResources(InputStream in) throws IOException {
    XMLResourceLoader resourceLoader = new XMLResourceLoader();
    resourceLoader.load(in);
  }

  public static void addImage(String key, Image image) {
    if (hasImage(key)) {
      throw new RuntimeException("Image for key " + key + " already exist!");
    }
    images.put(key, image);
  }

  public static void addSpriteSheet(String key, SpriteSheet sheet) {
    if (hasSpriteSheet(key)) {
      throw new RuntimeException("SpriteSheet for key " + key + " already exist!");
    }
    sheets.put(key, sheet);
  }

  public static void addAnimation(String key, Animation anim) {
    if (hasAnimation(key)) {
      throw new RuntimeException("Animation for key " + key + " already exist!");
    }
    animations.put(key, anim);
  }

  public static void addFont(String key, Font font) {
    if (hasFont(key)) {
      throw new RuntimeException("Font for key " + key + " already exist!");
    }
    fonts.put(key, font);
  }

  public static void addMusic(String key, Music music) {
    if (hasMusic(key)) {
      throw new RuntimeException("Music for key " + key + " already exist!");
    }
    songs.put(key, music);
  }

  public static void addSound(String key, Sound sound) {
    if (hasSound(key)) {
      throw new RuntimeException("Sound for key " + key + " already exist!");
    }
    sounds.put(key, sound);
  }

  public static void addParameter(String key, String value) {
    if (hasParameter(key)) {
      throw new RuntimeException("Parameter for key " + key + " already exist!");
    }
    parameters.put(key, value);
  }

  public static void addTiledMap(String key, TiledMap map) {
    if (hasTiledMap(key)) {
      throw new RuntimeException("TiledMap for key " + key + " already exist!");
    }
    tiledMaps.put(key, map);
  }

  public static boolean hasImage(String key) {
    return images.containsKey(key);
  }

  public static boolean hasSpriteSheet(String key) {
    return sheets.containsKey(key);
  }

  public static boolean hasAnimation(String key) {
    return animations.containsKey(key);
  }

  public static boolean hasFont(String key) {
    return fonts.containsKey(key);
  }

  public static boolean hasMusic(String key) {
    return songs.containsKey(key);
  }

  public static boolean hasSound(String key) {
    return sounds.containsKey(key);
  }

  public static boolean hasParameter(String key) {
    return parameters.containsKey(key);
  }

  public static boolean hasTiledMap(String key) {
    return tiledMaps.containsKey(key);
  }

  public static Image getImage(String key) {
    Image image = images.get(key);
    if (image == null)
      throw new RuntimeException("No image for key " + key + " " + images.keySet());
    return image;
  }

  public static SpriteSheet getSpriteSheet(String key) {
    SpriteSheet spriteSheet = sheets.get(key);
    if (spriteSheet == null)
      throw new RuntimeException("No spriteSheet for key " + key + " " + sheets.keySet());
    return spriteSheet;
  }

  public static Animation getAnimation(String key) {
    Animation anim = animations.get(key);
    if (anim == null)
      throw new RuntimeException("No Animation for key " + key + " " + animations.keySet());
    return anim;
  }

  public static Font getFont(String key) {
    Font font = fonts.get(key);
    if (font == null)
      throw new RuntimeException("No font for key " + key + " " + fonts.keySet());
    return font;
  }

  public static Music getMusic(String key) {
    Music music = songs.get(key);
    if (music == null)
      throw new RuntimeException("No music for key " + key + " " + songs.keySet());
    return music;
  }

  public static Sound getSound(String key) {
    Sound sound = sounds.get(key);
    if (sound == null)
      throw new RuntimeException("No sound for key " + key + " " + sounds.keySet());
    return sound;
  }

  public static int getInt(String key) {
    return Integer.parseInt(getParameter(key));
  }

  public static float getFloat(String key) {
    return Float.parseFloat(getParameter(key));
  }

  public static String getParameter(String key) {
    String val = parameters.get(key);
    if (val == null)
      throw new RuntimeException("No parameter for key " + key + " " + parameters.keySet());
    return val;
  }

  public static TiledMap getMap(String key) {
    TiledMap map = tiledMaps.get(key);
    if (map == null)
      throw new RuntimeException("No tilemap for key " + key + " " + tiledMaps.keySet());
    return map;
  }
}