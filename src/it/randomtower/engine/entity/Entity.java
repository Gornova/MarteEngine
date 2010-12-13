package it.randomtower.engine.entity;

import it.randomtower.engine.ME;
import it.randomtower.engine.StateManager;
import it.randomtower.engine.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

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

	/** default collidable type SOLID **/
	public static final String SOLID = "Solid";

	/** predefined type for player */
	public static final String PLAYER = "Player";

	/** the world this entity lives in */
	public World world = null;
	
	/** unique identifier **/
	public String name;

	/** x position **/
	public float x;
	/** y position **/
	public float y;

	/** width of the entity. not necessarily the width of the hitbox. Used for world wrapping */
	public int width;
	/** height of the entity. not necessarily the height of the hitbox. Used for world wrapping */
	public int height;
	
	public float previousx, previousy;

	/** start x and y position stored for reseting for example. very helpful */
	public float startx, starty;
	
	public boolean wrapHorizontal = false;
	public boolean wrapVertical = false;
	
	/** speed vector (x,y): specifies x and y movement per update call in pixels **/
	public Vector2f speed = null;
	
	/** angle in degrees from 0 to 360, used for drawing the entity rotated. NOT used for direction! */
	public int angle = 0;
	
	private Hashtable<String, Alarm> alarms = new Hashtable<String, Alarm>();

	/** spritesheet that holds animations **/
	protected SpriteSheet sheet;
	public Hashtable<String, Animation> animations = new Hashtable<String, Animation>();
	public String currentAnim;
	public int duration = 200;
	public int depth = -1;

	/** static image for not-animated entity **/
	public Image currentImage;
	
	/** available commands for entity **/
	public Hashtable<String, int[]> commands = new Hashtable<String, int[]>();

	/** collision type for entity **/
	private HashSet<String> type = new HashSet<String>();

	/** true for collidable entity, false otherwise **/
	public boolean collidable = true;
	/** true if this entity should be visible, false otherwise */
	public boolean visible = true;

	/** x offset for collision box */
	public float xOffset;
	/** y offset for collision box */
	public float yOffset;
	/** hitbox width of entity **/
	public int hitboxWidth;
	/** hitbox height of entity **/
	public int hitboxHeight;

	/** stateManager for entity **/
	public StateManager stateManager;
	
	/**
	 * create a new entity setting initial position (x,y)
	 * 
	 * @param x
	 * @param y
	 */
	public Entity(float x, float y) {
		this.x = x;
		this.y = y;
		this.startx = x;
		this.starty = y;
		stateManager = new StateManager();
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
		if (stateManager!=null && stateManager.currentState()!=null){
			stateManager.update(container, delta);
			return;
		}
		if (animations != null) {
			if (currentAnim != null) {
				Animation anim = animations.get(currentAnim);
				if (anim != null) {
					anim.update(delta);
				}
			}
		}
		if (speed != null) {
			x += speed.x;
			y += speed.y;
		}
		checkWorldBoundaries();
		previousx = x;
		previousy = y;
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
		if (!visible)
			return;
		if (stateManager!=null && stateManager.currentState()!=null){
			stateManager.render(g);
			return;
		}		
		if (currentAnim != null) {
			animations.get(currentAnim).draw(x, y);
		} else if (currentImage != null) {
			currentImage.setRotation(angle);
			g.drawImage(currentImage, x, y);
		}
		if (ME.debugEnabled) {
			g.setColor(ME.borderColor);
			Rectangle hitBox = new Rectangle(x + xOffset, y + yOffset, hitboxWidth, hitboxHeight);
			g.draw(hitBox);
			g.setColor(Color.white);
		}
	}

	/**
	 * Set an image as graphic
	 * @param image
	 */
	public void setGraphic(Image image) {
		this.currentImage = image;
		this.width = image.getWidth();
		this.height = image.getHeight();
	}

	/**
	 * Set a sprite sheet as graphic
	 * @param sheet
	 */
	public void setGraphic(SpriteSheet sheet) {
		this.sheet = sheet;
		this.width = sheet.getSprite(0, 0).getWidth();
		this.height = sheet.getSprite(0, 0).getHeight();
	}

	/**
	 * Add animation to entity, first animation added is current animation
	 * 
	 * @param name
	 * @param loop
	 * @param row
	 * @param frames
	 */
	public void addAnimation(String name, boolean loop, int row, int... frames) {
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
			if (world.container.getInput().isKeyDown(checked[i])) {
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
			if (world.container.getInput().isKeyPressed(checked[i])) {
				return true;
			} else if (checked[i] == Input.MOUSE_LEFT_BUTTON
					|| checked[i] == Input.MOUSE_RIGHT_BUTTON) {
				if (world.container.getInput().isMousePressed(checked[i])) {
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
		if (depth == o.depth)
			return 0;
		if (depth > o.depth)
			return 1;
		return -1;
	}

	/**
	 * Set hitbox for collision (by default if and entity have an hitbox, is
	 * collidable against other entities)
	 * 
	 * @param xOffset
	 * @param yOffset
	 * @param width
	 * @param height
	 */
	public void setHitBox(float xOffset, float yOffset, int width, int height) {
		setHitBox(xOffset, yOffset, width, height, true);
	}

	/**
	 * Set hitbox for collision and set if is collidable against other entities
	 * 
	 * @param xOffset
	 * @param yOffset
	 * @param width
	 * @param height
	 * @param collidable
	 */
	public void setHitBox(float xOffset, float yOffset, int width, int height, boolean collidable) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.hitboxWidth = width;
		this.hitboxHeight = height;
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
	 * check collision with another entity of given type
	 * 
	 * @param type
	 * @param x
	 * @param y
	 * @return
	 */
	public Entity collide(String type, float x, float y) {
		if (type == null || type.isEmpty())
			return null;
		// offset
		for (Entity entity : world.getEntities()) {
			if (entity.collidable && entity.type.contains(type)) {
				if (!entity.equals(this)
						&& x + xOffset + hitboxWidth > entity.x + entity.xOffset
						&& y + yOffset + hitboxHeight > entity.y + entity.yOffset
						&& x + xOffset < entity.x + entity.xOffset
								+ entity.hitboxWidth
						&& y + yOffset < entity.y + entity.yOffset
								+ entity.hitboxHeight) {
					this.collisionResponse(entity);
					entity.collisionResponse(this);
					return entity;
				}
			}
		}
		return null;
	}

	public Entity collide(String[] types, float x, float y) {
		for (String type : types) {
			Entity e = collide(type, x, y);
			if (e != null)
				return e;
		}
		return null;
	}
	
	public Entity collideWith(Entity other, float x, float y) {
		if (other.collidable) {
			if (!other.equals(this)
					&& x + xOffset + hitboxWidth > other.x + other.xOffset
					&& y + yOffset + hitboxHeight > other.y + other.yOffset
					&& x + xOffset < other.x + other.xOffset
							+ other.hitboxWidth
					&& y + yOffset < other.y + other.yOffset
							+ other.hitboxHeight) {
				this.collisionResponse(other);
				other.collisionResponse(this);
				return other;
			}
			return null;
		}
		return null;
	}

	
	public List<Entity> collideInto(String type, float x, float y) {
		if (type == null || type.isEmpty())
			return null;
		ArrayList<Entity> collidingEntities = null;
		for (Entity entity : world.getEntities()) {
			if (entity.collidable && entity.type.contains(type)) {
				if (!entity.equals(this)
						&& x + xOffset + hitboxWidth > entity.x + entity.xOffset
						&& y + yOffset + hitboxHeight > entity.y + entity.yOffset
						&& x + xOffset < entity.x + entity.xOffset
								+ entity.hitboxWidth
						&& y + yOffset < entity.y + entity.yOffset
								+ entity.hitboxHeight) {
					this.collisionResponse(entity);
					entity.collisionResponse(this);
					if (collidingEntities == null)
						collidingEntities = new ArrayList<Entity>();
					collidingEntities.add(entity);
				}
			}
		}
		return collidingEntities;
	}
	
	/*
	 * overload if you want to act on addition to world
	 */
	public void addedToWorld() {
		
	}
	
	/*
	 * overload if you want to act on removal from world
	 */
	public void removedFromWorld() {
		
	}

	/**
	 * Response to a collision with another entity
	 * 
	 * @param other
	 */
	public void collisionResponse(Entity other) {

	}

	/*
	 * overload if you want to act on leaving world boundaries
	 */
	public void leftWorldBoundaries() {
		
	}

	public Image getCurrentImage() {
		return currentImage;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	
	public void checkWorldBoundaries() {
		if ((x + width) < 0) {
			leftWorldBoundaries();
			if (wrapHorizontal) {
				x = this.world.width + 1;
			}
		}
		if (x > this.world.width) {
			leftWorldBoundaries();
			if (wrapHorizontal) {
				x = (-width+1);
			}
		}
		if ((y + height) < 0) {
			leftWorldBoundaries();
			if (wrapVertical) {
				y = this.world.height + 1;
			}
		}
		if (y > this.world.height) {
			leftWorldBoundaries();
			if (wrapVertical) {
				y = (-height+1);
			}
		}
	}
	
	private String getTypes() {
		StringBuffer types = new StringBuffer();
		for (String singleType : type) {
			if (types.length() > 0)
				types.append(", ");
			types.append(singleType);
		}
		return types.toString();
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("name: " + name);
		sb.append(", types: " + getTypes());
		sb.append(", depth: " + depth);
		sb.append(", x: " + this.x);
		sb.append(", y: " + this.y);
		return sb.toString();
	}

	public HashSet<String> getType() {
		return type;
	}
	
	public boolean isType(String type) {
		return type.contains(type);
	}
	
	/**
	 * remove ourselves from world
	 */
	public void destroy() {
		this.world.remove(this);
	}
	
	/***************** some methods to deal with alarms ************************************/
	public void setAlarm(String name, int triggerTime, boolean oneShot, boolean startNow) {
		Alarm alarm = new Alarm(name, triggerTime, oneShot);
		alarms.put(name, alarm);
		if (startNow)
			alarm.start();
	}
	
	public void restartAlarm(String name) {
		Alarm alarm = alarms.get(name);
		if (alarm != null)
			alarm.start();
	}

	public void pauseAlarm(String name) {
		Alarm alarm = alarms.get(name);
		if (alarm != null)
			alarm.pause();
	}

	public void resumeAlarm(String name) {
		Alarm alarm = alarms.get(name);
		if (alarm != null)
			alarm.resume();
	}
	
	public void destroyAlarm(String name) {
		Alarm alarm = alarms.get(name);
		if (alarm != null)
			alarm.setDead(true);
	}
	
	/**
	 * overwrite this method if your entity shall react on alarms that reached their triggerTime
	 * @param name the name of the alarm that triggered right now
	 */
	public void alarmTriggered(String name) {
		// this method needs to be overwritten to deal with alarms
	}

	/**
	 * this method is called automatically by the World and must not be called by your game code.
	 * Don't touch this method ;-)
	 * Consider it private!
	 */
	public void updateAlarms() {
		ArrayList<String> deadAlarms = null;
		Set<String> alarmNames = alarms.keySet();
		if (!alarmNames.isEmpty()) {
			for (String alarmName : alarmNames) {
				Alarm alarm = alarms.get(alarmName);
				if (alarm.isActive()) {
					boolean retval = alarm.update();
					if (retval) {
						alarmTriggered(alarm.getName());
						if (alarm.isOneShotAlaram()) {
							alarm.setActive(false);
						} else {
							alarm.start();
						}
					}
				}
				if (alarm.isDead()) {
					if (deadAlarms == null)
						deadAlarms = new ArrayList<String>();
					deadAlarms.add(alarmName);
				}
			}
			if (deadAlarms != null) {
				for (String deadAlarm : deadAlarms) {
					alarms.put(deadAlarm, null);
				}
			}
		}
	}
}
