package it.marteEngine;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.ResourceLoader;

/**
 * Provides convenient access to all game resources. Each of these resources is
 * mapped to a key eg "SELECT_SOUND" -> Sound object.
 *
 * If an attempt is made to overwrite an existing resource an
 * IllegalArgumentException is thrown.This is true for all resources except the
 * parameters, the parameter values can be overwritten.
 */
public final class ResourceManager {
	private static final Map<String, Music> songs = new HashMap<String, Music>();
	private static final Map<String, Sound> sounds = new HashMap<String, Sound>();
	private static final Map<String, Image> images = new HashMap<String, Image>();
	private static final Map<String, SpriteSheet> sheets = new HashMap<String, SpriteSheet>();
	private static final Map<String, Animation> animations = new HashMap<String, Animation>();
	private static final Map<String, Font> fonts = new HashMap<String, Font>();
	private static final Map<String, String> parameters = new HashMap<String, String>();
	private static final Map<String, TiledMap> tiledMaps = new HashMap<String, TiledMap>();

	/**
	 * This constructor is intentionally made private. Use the static methods.
	 */
	private ResourceManager() {
	}

	/**
	 * @param ref
	 *            The path to the resource file
	 * @throws SlickException
	 *             If the resources could not be loaded
	 * @see it.marteEngine.XMLResourceLoader
	 */
	public static void loadResources(String ref) throws SlickException {
		loadResources(ResourceLoader.getResourceAsStream(ref));
	}

	/**
	 * @param in
	 *            The stream to the resource file
	 * @throws SlickException
	 *             If the resources could not be loaded
	 * @see it.marteEngine.XMLResourceLoader
	 */
	public static void loadResources(InputStream in) throws SlickException {
		try {
			XMLResourceLoader resourceLoader = new XMLResourceLoader();
			resourceLoader.load(in);
		} catch (IOException e) {
			throw new SlickException("Resource loading failed: "
					+ e.getMessage());
		}
	}

	public static void addImage(String key, Image image) {
		if (hasImage(key)) {
			throw new IllegalArgumentException("Image for key " + key
					+ " already exist!");
		}
		images.put(key, image);
	}

	public static void addSpriteSheet(String key, SpriteSheet sheet) {
		if (hasSpriteSheet(key)) {
			throw new IllegalArgumentException("SpriteSheet for key " + key
					+ " already exist!");
		}
		sheets.put(key, sheet);
	}

	public static void addAnimation(String key, Animation anim) {
		if (hasAnimation(key)) {
			throw new IllegalArgumentException("Animation for key " + key
					+ " already exist!");
		}
		animations.put(key, anim);
	}

	public static void addFont(String key, Font font) {
		if (hasFont(key)) {
			throw new IllegalArgumentException("Font for key " + key
					+ " already exist!");
		}
		fonts.put(key, font);
	}

	public static void addMusic(String key, Music music) {
		if (hasMusic(key)) {
			throw new IllegalArgumentException("Music for key " + key
					+ " already exist!");
		}
		songs.put(key, music);
	}

	public static void addSound(String key, Sound sound) {
		if (hasSound(key)) {
			throw new IllegalArgumentException("Sound for key " + key
					+ " already exist!");
		}
		sounds.put(key, sound);
	}

	public static void setParameter(String key, String value) {
		parameters.put(key, value);
	}

	public static void addTiledMap(String key, TiledMap map) {
		if (hasTiledMap(key)) {
			throw new IllegalArgumentException("TiledMap for key " + key
					+ " already exist!");
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
			throw new IllegalArgumentException("No image for key " + key + " "
					+ images.keySet());
		return image;
	}

	public static SpriteSheet getSpriteSheet(String key) {
		SpriteSheet spriteSheet = sheets.get(key);
		if (spriteSheet == null)
			throw new IllegalArgumentException("No spriteSheet for key " + key
					+ " " + sheets.keySet());
		return spriteSheet;
	}

	public static Animation getAnimation(String key) {
		Animation anim = animations.get(key);
		if (anim == null)
			throw new IllegalArgumentException("No Animation for key " + key
					+ " " + animations.keySet());
		return anim;
	}

	public static Font getFont(String key) {
		Font font = fonts.get(key);
		if (font == null)
			throw new IllegalArgumentException("No font for key " + key + " "
					+ fonts.keySet());
		return font;
	}

	public static Music getMusic(String key) {
		Music music = songs.get(key);
		if (music == null)
			throw new IllegalArgumentException("No music for key " + key + " "
					+ songs.keySet());
		return music;
	}

	public static Sound getSound(String key) {
		Sound sound = sounds.get(key);
		if (sound == null)
			throw new IllegalArgumentException("No sound for key " + key + " "
					+ sounds.keySet());
		return sound;
	}

	public static int getInt(String key) {
		return Integer.parseInt(getParameter(key));
	}

	public static double getDouble(String key) {
		return Double.parseDouble(getParameter(key));
	}

	public static float getFloat(String key) {
		return Float.parseFloat(getParameter(key));
	}

	public static String getParameter(String key) {
		String val = parameters.get(key);
		if (val == null)
			throw new IllegalArgumentException("No parameter for key " + key
					+ " " + parameters.keySet());
		return val;
	}

	public static TiledMap getMap(String key) {
        TiledMap map = tiledMaps.get(key);
        if (map == null)
            throw new IllegalArgumentException("No tiledmap for key " + key + " " + tiledMaps.keySet());
        return map;
    }
}