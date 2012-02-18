package it.marteEngine.entity;

import it.marteEngine.ME;
import it.marteEngine.StateManager;
import it.marteEngine.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

//TODO modify hitbox coordinates to a real shape without changing method interface.
//TODO a shape can be rotated and scaled when the entity is rotated and scaled.
public abstract class Entity implements Comparable<Entity> {

	/** default collidable type SOLID */
	public static final String SOLID = "Solid";

	/** predefined type for player */
	public static final String PLAYER = "Player";

	/** the world this entity lives in */
	public World world = null;

	/** unique identifier */
	public String name;

	/** x position */
	public float x;
	/** y position */
	public float y;

	/**
	 * If this entity is centered the x,y position is in the center. otherwise
	 * the x,y position is the top left corner.
	 */
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

	/** speed vector (x,y): specifies x and y movement per update call in pixels */
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

	private AlarmContainer alarms;

	protected SpriteSheet sheet;
	private Map<String, Animation> animations = new HashMap<String, Animation>();
	private String currentAnim;
	public int duration = 200;
	public int depth = -1;

	/** static image for non-animated entity */
	public Image currentImage;

	public InputManager input;

	/** The types this entity can collide with */
	private HashSet<String> collisionTypes = new HashSet<String>();

	/** true if this entity can receive updates */
	public boolean active = true;
	public boolean collidable = true;
	public boolean visible = true;

	public float hitboxOffsetX;
	public float hitboxOffsetY;
	public int hitboxWidth;
	public int hitboxHeight;

	public StateManager stateManager;
	private boolean leftTheWorld;

	/**
	 * Create a new entity positioned at the (x,y) coordinates.
	 */
	public Entity(float x, float y) {
		this.x = x;
		this.y = y;
		this.startx = x;
		this.starty = y;
		stateManager = new StateManager();
		alarms = new AlarmContainer(this);
		input = new InputManager();
	}

	/**
	 * Create a new entity positioned at the (x,y) coordinates. Displayed as an
	 * image.
	 */
	public Entity(float x, float y, Image image) {
		this(x, y);
		setGraphic(image);
	}

	/**
	 * Set if the image or animation must be centered
	 */
	public void setCentered(boolean center) {
		int whalf = 0, hhalf = 0;
		if (currentImage != null) {
			whalf = currentImage.getWidth() / 2;
			hhalf = currentImage.getHeight() / 2;
		}
		if (currentAnim != null) {
			whalf = animations.get(currentAnim).getWidth() / 2;
			hhalf = animations.get(currentAnim).getHeight() / 2;
		}
		if (center) {
			// modify hitbox position accordingly - move it a bit up and left
			this.hitboxOffsetX -= whalf;
			this.hitboxOffsetY -= hhalf;
			this.centered = true;
		} else {
			if (centered) {
				// reset hitbox position to top left origin
				this.hitboxOffsetX += whalf;
				this.hitboxOffsetY += hhalf;
			}
			this.centered = false;
		}
	}

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
	 */
	public void setGraphic(Image image) {
		this.currentImage = image;
		this.width = image.getWidth();
		this.height = image.getHeight();
	}

	/**
	 * Set a sprite sheet as graphic
	 */
	public void setGraphic(SpriteSheet sheet) {
		this.sheet = sheet;
		this.width = sheet.getSprite(0, 0).getWidth();
		this.height = sheet.getSprite(0, 0).getHeight();
	}

	public void addAnimation(String animName, boolean loop, int row,
			int... frames) {
		Animation anim = new Animation(false);
		anim.setLooping(loop);
		for (int frame : frames) {
			anim.addFrame(sheet.getSprite(frame, row), duration);
		}
		addAnimation(animName, anim);
	}

	public Animation addAnimation(SpriteSheet sheet, String animName,
			boolean loop, int row, int... frames) {
		Animation anim = new Animation(false);
		anim.setLooping(loop);
		for (int frame : frames) {
			anim.addFrame(sheet.getSprite(frame, row), duration);
		}
		addAnimation(animName, anim);
		return anim;
	}

	/**
	 * Add animation to entity. The frames can be flipped horizontally and/or
	 * vertically.
	 */
	public void addFlippedAnimation(String animName, boolean loop,
			boolean fliphorizontal, boolean flipvertical, int row,
			int... frames) {
		Animation anim = new Animation(false);
		anim.setLooping(loop);
		for (int frame : frames) {
			anim.addFrame(
					sheet.getSprite(frame, row).getFlippedCopy(fliphorizontal,
							flipvertical), duration);
		}
		addAnimation(animName, anim);
	}

