package it.randomtower.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

public class ResourceManager {
private static float sfxVolume = 1.0f;
private static float musicVolume = 1.0f;

private static HashMap<String,Sound> sounds = new HashMap<String, Sound>();
private static HashMap<String,Music> songs = new HashMap<String, Music>();
private static HashMap<String,Image> images = new HashMap<String, Image>();
private static HashMap<String,SpriteSheet> sheets = new HashMap<String, SpriteSheet>();
private static HashMap<String,AngelCodeFont> fonts = new HashMap<String, AngelCodeFont>();
private static HashMap<String,Integer> ints = new HashMap<String, Integer>();
private static HashMap<String,Float> floats = new HashMap<String, Float>();
private static HashMap<String,String> strings = new HashMap<String, String>();

public static void loadResources(String ref) throws IOException {
loadResources(ResourceLoader.getResourceAsStream(ref));
}

public static void loadResources(File ref) throws IOException {
loadResources(new FileInputStream(ref));
}

public static void loadResources(InputStream ref) throws IOException {
try {
DocumentBuilder builder = DocumentBuilderFactory.newInstance()
.newDocumentBuilder();
Document document = builder.parse(ref);

Element element = document.getDocumentElement();
if (!element.getNodeName().equals("resources")) {
throw new IOException("Not a resource configuration file");
}
// load sounds
NodeList list = element.getElementsByTagName("sound");
for (int i = 0; i < list.getLength(); i++) {
loadSound((Element) list.item(i));
}
// load songs
list = element.getElementsByTagName("music");
for (int i = 0; i < list.getLength(); i++) {
loadMusic((Element) list.item(i));
}
// load images
list = element.getElementsByTagName("image");
for (int i = 0; i < list.getLength(); i++) {
loadImage((Element) list.item(i));
}
// load sheets
list = element.getElementsByTagName("sheet");
for (int i = 0; i < list.getLength(); i++) {
loadSpriteSheet((Element) list.item(i));
}
// load fonts
list = element.getElementsByTagName("angelcodefont");
for (int i = 0; i < list.getLength(); i++) {
loadAngelCodeFont((Element) list.item(i));
}
// load ints
list = element.getElementsByTagName("int");
for (int i = 0; i < list.getLength(); i++) {
loadInt((Element) list.item(i));
}
// load floats
list = element.getElementsByTagName("float");
for (int i = 0; i < list.getLength(); i++) {
loadFloat((Element) list.item(i));
}
// load strings
list = element.getElementsByTagName("string");
for (int i = 0; i < list.getLength(); i++) {
loadString((Element) list.item(i));
}
} catch (IOException e) {
Log.error(e);
throw e;
} catch (Exception e) {
Log.error(e);
throw new IOException("Unable to load resource configuration file");
}
}

private static void loadMusic(Element music) throws SlickException {
String key = music.getAttribute("key");
String file = music.getAttribute("file");
loadMusic(key, file);
}

public static void loadMusic(String key, String file) throws SlickException {
Log.debug("Trying to load music file '" + file + "' at key '" + key + "'...");
if (songs.get(key)!= null)
throw new SlickException("Music for key " + key + " already existing!");
Music song = new Music(file);
songs.put(key, song);
}

public static Music getMusic(String key) {
Music music = songs.get(key);
if (music == null)
Log.error("No music for key " + key + " found!");
return music;
}

private static void loadSound(Element snd) throws SlickException {
String key = snd.getAttribute("key");
String file = snd.getAttribute("file");
loadSound(key, file);
}

public static void loadSound(String key, String file) throws SlickException {
Log.debug("Trying to load sound file '" + file + "' at key '" + key + "'...");
if (sounds.get(key)!= null)
throw new SlickException("Sound for key " + key + " already existing!");
Sound sound = new Sound(file);
sounds.put(key, sound);
}

public static Sound getSound(String key) {
Sound sound = sounds.get(key);
if (sound == null)
Log.error("No sound for key " + key + " found!");
return sound;
}

private static void loadImage(Element img) throws SlickException {
String key = img.getAttribute("key");
String file = img.getAttribute("file");
String transColor = img.getAttribute("transparentColor");
Color transparentColor = null;
if (transColor != null && !transColor.isEmpty())
transparentColor = Color.decode(transColor);
else
transColor = null;
loadImage(key, file, transparentColor);
}

public static void loadImage(String key, String file, Color transparentColor) throws SlickException {
Log.debug("Trying to load image file '" + file + "' at key '" + key + "'...");
if (images.get(key)!= null)
throw new SlickException("Image for key " + key + " already existing!");
Image image;
if (transparentColor != null)
image = new Image(file, transparentColor);
else
image = new Image(file);
images.put(key, image);
}

public static Image getImage(String key) {
Image image = images.get(key);
if (image == null)
Log.error("No image for key " + key + " found!");
return image;
}

private static void loadSpriteSheet(Element sprsheet) throws SlickException {
String key = sprsheet.getAttribute("key");
String file = sprsheet.getAttribute("file");
int width = Integer.parseInt(sprsheet.getAttribute("width"));
int height = Integer.parseInt(sprsheet.getAttribute("height"));
String transColor = sprsheet.getAttribute("transparentColor");
Color transparentColor = null;
if (transColor != null && !transColor.isEmpty())
transparentColor = Color.decode(transColor);
else
transColor = null;
loadSpriteSheet(key, file, width, height, transparentColor);
}

public static void loadSpriteSheet(String key, String file, int width, int height, Color transparentColor) throws SlickException {
Log.debug("Trying to load spritesheet file '" + file + "' with width " + width +
" and height " + height +
((transparentColor == null) ? " without transparent color" : " with transparent color '" + transparentColor.toString() + "'" ) + " at key '" + key + "'...");
if (sheets.get(key)!= null)
throw new SlickException("SpriteSheet for key " + key + " already existing!");
SpriteSheet spriteSheet = null;
if (transparentColor == null)
spriteSheet = new SpriteSheet(file, width, height);
else
spriteSheet = new SpriteSheet(file, width, height, transparentColor);
sheets.put(key, spriteSheet);

}

public static SpriteSheet getSpriteSheet(String key) {
SpriteSheet spriteSheet = sheets.get(key);
if (spriteSheet == null)
Log.error("No SpriteSheet for key " + key + " found!");
return spriteSheet;
}

private static void loadAngelCodeFont(Element fnt) throws SlickException {
String key = fnt.getAttribute("key");
String fntfile = fnt.getAttribute("fontFile");
String imagefile = fnt.getAttribute("imageFile");
loadAngelCodeFont(key, fntfile, imagefile);
}

public static void loadAngelCodeFont(String key, String fontFile, String imageFile) throws SlickException {
Log.debug("Trying to load Angelcode font file '" + fontFile + "' and imagefile '" + imageFile
+ "' at key '" + key + "'...");
if (fonts.get(key)!= null)
throw new SlickException("AngelCodeFont for key " + key + " already existing!");
// load AngelCodeFonts with caching enabled to speed up rendering
AngelCodeFont font = new AngelCodeFont(fontFile, imageFile, true);
fonts.put(key, font);
}

public static AngelCodeFont getAngelCodeFont(String key) {
AngelCodeFont font = fonts.get(key);
if (font == null)
Log.error("No AngelCodeFont for key " + key + " found!");
return font;
}

private static void loadInt(Element intval) throws SlickException {
String key = intval.getAttribute("key");
String value = intval.getAttribute("value");
int val = Integer.parseInt(value);
setInt(key, val);
}

public static void setInt(String key, int value) {
ints.put(key, value);
}

public static int getInt(String key) {
Integer intval = ints.get(key);
if (intval == null)
Log.error("No int for key " + key + " found!");
return intval;
}

private static void loadFloat(Element floatval) throws SlickException {
String key = floatval.getAttribute("key");
String value = floatval.getAttribute("value");
float val = Float.parseFloat(value);
setFloat(key, val);
}

public static void setFloat(String key, float value) {
floats.put(key, value);
}

public static float getFloat(String key) {
Float floatval = floats.get(key);
if (floatval == null)
Log.error("No float for key " + key + " found!");
return floatval;
}

private static void loadString(Element stringval) throws SlickException {
String key = stringval.getAttribute("key");
String value = stringval.getAttribute("value");
setString(key, value);
}

public static void setString(String key, String value) {
strings.put(key, value);
}

public static String getString(String key) {
String val = strings.get(key);
if (val == null)
Log.error("No string for key" + key + " found!");
return val;
}

/**
* set the volume of all sound effects to given volume
* @param sfxVolume a value between 0 and 1
*/
public static void setSfxVolume(float volume) {
sfxVolume = volume;
SoundStore.get().setSoundVolume(sfxVolume);
Log.debug("sfx volume set to " + sfxVolume);
}

/**
* set the volume of all songs to given volume
* @param musicVolume a value between 0 and 1
*/
public static void setMusicVolume(float volume) {
musicVolume = volume;
SoundStore.get().setMusicVolume(musicVolume);
Log.debug("music volume set to " + musicVolume);
}

public static float getMusicVolume() {
return musicVolume;
}

public static float getSfxVolume() {
return sfxVolume;
}

}

