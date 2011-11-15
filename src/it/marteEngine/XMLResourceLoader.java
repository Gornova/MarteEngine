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

/**
 * The XMLResourceLoader parses an xml document.
 * and reads the following known elements:
 * (required) basedir path
 * sound key file
 * music key file
 * image key file
 * sheet key file width height transparent-color(optional)
 * angelcodefont key font-file image-file
 * ttf key font-file image-file
 * map key file
 * param key value
 * The file parameter is relative to the basedir.
 */
public class XMLResourceLoader {
  private String baseDir;

  public void load(InputStream in) throws IOException {
    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document document = builder.parse(in);
      Element rootElement = document.getDocumentElement();
      validateResourceFile(rootElement);

      // first look for basedir
      NodeList list = rootElement.getElementsByTagName("basedir");
      for (int i = 0; i < list.getLength(); i++) {
        Element element = (Element) list.item(i);
        setBaseDirectory(element.getAttribute("path"));
      }

      // load sounds
      list = rootElement.getElementsByTagName("sound");
      for (int i = 0; i < list.getLength(); i++) {
        loadSound((Element) list.item(i));
      }
      // load songs
      list = rootElement.getElementsByTagName("music");
      for (int i = 0; i < list.getLength(); i++) {
        loadMusic((Element) list.item(i));
      }
      // load images
      list = rootElement.getElementsByTagName("image");
      for (int i = 0; i < list.getLength(); i++) {
        loadImage((Element) list.item(i));
      }
      // load sheets
      list = rootElement.getElementsByTagName("sheet");
      for (int i = 0; i < list.getLength(); i++) {
        loadSpriteSheet((Element) list.item(i));
      }

      // load fonts
      list = rootElement.getElementsByTagName("angelcodefont");
      for (int i = 0; i < list.getLength(); i++) {
        loadAngelCodeFont((Element) list.item(i));
      }
      list = rootElement.getElementsByTagName("unicodefont");
      for (int i = 0; i < list.getLength(); i++) {
        loadUnicodeFont((Element) list.item(i));
      }
      // load maps
      list = rootElement.getElementsByTagName("map");
      for (int i = 0; i < list.getLength(); i++) {
        loadTiledMap((Element) list.item(i));
      }
      // load parameters
      list = rootElement.getElementsByTagName("param");
      for (int i = 0; i < list.getLength(); i++) {
        loadParameter((Element) list.item(i));
      }
    } catch (IOException e) {
      Log.error(e);
      throw e;
    } catch (Exception e) {
      Log.error(e);
      throw new IOException("Unable to load resource configuration file");
    }
  }

  private void validateResourceFile(Element element) throws IOException {
    if (!element.getNodeName().equals("resources")) {
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
    Log.debug(formatLoadMsg("image", key, file));
    ResourceManager.addImage(key, new Image(baseDir + file));
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

    Log.debug(String.format("Loading spritesheet key=%s width=%s height=%s file=%s", file, width, height, key));
    ResourceManager.addSpriteSheet(key, new SpriteSheet(baseDir + file, width, height, transparentColor));
  }

  private void loadAngelCodeFont(Element element) throws SlickException {
    String key = element.getAttribute("key");
    String fontFile = element.getAttribute("fontFile");
    String imageFile = element.getAttribute("imageFile");

    Log.debug(String.format("Loading Angelcode font file key=%s imagefile=%s fontfile=%s", key, imageFile, fontFile));
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

    Log.debug(formatLoadMsg("Unicode", key, ttfFile));
    UnicodeFont unicodeFont = new UnicodeFont(baseDir + ttfFile, fontSize, false, false);
    unicodeFont.getEffects().add(new ColorEffect());
    unicodeFont.addAsciiGlyphs();
    unicodeFont.loadGlyphs();
    unicodeFont.setDisplayListCaching(false);
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
