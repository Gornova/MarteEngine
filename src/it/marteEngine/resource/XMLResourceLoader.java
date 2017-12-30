package it.marteEngine.resource;

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
 * The XMLResourceLoader parses an xml document and reads the following known
 * elements:
 * basedir path (required, all resources are relative to this directory)
 * sound key file
 * music key file
 * image key file transparentColor(opt)
 * sheet key file width height transparentColor(opt)
 * anim key imgName frameDuration row frames(default=all horizontal frames)
 * flipVertical(opt) flipHorizontal(opt) loop(default=true)
 * angelcodefont key fontFile imageFile
 * unicodefont key file fontSize(default=12)
 * spritesheetfont key file firstchar
 * map key file
 * param key value
 *
 * The key is the reference to the resource. All parameters are required except
 * when the parameter is suffixed with (opt) which stands for optional.
 * Some parameters are optional because they have a default value.
 *
 * Examples:
 * <sound key="jump" file="jump.wav" />
 * <music key="sleepy" file="sleep.wav" />
 * <image key="ship" file="ship.png" />
 * <sheet key="homer" file="homer.png" width="25" height="25" transparentColor="000000" />
 * <anim key="homer" imgName="homer" frameDuration="250" row="0" frames="1,2,3,4,5"
 * flipVertical="true" flipHorizontal="true" loop="false"/>
 * <unicodefont key="default" file="gbb.ttf" fontSize="20" />
 * <angelcodefont key="font" fontFile="font.fnt" imageFile="font.png" />
 * <map key="level1" file="level1.tmx" />
 * <param key="bossLevel" value="11" />
 */
public class XMLResourceLoader {
	private static final Element[] NO_ELEMENTS = new Element[0];
	private String baseDir;
	private Element rootElement;

