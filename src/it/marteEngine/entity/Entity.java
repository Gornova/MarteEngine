package it.marteEngine.entity;

import it.marteEngine.ME;
import it.marteEngine.StateManager;
import it.marteEngine.World;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import java.util.*;

//TODO modify hitbox coordinates to a real shape without changing method interface.
//TODO a shape can be rotated and scaled when the entity is rotated and scaled.
public abstract class Entity implements Comparable<Entity> {

	/** default collidable type SOLID */
	public static final String SOLID = "Solid";

	/** predefined type for player */
	public static final String PLAYER = "Player";

	/** the world this entity lives in */
	public World world;

	/** unique identifier */
	public String name;

	/** x, y position */
	public float x, y;

	/**
	 * If this entity is centered the x,y position is in the center. otherwise
	 * the x,y position is the top left corner.
	 */
	public boolean centered;

	/**
	 * Dimension of the entity. Not necessarily the width of the hitbox. Used
	 * for world wrapping
	 */
	public int width, height;

	public float previousx, previousy;

	/** start x and y position */
	public float startx, starty;

	/**
	 * If true the entity reappears on the opposite side of the world when it
	 * leaves the world.
	 */
	public boolean wrapHorizontal, wrapVertical;

	/** Specifies x and y movement per update call in pixels */
	public Vector2f speed = new Vector2f(0, 0);

	/**
	 * Angle in degrees from 0 to 360, used for drawing the entity rotated. NOT
	 * used for direction!
	 */
	protected int angle = 0;

	/** The center point to use when rotating, in screen coordinates. */
	protected float centerOfRotationX, centerOfRotationY;

	/** Scale used for both horizontal and vertical scaling. */
	public float scale = 1.0f;

	/**
	 * Color of the entity, mainly used for alpha transparency, but could also
	 * be used for tinting
	 */
	private Color color = new Color(Color.white);

	public AlarmContainer alarms;

	protected SpriteSheet sheet;
	private Map<String, Animation> animations = new HashMap<>();
	private String currentAnim;
	public int duration = 200;
	public int depth = -1;

	/** static image for non-animated entity */
	private Image currentImage;

	/** Allows to bind input to a string command */
	public InputManager input;

	/** The types this entity can collide with */
	private HashSet<String> collisionTypes = new HashSet<>();

	/** true if this entity can receive updates */
	public boolean active = true;
	public boolean collidable = true;
	public boolean visible = true;
	private boolean leftTheWorld;

	public float hitboxOffsetX;
	public float hitboxOffsetY;
	public int hitboxWidth;
	public int hitboxHeight;

	public StateManager stateManager;

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

	public void update(GameContainer container, int delta)
			throws SlickException {
		previousx = x;
		previousy = y;
		if (stateManager.isActive()) {
			stateManager.update(container, delta);
			return;
		}
		updateAnimation(delta);
		if (speed != null) {
			x += speed.x;
			y += speed.y;
		}
		previousx = x;
		previousy = y;
	}

	protected void updateAnimation(int delta) {
		if (currentAnim != null) {
			Animation anim = animations.get(currentAnim);
			if (anim != null) {
				anim.update(delta);
			}
		}
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		if (stateManager.isActive()) {
			stateManager.render(g);
		} else if (currentAnim != null) {
			Animation anim = animations.get(currentAnim);
			render(anim.getCurrentFrame(), g);
		} else if (currentImage != null) {
			render(currentImage, g);
		}
	}

	private void render(Image img, Graphics g) {
		img.setAlpha(color.a);

		float xpos, ypos;
		if (centered) {
			float scaledWidth = img.getWidth() * scale;
			float scaledHeight = img.getHeight() * scale;
			xpos = x - scaledWidth / 2;
			ypos = y - scaledHeight / 2;
			centerOfRotationX = x;
			centerOfRotationY = y;
		} else {
			xpos = x;
			ypos = y;
		}

		if (angle != 0) {
			g.rotate(centerOfRotationX, centerOfRotationY, angle);
		}

		img.draw(xpos, ypos, scale, color);

		if (angle != 0) {
			g.rotate(centerOfRotationX, centerOfRotationY, -angle);
		}
	}

