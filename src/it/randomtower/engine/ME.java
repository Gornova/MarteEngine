package it.randomtower.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.RoundedRectangle;

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

	/** game container **/
	public static GameContainer container;

	private static final List<Entity> entities = new ArrayList<Entity>();
	private static final List<Entity> removeable = new ArrayList<Entity>();
	private static final List<Entity> addable = new ArrayList<Entity>();

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

	/**
	 * Add entity to game and sort entity in z order
	 * @param e entity to add
	 */
	public static void add(Entity e) {
		// sort in z order
		if (entities.size() > 1) {
			Collections.sort(entities);
		}
		addable.add(e);
	}

	/** 
	 * Update game camera, entities and add new entities and remove old entities
	 * @param container
	 * @param delta
	 * @throws SlickException
	 */
	public static void update(GameContainer container, int delta)
			throws SlickException {
		if (container == null)
			throw new SlickException("no container set");
		removeable.clear();

		// special key handling
		if (keyToggleDebug != -1) {
			if (container.getInput().isKeyPressed(keyToggleDebug)) {
				debugEnabled = debugEnabled ? false : true;
			}
		}

		// update camera
		if (camera != null) {
			camera.update(container, delta);
		}

		// add new entities
		for (Entity entity : addable) {
			entities.add(entity);
		}
		addable.clear();

		// update entities
		for (Entity e : entities) {
			e.update(container, delta);
		}
		// remove signed entities
		for (Entity entity : removeable) {
			entities.remove(entity);
		}
	}

	/**
	 * Render entities following camera, show debug information if in debug mode
	 * @param container
	 * @param g
	 * @throws SlickException
	 */
	public static void render(GameContainer container, Graphics g)
			throws SlickException {
		if (scaleX != 1 || scaleY != 1)
			g.scale(scaleX, scaleY);
		// center to camera position
		if (camera != null)
			g.translate(camera.x, camera.y);

		// render entities
		for (Entity e : entities) {
			if (ME.debugEnabled) {
				g.setColor(ME.borderColor);
				Rectangle hitBox = new Rectangle(e.x + e.xOffset, e.y + e.yOffset, e.width,
						e.height);
				g.draw(hitBox);
				g.setColor(Color.white);
			}			
			if (camera != null) {
				// TODO
				// if (camera.contains(e)) {
				e.render(container, g);
				// }
			} else {
				e.render(container, g);
			}
		}

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
			g.drawString("Entities: " + entities.size(),
					container.getWidth() - 110, 10);
			container.setShowFPS(true);

		} else {
			container.setShowFPS(false);
		}
	}

	/**
	 * @return List of entities currently in game
	 */
	public static List<Entity> getEntities() {
		return entities;
	}

	/**
	 * @param entity to remove from game
	 * @return false if entity is already set to be remove
	 */
	public static boolean remove(Entity entity) {
		if (!removeable.contains(entity)) {
			return removeable.add(entity);
		}
		return false;
	}

	/**
	 * @param name
	 * @return null if name is null or if no entity is found in game, entity otherwise
	 */
	public static Entity find(String name) {
		if (name == null)
			return null;
		for (Entity entity : entities) {
			if (entity.name.equalsIgnoreCase(name)) {
				return entity;
			}
		}
		return null;
	}

	/**
	 * Remove all entities
	 */
	public static void clear() {
		entities.clear();
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

}