	/**
	 * Add an animation.The first animation added is set as the current
	 * animation.
	 */
	public void addAnimation(String animName, Animation animation) {
		boolean firstAnim = animations.isEmpty();
		animations.put(animName, animation);

		if (firstAnim) {
			setAnim(animName);
		}
	}

	/**
	 * Start playing the animation stored as animName.
	 *
	 * @param animName
	 *            The name of the animation to play
	 * @throws IllegalArgumentException
	 *             If there is no animation stored as animName
	 * @see #addAnimation(String, org.newdawn.slick.Animation)
	 */
	public void setAnim(String animName) {
		if (!animations.containsKey(animName)) {
			throw new IllegalArgumentException("No animation for " + animName);
		}
		currentAnim = animName;
		Animation currentAnimation = animations.get(currentAnim);
		width = currentAnimation.getWidth();
		height = currentAnimation.getHeight();
	}

	/**
	 * @see #bindToKey(String, int...)
	 */
	public void define(String command, int... keys) {
		bindToKey(command, keys);
	}

	/**
	 * @see InputManager#bindToKey(String, int...)
	 */
	public void bindToKey(String command, int... keys) {
		input.bindToKey(command, keys);
	}

	/**
	 * @see InputManager#bindToMouse(String, int...)
	 */
	public void bindToMouse(String command, int... buttons) {
		input.bindToMouse(command, buttons);
	}

	/**
	 * @see InputManager#isDown(String)
	 */
	public boolean check(String command) {
		return input.isDown(command);
	}