	public void renderDebug(Graphics g) {
		g.setColor(ME.borderColor);
		Rectangle hitBox = new Rectangle(x + hitboxOffsetX, y + hitboxOffsetY,
				hitboxWidth, hitboxHeight);
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
	 * Add a name, animation pair to this entity. If the animation name is
	 * already used, the new animation overwrites the previous animation.
	 * 
	 * @param animName
	 *            The unique identifier for this animation
	 * @param animation
	 *            The animation to add
	 * @see #setAnim(String)
	 */
	public void addAnimation(String animName, Animation animation) {
		animations.put(animName, animation);
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

	/**
	 * Start playing the animation stored as animName.
	 * 
	 * @param animName
	 *            The name of the animation to play
	 * @throws IllegalArgumentException
	 *             If there is no animation stored as animName
	 * @see #addAnimation(String, Animation)
	 */
	public void setAnim(String animName) {
		if (!animations.containsKey(animName)) {
			throw new IllegalArgumentException("No animation for " + animName
					+ " animations: " + animations.keySet());
		}
		currentAnim = animName;
		Animation currentAnimation = animations.get(currentAnim);
		width = currentAnimation.getWidth();
		height = currentAnimation.getHeight();
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
		this.collidable = true;
	}

	public void setColor(Color color) {
		this.color = color;
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

	public void setWorld(World world) {
		this.world = world;
		input.setInputProvider(world.container.getInput());
	}

	// TODO: add proper rotation for the hitbox/shape here!!!
	public void setAngle(int angle) {
		this.angle = angle;
	}

	/**
	 * Set the center where the entity should rotate around.
	 * 
	 * @param corX
	 *            The x coordinate relative to the top left corner of the entity
	 *            to be used as rotation center.
	 * @param corY
	 *            The y coordinate relative to the top left corner of the entity
	 *            to be used as rotation center.
	 */
	public void setCenterOfRotation(int corX, int corY) {
		centerOfRotationX = this.x + corX;
		centerOfRotationY = this.y + corY;
	}

	/**
	 * remove ourselves from world
	 */
	public void destroy() {
		this.world.remove(this);
		this.visible = false;
	}

	/**
	 * @deprecated As of release 0.3, replaced by
	 *             {@link #bindToKey(String, int...)} and
	 *             {@link #bindToMouse(String, int...)}
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
						collidingEntities = new ArrayList<>();
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
	 * Check if this entity has left the world.
	 *
	 * If the entity has moved outside of the world then the entity is notified
	 * once by the {@link #leftWorldBoundaries()} event.
	 *
	 * If the entity must be wrapped (wrapHorizontal or wrapVertical is true)
	 * make this entity reappear on the opposite side of the world. The entity
	 * did not leave the world hence {@link #leftWorldBoundaries()} is not
	 * called.
	 */
	public void checkWorldBoundaries() {
		if (wrapHorizontal || wrapVertical) {
			wrapEntity();
		} else {
			if (world.contains(this)) {
				leftTheWorld = false;
			} else {
				if (!leftTheWorld) {
					leftWorldBoundaries();
					leftTheWorld = true;
				}
			}
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
				x = -width + 1;
			}
		}
		if (y + height < 0) {
			if (wrapVertical) {
				y = world.height - 1;
			}
		}
		if (y > world.height) {
			if (wrapVertical) {
				y = -height + 1;
			}
		}
	}

	/**
	 * Compare to another entity on zLevel
	 */
	public int compareTo(Entity o) {
		return Integer.compare(depth, o.depth);
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

	public String[] getTypes() {
		return collisionTypes.toArray(new String[collisionTypes.size()]);
	}

	public boolean isType(String type) {
		return collisionTypes.contains(type);
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

	public int getAngle() {
		return angle;
	}

	public Color getColor() {
		return color;
	}

	public float getAlpha() {
		return color.a;
	}

	public boolean hasAnim(String animName) {
		return animations.containsKey(animName);
	}

	public boolean isCurrentAnim(String animName) {
		return currentAnim != null && currentAnim.equals(animName);
	}

	public Animation getCurrentAnim() {
		if (animations.isEmpty()) {
			throw new IllegalArgumentException(
					"No animations defined, use addAnimation.");
		}
		if (currentAnim == null) {
			throw new IllegalArgumentException("No animation set, use setAnim");
		}
		return animations.get(currentAnim);
	}

	public String toCsv() {
		return "" + (int) x + "," + (int) y + "," + name + ","
				+ collisionTypesToString();
	}

	public String toString() {
		return "name: " + name +
				", types: " + collisionTypesToString() +
				", depth: " + depth +
				", x: " + x +
				", y: " + y;
	}

	private String collisionTypesToString() {
		StringBuilder sb = new StringBuilder();
		for (String type : collisionTypes) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(type);
		}
		return sb.toString();
	}
}
