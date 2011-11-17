package it.marteEngine;

import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

/**
 * The XMLResourceLoader parses an xml document.
 * and reads the following known elements:
 * basedir path(req)
 * sound key file
 * music key file
 * image key file transparentColor(opt)
 * sheet key file width height transparentColor(opt)
 * anim key imgName frameDuration frames(opt) flipVertical(opt) flipHorizontal(opt)
 * angelcodefont key fontFile imageFile
 * unicodefont   key file fontSize(opt)
 * map key file
 * param key value
 * <p/>
 * A parameter followed by (req) means Required and by (opt) means Optional.
 * The file parameter is relative to the basedir.
 * <p/>
 * Examples:
 * <sound key="jump" file="jump.wav" />
 * <music key="sleepy" file="sleep.wav" />
 * <image key="ship" file="ship.png" />
 * <sheet key="monster1" file="invader.png" width="25" height="25" transparentColor="000000" />
 * <anim key="homer" imgName="homeranim" frameDuration="250" row="0" frames="1,2,3,4,5" flipVertical="true" flipHorizontal="true"/>
 * <unicodefont key="default" file="gbb.ttf" fontSize="20" />
 * <angelcodefont key="font" fontFile="font.fnt" imageFile="font.png" />
 * <map key="level1" file="level1.tmx" />
 * <param key="bossLevel" value="11" />
 */
public class XMLResourceLoader {
  private Element[] NO_ELEMENTS = new Element[0];
  private String baseDir;
  private Element rootElement;

