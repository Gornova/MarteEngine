package it.randomtower.engine;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.RoundedRectangle;

/**
 * Marte Engine Utility class
 * 
 * @author RandomTower
 * @project MarteEngine
 */
public class ME {

    public static boolean debugEnabled = false;
    public static int keyToggleDebug = -1;

    public static GameContainer container;

    private static final List<Entity> entities = new ArrayList<Entity>();
    private static final List<Entity> removeable = new ArrayList<Entity>();
    private static final List<Entity> addable = new ArrayList<Entity>();

    public static float scaleX = 1;
    public static float scaleY = 1;
    public static Camera camera;

    public static final String SOLID = "solid";

    public static final Integer Z_LEVEL_TOP = 100;
    
    public static final String WALK_LEFT = "walk_Left";
    public static final String WALK_RIGHT = "walk_Right";
    public static final String WALK_UP = "walk_Up";
    public static final String WALK_DOWN = "walk_Down";
    
    public static Hashtable<String, Object> attributes = new Hashtable<String, Object>();
    public static Color borderColor = Color.red;
    

    public static void add(Entity e) {
	// sort in z order
	if (entities.size() > 1) {
	    Collections.sort(entities);
	}
	addable.add(e);
    }

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

    public static void render(GameContainer container, Graphics g)
	    throws SlickException {
	if (scaleX != 1 || scaleY != 1)
	    g.scale(scaleX, scaleY);
	// center to camera position
	if (camera != null)
	    g.translate(camera.x, camera.y);

	// render entities
	for (Entity e : entities) {
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

    public static List<Entity> getEntities() {
	return entities;
    }

    public static boolean remove(Entity entity) {
	if (!removeable.contains(entity)) {
	    return removeable.add(entity);
	}
	return false;
    }

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

    public static void clear() {
	entities.clear();
    }

    public static void scale(float sx, float sy) {
	scaleX = sx;
	scaleY = sy;
    }

    public static void setCamera(Camera camera) {
	ME.camera = camera;
	// ME.add(ME.camera);
    }

}
