package it.randomtower.engine;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public abstract class Entity implements Comparable<Entity> {

    // identification
    public String name;

    // position
    public float x;
    public float y;

    // movement
    public Vector2f speed;

    // animations
    protected SpriteSheet sheet;
    public Hashtable<String, Animation> animations = new Hashtable<String, Animation>();
    public String currentAnim;
    public int duration = 200;
    public int zLevel = -1;

    // static image (no animation)
    protected Image currentImage;

    // commands
    public Hashtable<String, int[]> commands = new Hashtable<String, int[]>();

    // collisions
    private HashSet<String> type = new HashSet<String>();
    public boolean collidable = true;
    private float xOffset;
    private float yOffset;
    protected int width;
    protected int height;

    /**
     * create a new entity setting initial position
     * 
     * @param x
     * @param y
     */
    public Entity(float x, float y) {
	this.x = x;
	this.y = y;
    }

    /**
     * Update entity animation
     * 
     * @param container
     * @param delta
     * @throws SlickException
     */
    public void update(GameContainer container, int delta)
	    throws SlickException {
	if (animations != null) {
	    if (currentAnim != null) {
		Animation anim = animations.get(currentAnim);
		if (anim != null) {
		    anim.update(delta);
		}
	    }
	}
    }

    /**
     * Render entity
     * 
     * @param container
     * @param g
     * @throws SlickException
     */
    public void render(GameContainer container, Graphics g)
	    throws SlickException {
	if (currentAnim != null) {
	    animations.get(currentAnim).draw(x, y);
	} else if (currentImage != null) {
	    g.drawImage(currentImage, x, y);
	}
	if (ME.debugEnabled) {
	    g.setColor(ME.borderColor);
	    Rectangle hitBox = new Rectangle(x + xOffset, y + yOffset, width,
		    height);
	    g.draw(hitBox);
	    g.setColor(Color.white);
	}
    }

    public void setGraphic(Image image) {
	this.currentImage = image;
    }

    public void setGraphic(SpriteSheet sheet) {
	this.sheet = sheet;
    }

    /**
     * Add animation to entity, first animation added is current animation
     * 
     * @param name
     * @param loop
     * @param row
     * @param frames
     */
    public void add(String name, boolean loop, int row, int... frames) {
	Animation anim = new Animation(false);
	anim.setLooping(loop);
	for (int i = 0; i < frames.length; i++) {
	    anim.addFrame(sheet.getSprite(frames[i], row), duration);
	}
	if (animations.size() == 0) {
	    currentAnim = name;
	}
	animations.put(name, anim);
    }

    /**
     * define commands
     * 
     * @param key
     * @param keys
     */
    public void define(String command, int... keys) {
	commands.put(command, keys);
    }

    /**
     * Check if a command is down
     * 
     * @param key
     * @return
     */
    public boolean check(String command) {
	int[] checked = commands.get(command);
	if (checked == null)
	    return false;
	for (int i = 0; i < checked.length; i++) {
	    if (ME.container.getInput().isKeyDown(checked[i])) {
		return true;
	    }
	}
	return false;
    }

    /**
     * Check if a command is pressed
     * 
     * @param key
     * @return
     */
    public boolean pressed(String command) {
	int[] checked = commands.get(command);
	if (checked == null)
	    return false;
	for (int i = 0; i < checked.length; i++) {
	    if (ME.container.getInput().isKeyPressed(checked[i])) {
		return true;
	    } else if (checked[i] == Input.MOUSE_LEFT_BUTTON
		    || checked[i] == Input.MOUSE_RIGHT_BUTTON) {
		if (ME.container.getInput().isMousePressed(checked[i])) {
		    return true;
		}
	    }
	}
	return false;
    }

    /**
     * Compare to another entity on zLevel
     */
    public int compareTo(Entity o) {
	if (zLevel == o.zLevel)
	    return 0;
	if (zLevel > o.zLevel)
	    return 1;
	return -1;
    }

    /**
     * Set hitbox for collision (by default if and entity have an hitbox, is
     * collidable)
     * 
     * @param xOffset
     * @param yOffset
     * @param width
     * @param height
     */
    public void setHitBox(float xOffset, float yOffset, int width, int height) {
	setHitBox(xOffset, yOffset, width, height, true);
    }

    public void setHitBox(float xOffset, float yOffset, int width, int height,
	    boolean collidable) {
	this.xOffset = xOffset;
	this.yOffset = yOffset;
	this.width = width;
	this.height = height;
	this.collidable = true;
    }

    /**
     * Add collision types to entity
     * 
     * @param types
     * @return
     */
    public boolean addType(String... types) {
	return type.addAll(Arrays.asList(types));
    }

    /**
     * check collision with another entity
     * 
     * @param type
     * @param x
     * @param y
     * @return
     */
    public boolean collide(String type, float x, float y) {
	if (type == null)
	    return false;
	if (type.length() == 0)
	    return false;
	// offset
	for (Entity entity : ME.getEntities()) {
	    if (entity.collidable && entity.type.contains(type)) {
		if (!entity.equals(this)
			&& x + xOffset + width > entity.x + entity.xOffset
			&& y + yOffset + height > entity.y + entity.yOffset
			&& x + xOffset < entity.x + entity.xOffset
				+ entity.width
			&& y + yOffset < entity.y + entity.yOffset
				+ entity.height) {
		    this.collisionResponse(entity);
		    entity.collisionResponse(this);
		    return true;
		}
	    }
	}
	return false;
    }

    /**
     * Response to a collision with another entity
     * 
     * @param entity
     */
    public void collisionResponse(Entity entity) {

    }

}