  public void load(InputStream in) throws IOException {
    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document document = builder.parse(in);
      rootElement = document.getDocumentElement();
      validateResourceFile();

      Element[] elements = getElementsByTagName("basedir");
      setBaseDirectory(elements[0].getAttribute("path"));

      for (Element element : getElementsByTagName("sound")) {
        loadSound(element);
      }
      for (Element element : getElementsByTagName("music")) {
        loadMusic(element);
      }
      for (Element element : getElementsByTagName("image")) {
        loadImage(element);
      }
      for (Element element : getElementsByTagName("sheet")) {
        loadSpriteSheet(element);
      }
      for (Element element : getElementsByTagName("anim")) {
        loadAnimation(element);
      }
      for (Element element : getElementsByTagName("angelcodefont")) {
        loadAngelCodeFont(element);
      }
      for (Element element : getElementsByTagName("unicodefont")) {
        loadUnicodeFont(element);
      }
      for (Element element : getElementsByTagName("map")) {
        loadTiledMap(element);
      }
      for (Element element : getElementsByTagName("param")) {
        loadParameter(element);
      }
    } catch (IOException e) {
      Log.error(e);
      throw e;
    } catch (Exception e) {
      Log.error(e);
      throw new IOException("Unable to load resource configuration file");
    }
  }

  private Element[] getElementsByTagName(String elementName) {
    NodeList nodes = rootElement.getElementsByTagName(elementName);
    int nodeCount = nodes.getLength();

    if (nodeCount != 0) {
      Element[] elements = new Element[nodeCount];

      for (int i = 0; i < nodeCount; i++) {
        Element element = (Element) nodes.item(i);
        elements[i] = element;
      }
      return elements;
    } else {
      return NO_ELEMENTS;
    }
  }

  private void validateResourceFile() throws IOException {
    if (!rootElement.getNodeName().equals("resources")) {
      throw new IOException("Not a resource configuration file");
    }
  }

  public void setBaseDirectory(String baseDirectory) throws SlickException {
    Log.debug("Setting base directory to " + baseDirectory);
    if (baseDirectory == null || baseDirectory.isEmpty()) {
      throw new SlickException("BaseDirectory cannot be null or empty!");
    }
    baseDir = baseDirectory;
    if (!baseDir.endsWith("/")) {
      baseDir += "/";
    }
  }

  private void loadSound(Element element) throws SlickException {
    String key = element.getAttribute("key");
    String file = element.getAttribute("file");
    Log.debug(formatLoadMsg("sound", key, file));
    ResourceManager.addSound(key, new Sound(baseDir + file));
  }

  private void loadMusic(Element element) throws SlickException {
    String key = element.getAttribute("key");
    String file = element.getAttribute("file");
    Log.debug(formatLoadMsg("music", key, file));
    ResourceManager.addMusic(key, new Music(baseDir + file));
  }

  private void loadImage(Element element) throws SlickException {
    String key = element.getAttribute("key");
    String file = element.getAttribute("file");
    String transparentColorAsText = element.getAttribute("transparentColor");

    Color transparentColor = null;
    if (transparentColorAsText != null && !transparentColorAsText.isEmpty()) {
      transparentColor = Color.decode(transparentColorAsText);
    }

    Image image;
    try {
      image = new Image(baseDir + file, transparentColor);
    } catch (SlickException ex) {
      // For textures larger than 512x512 old cards throws error
      Log.info("Using BigImage.");
      image = new BigImage(baseDir + file);
    }
    Log.debug(formatLoadMsg("image", key, file));
    ResourceManager.addImage(key, image);
  }

  private void loadSpriteSheet(Element element) throws SlickException {
    String key = element.getAttribute("key");
    String file = element.getAttribute("file");
    int width = Integer.parseInt(element.getAttribute("width"));
    int height = Integer.parseInt(element.getAttribute("height"));
    String transparentColorAsText = element.getAttribute("transparentColor");

    Color transparentColor = null;
    if (transparentColorAsText != null && !transparentColorAsText.isEmpty()) {
      transparentColor = Color.decode(transparentColorAsText);
    }

    Log.debug(String.format("Loading spritesheet key=%s file=%s width=%s height=%s", key, file, width, height));
    ResourceManager.addSpriteSheet(key, new SpriteSheet(baseDir + file, width, height, transparentColor));
  }

  private void loadAnimation(Element element) {
    String key = element.getAttribute("key");
    String imgName = element.getAttribute("imgName");
    int frameDuration = Integer.parseInt(element.getAttribute("frameDuration"));
    boolean flipHorizontal = false, flipVertical = false;
    int[] frames = null;
    int row = 0;

    if (element.hasAttribute("frames")) {
      frames = readFrameIndexes(element);
    }
    if (element.hasAttribute("flipHorizontal")) {
      flipHorizontal = Boolean.parseBoolean(element.getAttribute("flipHorizontal"));
    }
    if (element.hasAttribute("flipVertical")) {
      flipVertical = Boolean.parseBoolean(element.getAttribute("flipVertical"));
    }
    if (element.hasAttribute("row")) {
      row = Integer.parseInt(element.getAttribute("row"));
    }

    SpriteSheet sheet = ResourceManager.getSpriteSheet(imgName);
    Animation anim;

    if (frames == null && !flipHorizontal && !flipVertical) {
      // Create Animation from the entire row
      anim = new Animation(false);
      for (int i = 0; i < sheet.getHorizontalCount(); i++) {
        anim.addFrame(sheet.getSubImage(i, row), frameDuration);
      }
    } else {
      if (frames != null) {
        anim = buildAnimationFromFrames(sheet, row, frames, frameDuration, flipHorizontal, flipVertical);
      } else {
        int[] allFrames = new int[sheet.getHorizontalCount()];
        anim = buildAnimationFromFrames(sheet, row, allFrames, frameDuration, flipHorizontal, flipVertical);
      }
    }
    ResourceManager.addAnimation(key, anim);
  }

  /**
   * Create an animation from a set of frames in the spritesheet at a given row.
   */
  private Animation buildAnimationFromFrames(SpriteSheet sheet, int row, int[] frames, int frameDuration, boolean flipHorizontal, boolean flipVertical) {
    Animation animation = new Animation(false);
    for (int frameIndex : frames) {
      Image img = sheet.getSubImage(frameIndex, row);
      if (flipHorizontal || flipVertical) {
        img = img.getFlippedCopy(flipHorizontal, flipVertical);
        animation.addFrame(img, frameDuration);
      } else {
        animation.addFrame(img, frameDuration);
      }
    }
    return animation;
  }

  private int[] readFrameIndexes(Element element) {
    String framesAsText = element.getAttribute("frames");
    StringTokenizer tokenizer = new StringTokenizer(framesAsText, ",");

    int frames[] = new int[tokenizer.countTokens()];
    for (int i = 0; tokenizer.hasMoreTokens(); i++) {
      frames[i] = Integer.parseInt(tokenizer.nextToken().trim());
    }
    return frames;
  }

  private void loadAngelCodeFont(Element element) throws SlickException {
    String key = element.getAttribute("key");
    String fontFile = element.getAttribute("fontFile");
    String imageFile = element.getAttribute("imageFile");

    Log.debug(String.format("Loading Angelcode font key=%s imagefile=%s fontfile=%s", key, imageFile, fontFile));
    // load AngelCodeFonts with caching enabled to speed up rendering
    AngelCodeFont font = new AngelCodeFont(baseDir + fontFile, baseDir + imageFile, true);
    ResourceManager.addFont(key, font);
  }

  @SuppressWarnings("unchecked")
  private void loadUnicodeFont(Element element) throws SlickException {
    String key = element.getAttribute("key");
    String ttfFile = element.getAttribute("file");
    String fontSizeAsText = element.getAttribute("fontSize");
    int fontSize;

    if (!fontSizeAsText.isEmpty()) {
      fontSize = Integer.valueOf(fontSizeAsText);
    } else {
      fontSize = 12;
    }

    Log.debug(formatLoadMsg("Unicode font", key, ttfFile));
    UnicodeFont unicodeFont = new UnicodeFont(baseDir + ttfFile, fontSize, false, false);
    unicodeFont.getEffects().add(new ColorEffect());
    unicodeFont.addAsciiGlyphs();
    unicodeFont.loadGlyphs();
    unicodeFont.setDisplayListCaching(true);
    ResourceManager.addFont(key, unicodeFont);
  }

  public void loadTiledMap(Element element) throws SlickException {
    String key = element.getAttribute("key");
    String file = element.getAttribute("file");

    Log.debug(formatLoadMsg("Tiled map", key, file));
    TiledMap tiledMap = new TiledMap(baseDir + file);
    ResourceManager.addTiledMap(key, tiledMap);
  }

  private void loadParameter(Element element) throws SlickException {
    String key = element.getAttribute("key");
    String value = element.getAttribute("value");

    Log.debug(formatLoadMsg("Parameter", key, "value", value));
    ResourceManager.addParameter(key, value);
  }

  private String formatLoadMsg(String type, String key, String value) {
    return formatLoadMsg(type, key, "file", value);
  }

  private String formatLoadMsg(String type, String key, String valueName, String value) {
    StringBuffer buf = new StringBuffer("Loading ");
    buf.append(type)
        .append(" key=").append(key).append(" ")
        .append(valueName).append("=").append(value);
    return buf.toString();
  }
}
