package it.marteEngine;

import it.marteEngine.entity.Entity;

import java.util.Hashtable;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Marte Engine Utility class
 * 
 * @author RandomTower
 * @project MarteEngine
 */
public class ME {

	/** true if debug is enabled, shows hitbox of entities **/
	public static boolean debugEnabled = false;
	/** place debug information on top or at bottom **/
	public static boolean debugWindowAtTop = true;
	/** key to activate debug mode **/
	public static int keyToggleDebug = -1;
	/** default border color of hitbox in debug mode **/
	public static Color borderColor = Color.red;
	/** key for restarting game **/
	public static int keyRestart = -1;
	/** key for mute music and sounds **/
	public static int keyMuteMusic = -1;
	public static boolean playMusic = true;
	/** key for full screen mode **/
	public static int keyFullScreen = -1;

	/** x scale factor for graphics, default 1 (nothing) **/
	public static float scaleX = 1;
	/** y scale factor for graphics, default 1 (nothing) **/
	public static float scaleY = 1;

	/** top z order **/
	public static final Integer Z_LEVEL_TOP = 100;

	public static final String WALK_LEFT = "walk_Left";
	public static final String WALK_RIGHT = "walk_Right";
	public static final String WALK_UP = "walk_Up";
	public static final String WALK_DOWN = "walk_Down";

	/** utility hashtable for game attributes **/
	public static Hashtable<String, Object> attributes = new Hashtable<String, Object>();

	public static World world;

	/** do we base time calculations on delta timing or on frames per second? */
	public static boolean useDeltaTiming = false;
	/** value of current delta of update call. might be helpful here */
	public static int delta;
	/** the frames per seconds we targeted in our main class */
	public static int targetFrameRate;

	public static ParticleSystem ps;
	public static boolean renderParticle = false;

	public static void setTargetFrameRate(GameContainer container,
			int targetframerate) {
		container.setTargetFrameRate(targetframerate);
		ME.targetFrameRate = targetframerate;

	}

	/**
	 * Update entities and add new entities and remove old entities
	 * 
	 * @param container
	 * @param delta
	 * @throws SlickException
	 */
	public static void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		if (container == null)
			throw new SlickException("no container set");
		if (world == null)
			throw new SlickException("no world set");

		// special key handling
		if (keyToggleDebug != -1) {
			if (container.getInput().isKeyPressed(keyToggleDebug)) {
				debugEnabled = debugEnabled ? false : true;
			}
		}
		if (keyRestart != -1) {
			if (container.getInput().isKeyPressed(keyRestart)) {
				ME.world.clear();
				ME.world.init(container, game);
				// TODO: go to first state?
			}
		}
		if (keyMuteMusic != -1) {
			if (container.getInput().isKeyPressed(keyMuteMusic)) {
				playMusic = playMusic ? false : true;
				muteMusic();
			}
		}
		if (keyFullScreen != -1) {
			if (container.getInput().isKeyPressed(keyFullScreen)) {
				container
						.setFullscreen(container.isFullscreen() ? false : true);
			}
		}

		if (ME.ps != null) {
			ME.ps.update(delta);
		}

	}

	/**
	 * Render entities following camera, show debug information if in debug mode
	 * 
	 * @param container
	 * @param g
	 * @throws SlickException
	 */
	public static void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		if (container == null)
			throw new SlickException("no container set");
		if (world == null)
			throw new SlickException("no world set");

		if (scaleX != 1 || scaleY != 1)
			g.scale(scaleX, scaleY);

		// render debug stuff
		int ypos = 1;
		if (debugWindowAtTop)
			ypos = 1;
		else
			ypos = container.getHeight() - 40;
		if (debugEnabled) {
			String text = "Entities: " + world.getEntities().size()
					+ ", rendered Entities: " + world.renderedEntities;
			// int xpos= container.getWidth() - 350;/*130*/
			int xpos = 0;
			showMessage(container, g, xpos, ypos, container.getWidth() - 1, 40,
					20, Color.lightGray, text, 350);
			container.setShowFPS(true);

		} else {
			container.setShowFPS(false);
		}
	}

	public static void showMessage(GameContainer container, Graphics g,
			int xpos, int ypos, int width, int height, int radius, Color c,
			String text, int spaceText) {
		RoundedRectangle r = new RoundedRectangle(xpos, ypos, width, height,
				radius);
		c.a = 0.6f;
		g.setColor(c);
		g.fill(r);
		g.draw(r);
		g.setColor(Color.white);
		g.resetFont();
		g.drawString(text, xpos + spaceText, ypos + 9);
	}

	/**
	 * Set scale factor for graphics
	 * 
	 * @param sx
	 * @param sy
	 */
	public static void scale(float sx, float sy) {
		scaleX = sx;
		scaleY = sy;
	}

	public static void remove(Entity entity) {
		if (world != null) {
			world.remove(entity);
		}
	}

	public static void muteMusic() {
		if (playMusic) {
			ResourceManager.setMusicVolume(1.0f);
			ResourceManager.setSfxVolume(1.0f);
		} else {
			ResourceManager.setMusicVolume(0f);
			ResourceManager.setSfxVolume(0f);
		}
	}

}
