package it.randomtower.engine;

import java.util.Hashtable;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.RoundedRectangle;
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
	/** key to activate debug mode **/
	public static int keyToggleDebug = -1;
	/** default border color of hitbox in debug mode **/
	public static Color borderColor = Color.red;
	/** key for restarting game **/
	public static int keyRestart = -1;
	

	/** game container **/
	public static GameContainer container;

	/** x scale factor for graphics, default 1 (nothing) **/
	public static float scaleX = 1;
	/** y scale factor for graphics, default 1 (nothing) **/
	public static float scaleY = 1;
	/** current camera **/
	public static Camera camera;

	/** default collidable type SOLID **/
	public static final String SOLID = "solid";

	/** top z order **/
	public static final Integer Z_LEVEL_TOP = 100;

	public static final String WALK_LEFT = "walk_Left";
	public static final String WALK_RIGHT = "walk_Right";
	public static final String WALK_UP = "walk_Up";
	public static final String WALK_DOWN = "walk_Down";

	/** utility hashtable for game attributes **/
	public static Hashtable<String, Object> attributes = new Hashtable<String, Object>();

	public static World world;
	
	/** 
	 * Update game camera, entities and add new entities and remove old entities
	 * @param container
	 * @param delta
	 * @throws SlickException
	 */
	public static void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
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
				//TODO: go to first state?
			}
		}	

		// update camera
		if (camera != null) {
			camera.update(container, delta);
		}
	}

	/**
	 * Render entities following camera, show debug information if in debug mode
	 * @param container
	 * @param g
	 * @throws SlickException
	 */
	public static void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		if (container == null)
			throw new SlickException("no container set");
		if (world == null)
			throw new SlickException("no world set");		
		
		if (scaleX != 1 || scaleY != 1)
			g.scale(scaleX, scaleY);
		// center to camera position
		if (camera != null)
			g.translate(camera.x, camera.y);

		if (camera != null)
			g.translate(-camera.x, -camera.y);

		// render debug stuff
		if (debugEnabled) {
			RoundedRectangle r = new RoundedRectangle(1, 1,
					container.getWidth() - 1, 40, 20);
			Color c = Color.lightGray;
			c.a = 0.3f;
			g.setColor(c);
			g.fill(r);
			g.draw(r);
			g.setColor(Color.white);
			g.drawString("Entities: " + world.getEntities().size(),
					container.getWidth() - 110, 10);
			container.setShowFPS(true);

		} else {
			container.setShowFPS(false);
		}
	}

	/**
	 * Set scale factor for graphics
	 * @param sx
	 * @param sy
	 */
	public static void scale(float sx, float sy) {
		scaleX = sx;
		scaleY = sy;
	}

	/**
	 * Set camera 
	 * @param camera
	 */
	public static void setCamera(Camera camera) {
		ME.camera = camera;
	}

	public static void remove(Entity entity) {
		if (world!=null){
			world.remove(entity);
		}
		
	}

}