	public void load(InputStream in) throws IOException {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setValidating(true);

			DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
			builder.setErrorHandler(new XmlErrorHandler());

			Document document = builder.parse(in);
			rootElement = document.getDocumentElement();
			validateResourceFile();

			String baseDir = rootElement.getAttribute("basedir");
			setBaseDirectory(baseDir);

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
			for (Element element : getElementsByTagName("spritesheetfont")) {
				loadSpriteSheetFont(element);
			}
			for (Element element : getElementsByTagName("map")) {
				loadTiledMap(element);
			}
			for (Element element : getElementsByTagName("param")) {
				loadParameter(element);
			}
		} catch (Exception e) {
			Log.error(e);
			throw new IOException(
					"Unable to load resource configuration file", e);
		}
	}

	private void validateResourceFile() throws IOException {
		if (!rootElement.getNodeName().equals("resources")) {
			throw new IOException("Not a resource configuration file");
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

	private void setBaseDirectory(String baseDir) {
		if (!baseDir.endsWith("/")) {
			baseDir += "/";
		}
		this.baseDir = baseDir;
		Log.debug("Setting base directory to " + baseDir);
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
		Color transparentColor = null;

		if (element.hasAttribute("transparentColor")) {
			transparentColor = decodeColor(element);
		}

		Image image;
		try {
			image = new Image(baseDir + file, transparentColor);
		} catch (SlickException ex) {
			// To support large textures on older graphic cards
			Log.info("Using BigImage.");
			image = new BigImage(baseDir + file);
		}
		Log.debug(formatLoadMsg("image", key, file));
		ResourceManager.addImage(key, image);
	}

	private void loadSpriteSheet(Element element) throws SlickException {
		String key = element.getAttribute("key");
		String file = element.getAttribute("file");
		int width = parseIntAttribute(element, "width");
		int height = parseIntAttribute(element, "height");
		Color transparentColor = null;

		if (element.hasAttribute("transparentColor")) {
			transparentColor = decodeColor(element);
		}

		Log.debug(String.format(
				"Loading spritesheet key=%s file=%s width=%s height=%s", key,
				file, width, height));
		ResourceManager.addSpriteSheet(key, new SpriteSheet(baseDir + file,
				width, height, transparentColor));
	}

	private Color decodeColor(Element element) {
		String transparentColorAsText = element.getAttribute("transparentColor");
		if (transparentColorAsText != null && !transparentColorAsText.isEmpty()) {
			return Color.decode(transparentColorAsText);
		} else {
			return null;
		}
	}

	private void loadAnimation(Element element) {
		String key = element.getAttribute("key");
		String imgName = element.getAttribute("imgName");
		int frameDuration = parseIntAttribute(element, "frameDuration");
		int row = parseIntAttribute(element, "row");

		if (!ResourceManager.hasSpriteSheet(imgName)) {
			throw new IllegalArgumentException("Animation " + key
					+ " needs spritesheet " + imgName
					+ " but it has not been loaded.");
		}

		SpriteSheet sheet = ResourceManager.getSpriteSheet(imgName);
		int[] frames = getHorizontalFrames(element, sheet.getHorizontalCount());
		boolean flipHorizontal = parseBooleanAttribute(element,
				"flipHorizontal", false);
		boolean flipVertical = parseBooleanAttribute(element, "flipVertical",
				false);
		boolean loop = parseBooleanAttribute(element, "loop", true);

		Animation anim = buildAnimationFromFrames(sheet, row, frames,
				frameDuration, flipHorizontal, flipVertical);
		anim.setLooping(loop);
		Log.debug(formatLoadMsg("animation", key, "spritesheet", imgName));
		ResourceManager.addAnimation(key, anim);
	}

	private int[] getHorizontalFrames(Element element, int allFrames) {
		int[] frames;
		if (element.hasAttribute("frames")) {
			String framesAsText = element.getAttribute("frames");
			frames = readFrameIndexes(framesAsText);
		} else {
			frames = new int[allFrames];
			for (int i = 0; i < allFrames; i++) {
				frames[i] = i;
			}
		}
		return frames;
	}

	private int[] readFrameIndexes(String framesAsText) {
		StringTokenizer tokenizer = new StringTokenizer(framesAsText, ",");

		int frames[] = new int[tokenizer.countTokens()];
		for (int i = 0; tokenizer.hasMoreTokens(); i++) {
			frames[i] = Integer.parseInt(tokenizer.nextToken().trim());
		}
		return frames;
	}

	/**
	 * Create an animation from a spritesheet row.
	 *
	 * @param sheet
	 *            The spritesheet that contains the individual images.
	 * @param row
	 *            The horizontal index in the spritesheet to read the animation
	 *            from, zero based.
	 * @param frames
	 *            The column indexes on the given row that need to be included
	 *            in the animation, zero based.
	 * @param frameDuration
	 *            The duration of 1 frame in the resulting animation.
	 * @param flipHorizontal
	 *            If true flips each frame in the animation over the y axis
	 * @param flipVertical
	 *            If true flips each frame in the animation over the x axis
	 * @return An animation parsed from the spritesheet
	 */
	private Animation buildAnimationFromFrames(SpriteSheet sheet, int row,
			int[] frames, int frameDuration, boolean flipHorizontal,
			boolean flipVertical) {
		Animation animation = new Animation(false);
		for (int frameIndex : frames) {
			Image img = sheet.getSubImage(frameIndex, row);

			if (flipHorizontal || flipVertical) {
				img = img.getFlippedCopy(flipHorizontal, flipVertical);
			}
			animation.addFrame(img, frameDuration);
		}
		return animation;
	}

	private void loadAngelCodeFont(Element element) throws SlickException {
		String key = element.getAttribute("key");
		String fontFile = element.getAttribute("fontFile");
		String imageFile = element.getAttribute("imageFile");

		Log.debug(String.format(
				"Loading Angelcode font key=%s imagefile=%s fontfile=%s", key,
				imageFile, fontFile));
		AngelCodeFont font = new AngelCodeFont(baseDir + fontFile, baseDir
				+ imageFile, true);
		ResourceManager.addFont(key, font);
	}

	@SuppressWarnings("unchecked")
	private void loadUnicodeFont(Element element) throws SlickException {
		String key = element.getAttribute("key");
		String ttfFile = element.getAttribute("file");
		int fontSize = parseIntAttribute(element, "fontSize", 12);

		Log.debug(formatLoadMsg("Unicode font", key, ttfFile));
		UnicodeFont unicodeFont = new UnicodeFont(baseDir + ttfFile, fontSize,
				false, false);
		unicodeFont.getEffects().add(new ColorEffect());
		unicodeFont.addAsciiGlyphs();
		unicodeFont.loadGlyphs();
		unicodeFont.setDisplayListCaching(true);
		ResourceManager.addFont(key, unicodeFont);
	}

	private void loadSpriteSheetFont(Element element) {
		String key = element.getAttribute("key");
		String imgName = element.getAttribute("file");
		char startingChar = element.getAttribute("firstchar").charAt(0);

		if (!ResourceManager.hasSpriteSheet(imgName)) {
			throw new IllegalArgumentException("Spritesheetfont " + key
					+ " needs spritesheet " + imgName
					+ " but it has not been loaded.");
		}
		SpriteSheet sheet = ResourceManager.getSpriteSheet(imgName);

		Log.debug(formatLoadMsg("SpriteSheet font", key, imgName));
		SpriteSheetFont font = new SpriteSheetFont(sheet, startingChar);
		ResourceManager.addFont(key, font);
	}

	private void loadTiledMap(Element element) throws SlickException {
		String key = element.getAttribute("key");
		String file = element.getAttribute("file");

		Log.debug(formatLoadMsg("Tiled map", key, file));
		TiledMap tiledMap = new TiledMap(baseDir + file);
		ResourceManager.addTiledMap(key, tiledMap);
	}

	private void loadParameter(Element element) {
		String key = element.getAttribute("key");
		String value = element.getAttribute("value");

		Log.debug(formatLoadMsg("Parameter", key, "value", value));
		ResourceManager.setParameter(key, value);
	}

	private int parseIntAttribute(Element element, String attributeName,
			int defaultValue) {
		if (element.hasAttribute(attributeName)) {
			return parseIntAttribute(element, attributeName);
		} else {
			return defaultValue;
		}
	}

	private int parseIntAttribute(Element element, String attributeName) {
		return Integer.parseInt(element.getAttribute(attributeName));
	}

	private boolean parseBooleanAttribute(Element element,
			String attributeName, boolean defaultValue) {
		if (element.hasAttribute(attributeName)) {
			return parseBooleanAttribute(element, attributeName);
		} else {
			return defaultValue;
		}
	}

	private boolean parseBooleanAttribute(Element element, String attributeName) {
		return Boolean.parseBoolean(element.getAttribute(attributeName));
	}

	private String formatLoadMsg(String type, String key, String value) {
		return formatLoadMsg(type, key, "file", value);
	}

	private String formatLoadMsg(String type, String key, String valueName,
			String value) {
		return String.format("Loading %s key=%s %s=%s", type, key, valueName, value);
	}
}
