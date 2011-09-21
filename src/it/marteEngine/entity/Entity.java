package it.marteEngine.entity;

import it.marteEngine.ME;
import it.marteEngine.StateManager;
import it.marteEngine.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
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
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

//TODO modify hitbox coordinates to a real shape without changing method interface.
//TODO a shape can be rotated and scaled when the entity is rotated and scaled.
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

	/** x,y is the center of the image/animation, otherwise it's top left corner */
	public boolean centered = false;

	/**
	 * width of the entity. not necessarily the width of the hitbox. Used for
	 * world wrapping
	 */
	public int width;
	/**
	 * height of the entity. not necessarily the height of the hitbox. Used for
	 * world wrapping
	 */
	public int height;

	public float previousx, previousy;

	/** start x and y position stored for reseting for example. very helpful */
	public float startx, starty;

	public boolean wrapHorizontal = false;
	public boolean wrapVertical = false;

	/** speed vector (x,y): specifies x and y movement per update call in pixels **/
	public Vector2f speed = new Vector2f(0, 0);

	/**
	 * angle in degrees from 0 to 360, used for drawing the entity rotated. NOT
	 * used for direction!
	 */
	protected int angle = 0;

	/** scale used for both horizontal and vertical scaling. */
	public float scale = 1.0f;

	/**
	 * color of the entity, mainly used for alpha transparency, but could also
	 * be used for tinting
	 */
	private Color color = new Color(Color.white);

	private Hashtable<String, Alarm> alarms = new Hashtable<String, Alarm>();
	private Hashtable<String, Alarm> addableAlarms = new Hashtable<String, Alarm>();

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

	/**
	 * true for active entities, false otherwise. for active entities update()
	 * is called
	 */
	public boolean active = true;
	/** true for collidable entity, false otherwise **/
	public boolean collidable = true;
	/** true if this entity should be visible, false otherwise */
	public boolean visible = true;

	/** x offset for collision box */
	public float hitboxOffsetX;
	/** y offset for collision box */
	public float hitboxOffsetY;
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
	 * Create a new entity setting initial position (x,y) and static image
	 * 
	 * @param x
	 * @param y
	 * @param image
	 */
	public Entity(float x, float y, Image image) {
		this(x, y);
		setGraphic(image);
	}

	/**
	 * Set if image or animation must be centered on position
	 * 
	 * @param on
	 */
	public void setCentered(boolean on) {
		int whalf = 0, hhalf = 0;
		if (currentImage != null) {
			whalf = currentImage.getWidth() / 2;
			hhalf = currentImage.getHeight() / 2;
		}
		if (currentAnim != null) {
			whalf = animations.get(currentAnim).getWidth() / 2;
			hhalf = animations.get(currentAnim).getHeight() / 2;
		}
		if (on) {
			// modify hitbox position accordingly - move it a bit up and left
			this.hitboxOffsetX -= whalf;
			this.hitboxOffsetY -= hhalf;
			this.centered = true;
		} else {
			if (centered == true) {
				// reset hitbox position to top left origin
				this.hitboxOffsetX += whalf;
				this.hitboxOffsetY += hhalf;
			}
			this.centered = false;
		}
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
		previousx = x;
		previousy = y;
		if (stateManager != null && stateManager.currentState() != null) {
			stateManager.update(container, delta);
			return;
		}
		updateAnimation(delta);
		if (speed != null) {
			x += speed.x;
			y += speed.y;
		}
		checkWorldBoundaries();
		previousx = x;
		previousy = y;
	}

	protected void updateAnimation(int delta) {
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
		if (stateManager != null && stateManager.currentState() != null) {
			stateManager.render(g);
			return;
		}
		float xpos = x, ypos = y;
		if (currentAnim != null) {
			Animation anim = animations.get(currentAnim);
			int w = anim.getWidth();
			int h = anim.getHeight();
			int whalf = w / 2;
			int hhalf = h / 2;
			if (centered) {
				xpos = x - (whalf * scale);
				ypos = y - (hhalf * scale);
			}
			if (angle != 0) {
				g.rotate(x, y, angle);
			}
			anim.draw(xpos, ypos, w * scale, h * scale, color);
			if (angle != 0)
				g.resetTransform();
		} else if (currentImage != null) {
			currentImage.setAlpha(color.a);
			int w = currentImage.getWidth() / 2;
			int h = currentImage.getHeight() / 2;
			if (centered) {
				xpos -= w;
				ypos -= h;
				currentImage.setCenterOfRotation(w, h);
			} else
				currentImage.setCenterOfRotation(0, 0);

			if (angle != 0) {
				currentImage.setRotation(angle);
			}
			if (scale != 1.0f) {
				if (centered)
					g.translate(xpos - (w * scale - w), ypos - (h * scale - h));
				else
					g.translate(xpos, ypos);
				g.scale(scale, scale);
				g.drawImage(currentImage, 0, 0);
			} else
				g.drawImage(currentImage, xpos, ypos);
			if (scale != 1.0f)
				g.resetTransform();
		}
		if (ME.debugEnabled && collidable) {
			g.setColor(ME.borderColor);
			Rectangle hitBox = new Rectangle(x + hitboxOffsetX, y
					+ hitboxOffsetY, hitboxWidth, hitboxHeight);
			g.draw(hitBox);
			g.setColor(Color.white);
			g.drawRect(x, y, 1, 1);
			// draw entity center
			if (width != 0 && height != 0) {
				float centerX = x + width / 2;
				float centerY = y + height / 2;
				g.setColor(Color.green);
				g.drawRect(centerX, centerY, 1, 1);
				g.setColor(Color.white);
			}
		}
	}

	/**
	 * Set an image as graphic
	 * 
	 * @param image
	 */
	public void setGraphic(Image image) {
		this.currentImage = image;
		this.width = image.getWidth();
		this.height = image.getHeight();
	}

	/**
	 * Set a sprite sheet as graphic
	 * 
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

	public Animation addAnimation(SpriteSheet sheet, String name, boolean loop,
			int row, int... frames) {
		Animation anim = new Animation(false);
		anim.setLooping(loop);
		for (int i = 0; i < frames.length; i++) {
			anim.addFrame(sheet.getSprite(frames[i], row), duration);
		}
		if (animations.size() == 0) {
			currentAnim = name;
		}
		animations.put(name, anim);
		return anim;
	}

	/**
	 * Add animation to entity, first animation added is current animation. Can
	 * Flip the frames horizontally o vertically
	 * 
	 * @Param name
	 * @Param loop
	 * @Param fliphorizontal
	 * @Param flipvertical
	 * @Param row
	 * @Param frames
	 * 
	 * @author Sabathorn
	 */
	public void addFlippedAnimation(String name, boolean loop,
			boolean fliphorizontal, boolean flipvertical, int row,
			int... frames) {
		Animation anim = new Animation(false);
		anim.setLooping(loop);
		for (int i = 0; i < frames.length; i++) {
			anim.addFrame(
					sheet.getSprite(frames[i], row).getFlippedCopy(
							fliphorizontal, flipvertical), duration);
		}
		if (animations.size() == 0) {
			currentAnim = name;
		}
		animations.put(name, anim);
	}

	/**
	 * define commands to handle inputs
	 * 
	 * @param command
	 *            name of the command
	 * @param keys
	 *            keys or mouse input from {@link Input} class
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
			} else if (checked[i] < 10) {
				/**
				 * 10 is max number of button on a mouse
				 * 
				 * @see Input
				 */
				if (world.container.getInput().isMousePressed(checked[i])) {
					return true;
				}
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
	public void setHitBox(float xOffset, float yOffset, int width, int height,
			boolean collidable) {
		this.hitboxOffsetX = xOffset;
		this.hitboxOffsetY = yOffset;
		this.hitboxWidth = width;
		this.hitboxHeight = height;
		this.collidable = true;

		this.width = width;
		this.height = height;
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
	 * Reset type information for this entity
	 */
	public void clearTypes() {
		type.clear();
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
			if (entity.collidable && entity.isType(type)) {
				if (!entity.equals(this)
						&& x + hitboxOffsetX + hitboxWidth > entity.x
								+ entity.hitboxOffsetX
						&& y + hitboxOffsetY + hitboxHeight > entity.y
								+ entity.hitboxOffsetY
						&& x + hitboxOffsetX < entity.x + entity.hitboxOffsetX
								+ entity.hitboxWidth
						&& y + hitboxOffsetY < entity.y + entity.hitboxOffsetY
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
					&& x + hitboxOffsetX + hitboxWidth > other.x
							+ other.hitboxOffsetX
					&& y + hitboxOffsetY + hitboxHeight > other.y
							+ other.hitboxOffsetY
					&& x + hitboxOffsetX < other.x + other.hitboxOffsetX
							+ other.hitboxWidth
					&& y + hitboxOffsetY < other.y + other.hitboxOffsetY
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
			if (entity.collidable && entity.isType(type)) {
				if (!entity.equals(this)
						&& x + hitboxOffsetX + hitboxWidth > entity.x
								+ entity.hitboxOffsetX
						&& y + hitboxOffsetY + hitboxHeight > entity.y
								+ entity.hitboxOffsetY
						&& x + hitboxOffsetX < entity.x + entity.hitboxOffsetX
								+ entity.hitboxWidth
						&& y + hitboxOffsetY < entity.y + entity.hitboxOffsetY
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

	/**
	 * Checks if this Entity collides with the specified point.
	 * 
	 * @param x
	 *            The x-position of the point.
	 * @param y
	 *            The y-position of the point.
	 */
	public boolean collidePoint(float x, float y) {
		if (x >= this.x - hitboxOffsetX && y >= this.y - hitboxOffsetY
				&& x < this.x - hitboxOffsetX + width
				&& y < this.y - hitboxOffsetY + height) {
			this.collisionResponse(null);
			return true;
		}
		return false;
	}

	/**
	 * overload if you want to act on addition to world
	 */
	public void addedToWorld() {

	}

	/**
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

	/**
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
				x = (-width + 1);
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
				y = (-height + 1);
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
		return this.type.contains(type);
	}

	/**
	 * remove ourselves from world
	 */
	public void destroy() {
		this.world.remove(this);
		this.visible = false;
	}

	/***************** some methods to deal with angles and vectors ************************************/

	public int getAngleToPosition(Vector2f otherPos) {
		Vector2f diff = otherPos.sub(new Vector2f(x, y));
		return (((int) diff.getTheta()) + 90) % 360;
	}

	public int getAngleDiff(int angle1, int angle2) {
		return ((((angle2 - angle1) % 360) + 540) % 360) - 180;
	}

	public Vector2f getPointWithAngleAndDistance(int angle, float distance) {
		Vector2f point;
		float tx, ty;
		double theta = StrictMath.toRadians(angle + 90);
		tx = (float) (this.x + distance * StrictMath.cos(theta));
		ty = (float) (this.y + distance * StrictMath.sin(theta));
		point = new Vector2f(tx, ty);
		return point;
	}

	public float getDistance(Entity other) {
		return getDistance(new Vector2f(other.x, other.y));
	}

	public float getDistance(Vector2f otherPos) {
		Vector2f myPos = new Vector2f(x, y);
		return myPos.distance(otherPos);
	}

	/**
	 * Calculate vector from angle and magnitude
	 * 
	 * @param angle
	 * @param magnitude
	 * @return
	 * @author Alex Schearer
	 */
	public static Vector2f calculateVector(float angle, float magnitude) {
		Vector2f v = new Vector2f();
		v.x = (float) Math.sin(Math.toRadians(angle));
		v.x *= magnitude;
		v.y = (float) -Math.cos(Math.toRadians(angle));
		v.y *= magnitude;
		return v;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param x1
	 * @param y1
	 * @return
	 * @author Alex Schearer
	 */
	public static float calculateAngle(float x, float y, float x1, float y1) {
		double angle = Math.atan2(y - y1, x - x1);
		return (float) (Math.toDegrees(angle) - 90);
	}

	/***************** some methods to deal with alarms ************************************/

	/**
	 * Create an Alarm with given parameters and add to current Entity
	 */
	public Alarm setAlarm(String name, int triggerTime, boolean oneShot,
			boolean startNow) {
		Alarm alarm = new Alarm(name, triggerTime, oneShot);

		if (startNow)
			alarm.start();
		addableAlarms.put(name, alarm);
		return alarm;
	}

	public boolean restartAlarm(String name) {
		Alarm alarm = alarms.get(name);
		if (alarm != null) {
			alarm.start();
			return true;
		}
		return false;
	}

	public boolean pauseAlarm(String name) {
		Alarm alarm = alarms.get(name);
		if (alarm != null) {
			alarm.pause();
			return true;
		}
		return false;

	}

	public boolean resumeAlarm(String name) {
		Alarm alarm = alarms.get(name);
		if (alarm != null) {
			alarm.resume();
			return true;
		}
		return false;
	}

	public boolean destroyAlarm(String name) {
		Alarm alarm = alarms.get(name);
		if (alarm != null) {
			alarm.setDead(true);
			return true;
		}
		return false;
	}

	public boolean existAlarm(String name) {
		return alarms.get(name) == null ? false : true;
	}

	/**
	 * overwrite this method if your entity shall react on alarms that reached
	 * their triggerTime
	 * 
	 * @param name
	 *            the name of the alarm that triggered right now
	 */
	public void alarmTriggered(String name) {
		// this method needs to be overwritten to deal with alarms
	}

	/**
	 * this method is called automatically by the World and must not be called
	 * by your game code. Don't touch this method ;-) Consider it private!
	 */
	public void updateAlarms(int delta) {
		ArrayList<String> deadAlarms = null;
		Set<String> alarmNames = alarms.keySet();
		if (!alarmNames.isEmpty()) {
			for (String alarmName : alarmNames) {
				Alarm alarm = alarms.get(alarmName);
				if (alarm.isActive()) {
					boolean retval = alarm.update(delta);
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
					alarms.remove(deadAlarm);
				}
			}
		}
		if (addableAlarms != null && !addableAlarms.isEmpty()) {

			Iterator<String> itr = addableAlarms.keySet().iterator();
			while (itr.hasNext()) {
				String name = itr.next();
				alarms.put(name, addableAlarms.get(name));
			}
		}
		if (!addableAlarms.isEmpty()) {
			addableAlarms.clear();
		}
	}

	public int getAngle() {
		return angle;
	}

	// TODO: add proper rotation for the hitbox/shape here!!!
	public void setAngle(int angle) {
		this.angle = angle;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public float getAlpha() {
		return color.a;
	}

	public void setAlpha(float alpha) {
		if (alpha >= 0.0f && alpha <= 1.0f)
			color.a = alpha;
	}

	public void setPosition(Vector2f pos) {
		if (pos != null) {
			this.x = pos.x;
			this.y = pos.y;
		}
	}

	public String toCsv() {
		return "" + (int) x + "," + (int) y + "," + name + ","
				+ type.iterator().next();
	}

	/**
	 * @param shape
	 *            to check
	 * @return entity that intersect with theri hitboxes given shape
	 */
	public List<Entity> intersect(Shape shape) {
		if (shape == null)
			return null;
		List<Entity> result = new ArrayList<Entity>();
		for (Entity entity : world.getEntities()) {
			if (entity.collidable && !entity.equals(this)) {
				Rectangle rec = new Rectangle(entity.x, entity.y, entity.width,
						entity.height);
				if (shape.intersects(rec)) {
					result.add(entity);
				}
			}
		}
		return result;
	}

}