	/**
	 * @see InputManager#isPressed(String)
	 */
	public boolean pressed(String command) {
		return input.isPressed(command);
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
	 * Set the hitbox used for collision detection. If an entity has an hitbox,
	 * it is collidable against other entities.
	 *
	 * @param xOffset
	 *            The offset of the hitbox on the x axis. Relative to the top
	 *            left point of the entity.
	 * @param yOffset
	 *            The offset of the hitbox on the y axis. Relative to the top
	 *            left point of the entity.
	 * @param width
	 *            The width of the rectangle in pixels
	 * @param height
	 *            The height of the rectangle in pixels
	 */
	public void setHitBox(float xOffset, float yOffset, int width, int height) {
		this.hitboxOffsetX = xOffset;
		this.hitboxOffsetY = yOffset;
		this.hitboxWidth = width;
		this.hitboxHeight = height;
		this.width = width;
		this.height = height;
		this.collidable = true;
	}

	/**
	 * Add a type that this entity can collide with. To allow collision with
	 * other entities add at least 1 type. For example in a space invaders game.
	 * To allow a ship to collide with a bullet and a monster:
	 * ship.addType("bullet", "monster")
	 *
	 * @param types
	 *            The types that this entity can collide with.
	 */
	public boolean addType(String... types) {
		return collisionTypes.addAll(Arrays.asList(types));
	}

	/**
	 * Reset the types that this entity can collide with
	 */
	public void clearTypes() {
		collisionTypes.clear();
	}

	/**
	 * Check for a collision with another entity of the given entity type. Two
	 * entities collide when the hitbox of this entity intersects with the
	 * hitbox of another entity.
	 * <p/>
	 * The hitbox starts at the provided x,y coordinates. The size and offset of
	 * the hitbox is set in the {@link #setHitBox(float, float, int, int)}
	 * method.
	 * <p/>
	 * If a collision occurred then both the entities are notified of the
	 * collision by the {@link #collisionResponse(Entity)} method.
	 *
	 * @param type
	 *            The type of another entity to check for collision.
	 * @param x
	 *            The x coordinate where the the collision check needs to be
	 *            done.
	 * @param y
	 *            The y coordinate where the the collision check needs to be
	 *            done.
	 * @return The first entity that is colliding with this entity at the x,y
	 *         coordinates, or NULL if there is no collision.
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

	/**
	 * Checks for collision against multiple types.
	 *
	 * @see #collide(String, float, float)
	 */
	public Entity collide(String[] types, float x, float y) {
		for (String type : types) {
			Entity e = collide(type, x, y);
			if (e != null)
				return e;
		}
		return null;
	}

	/**
	 * Checks if this Entity collides with a specific Entity.
	 *
	 * @param other
	 *            The Entity to check for collision
	 * @param x
	 *            The x coordinate where the the collision check needs to be
	 *            done.
	 * @param y
	 *            The y coordinate where the the collision check needs to be
	 *            done.
	 * @return The entity that is colliding with the other entity at the x,y
	 *         coordinates, or NULL if there is no collision.
	 */
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
	 * Checks if this Entity contains the specified point. The
	 * {@link #collisionResponse(Entity)} is called to notify this entity of the
	 * collision.
	 *
	 * @param x
	 *            The x coordinate of the point to check
	 * @param y
	 *            The y coordinate of the point to check
	 * @return If this entity contains the specified point
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
	 * overload if you want to act on addition to the world
	 */
	public void addedToWorld() {

	}

	/**
	 * overload if you want to act on removal from the world
	 */
	public void removedFromWorld() {

	}

	/**
	 * Response to a collision with another entity
	 *
	 * @param other
	 *            The other entity that collided with us.
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
		input.setInput(world.container.getInput());
	}

	/**
	 * Check if this entity has left the world.
	 * 
	 * If the entity has moved outside of the world then the entity is notified by the
	 * {@link #leftWorldBoundaries()} method. If the entity must be wrapped, make it
	 * reappear on the opposite side of the world.
	 */
	public void checkWorldBoundaries() {
		if (world.contains(this)) {
			leftTheWorld = false;
		} else {
			if (!leftTheWorld) {
				leftWorldBoundaries();
				leftTheWorld = true;
			}
			wrapEntity();
		}
	}

	private void wrapEntity() {
		if (x + width < 0) {
			if (wrapHorizontal) {
				x = world.width - 1;
			}
		}
		if (x > world.width) {
			if (wrapHorizontal) {
				x = (-width + 1);
			}
		}
		if (y + height < 0) {
			if (wrapVertical) {
				y = world.height - 1;
			}
		}
		if (y > world.height) {
			if (wrapVertical) {
				y = (-height + 1);
			}
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("name: ").append(name);
		sb.append(", types: ").append(collisionTypesToString());
		sb.append(", depth: ").append(depth);
		sb.append(", x: ").append(x);
		sb.append(", y: ").append(y);
		return sb.toString();
	}

	public String[] getCollisionTypes() {
		return collisionTypes.toArray(new String[collisionTypes.size()]);
	}

	public boolean isType(String type) {
		return collisionTypes.contains(type);
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

	public static Vector2f calculateVector(float angle, float magnitude) {
		Vector2f v = new Vector2f();
		v.x = (float) Math.sin(Math.toRadians(angle));
		v.x *= magnitude;
		v.y = (float) -Math.cos(Math.toRadians(angle));
		v.y *= magnitude;
		return v;
	}

	public static float calculateAngle(float x, float y, float x1, float y1) {
		double angle = Math.atan2(y - y1, x - x1);
		return (float) (Math.toDegrees(angle) - 90);
	}

	/***************** some methods to deal with alarms ************************************/

	/**
	 * Add an alarm with the given parameters and add it to this Entity
	 */
	public void addAlarm(String alarmName, int triggerTime, boolean oneShot) {
		addAlarm(alarmName, triggerTime, oneShot, true);
	}

	/**
	 * Add an alarm with given parameters and add it to this Entity
	 */
	public void addAlarm(String alarmName, int triggerTime, boolean oneShot,
			boolean startNow) {
		Alarm alarm = new Alarm(alarmName, triggerTime, oneShot);
		alarms.addAlarm(alarm, startNow);
	}

	public boolean restartAlarm(String alarmName) {
		return alarms.restartAlarm(alarmName);
	}

	public boolean pauseAlarm(String alarmName) {
		return alarms.pauseAlarm(alarmName);
	}

	public boolean resumeAlarm(String alarmName) {
		return alarms.resumeAlarm(alarmName);
	}

	public boolean destroyAlarm(String alarmName) {
		return alarms.destroyAlarm(alarmName);
	}

	public boolean hasAlarm(String alarmName) {
		return alarms.hasAlarm(alarmName);
	}

	/**
	 * Overwrite this method if your entity shall react on alarms that reached
	 * their triggerTime.
	 *
	 * @param alarmName
	 *            the name of the alarm that triggered right now
	 */
	public void alarmTriggered(String alarmName) {
		// this method needs to be overwritten to deal with alarms
	}

	/**
	 * this method is called automatically by the World and must not be called
	 * by your game code. Don't touch this method ;-) Consider it private!
	 */
	public void updateAlarms(int delta) {
		alarms.update(delta);
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

	public boolean isCurrentAnim(String animName) {
		return currentAnim.equals(animName);
	}

	public String toCsv() {
		return "" + (int) x + "," + (int) y + "," + name + ","
				+ collisionTypesToString();
	}

	private String collisionTypesToString() {
		StringBuffer sb = new StringBuffer();
		for (String type : collisionTypes) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(type);
		}
		return sb.toString();
	}

	/**
	 * @param shape
	 *            the shape to check for intersection
	 * @return The entities that intersect with their hitboxes into the given
	 *         shape
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