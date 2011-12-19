package it.marteEngine;

import it.marteEngine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import java.awt.Point;

/**
 * A Camera is a device through which the player views the world.
 * For now let's just assume that the camera covers the whole screen, starting
 * at 0,0 so no clipping area is necessary.
 */
public class Camera {
	private float cameraX;
	private float cameraY;
	private final float cameraWidth, cameraHeight;
	private final Rectangle visibleRect, moveRect;
	private final int horBorderPixel, vertBorderPixel;
	private Vector2f speed;
	private World world;
	private Entity entityToFollow;

	/**
	 * Create a camera without smooth movement. Instead the camera will
	 * jump in 1 frame towards the target coordinates.
	 * Call {@link #setSpeed} to allow smooth movement.
	 */
	public Camera(World world, int cameraWidth, int cameraHeight) {
		this(world, cameraWidth, cameraHeight, -1, -1, new Vector2f(0, 0));
	}

	/**
	 * Create a camera with smooth movement.
	 */
	public Camera(World world, int cameraWidth, int cameraHeight, Vector2f speed) {
		this(world, cameraWidth, cameraHeight, -1, -1, speed);
	}

	/**
	 * Create a camera without smooth movement. Instead the camera will
	 * jump in 1 frame towards the entity coordinates.
	 * Call {@link #setSpeed} to allow smooth movement.
	 */
	public Camera(World world, Entity entity, int cameraWidth, int cameraHeight) {
		this(world, entity, 5, 5, cameraWidth, cameraHeight, new Vector2f(0, 0));
	}

	/**
	 * Create a camera that keeps on following an entity.
	 * The camera starts to scroll when the entity is at the edge of the camera.
	 */
	public Camera(World world, Entity entity, int cameraWidth,
				  int cameraHeight, Vector2f speed) {
		this(world, entity, 5, 5, cameraWidth, cameraHeight, speed);
	}

	/**
	 * Create a camera that follows an entity.
	 * <p/>
	 * The horBorderPixel and vertBorderPixel define a move region around the entity.
	 * The camera does not move when the entity is within the move rectangle.
	 * The camera starts moving when the entity is at the edge of the move rectangle.
	 */
	public Camera(World world, Entity entity, int cameraWidth,
				  int cameraHeight, int horBorderPixel, int vertBorderPixel,
				  Vector2f speed) {
		this(world, cameraWidth, cameraHeight, horBorderPixel, vertBorderPixel, speed);
		follow(entity);
	}

	public Camera(World world, int cameraWidth, int cameraHeight, int horBorderPixel, int vertBorderPixel, Vector2f speed) {
		this.cameraX = 0;
		this.cameraY = 0;
		this.cameraWidth = cameraWidth;
		this.cameraHeight = cameraHeight;
		this.horBorderPixel = horBorderPixel;
		this.vertBorderPixel = vertBorderPixel;
		this.speed = speed;
		this.world = world;

		if (speed == null) {
			throw new NullPointerException("speed cannot be null");
		}

		checkCameraPosition();
		visibleRect = new Rectangle(cameraX - horBorderPixel, cameraY
			- vertBorderPixel, cameraWidth + horBorderPixel, cameraHeight
			+ vertBorderPixel);
		moveRect = new Rectangle(cameraX + horBorderPixel / 2, cameraY
			+ vertBorderPixel / 2, cameraWidth - horBorderPixel,
			cameraHeight - vertBorderPixel);
	}

	public void update(GameContainer container, int delta)
		throws SlickException {
		if (entityToFollow != null && !isEntityWithinMoveRect()) {
			followEntity();
		}
		checkCameraPosition();
		calculateRectangles();
	}

	private boolean isEntityWithinMoveRect() {
		return moveRect.contains(entityToFollow.x + entityToFollow.width / 2,
			entityToFollow.y + entityToFollow.height / 2);
	}

	private void followEntity() {
		float centerX = entityToFollow.x < 0 ? 0 : entityToFollow.x
			- (cameraWidth / 2);
		float centerY = entityToFollow.y < 0 ? 0 : entityToFollow.y
			- (cameraHeight / 2);
		moveTo(centerX, centerY);
	}

	/**
	 * Check the camera coordinates. Make sure that the camera
	 * remains inside the world.
	 */
	private void checkCameraPosition() {
		if (cameraX < 0) {
			cameraX = 0;
		}
		if (cameraX + cameraWidth > world.width && canMoveHorizontally()) {
			cameraX = world.width - cameraWidth + 1;
		}
		if (cameraY < 0) {
			cameraY = 0;
		}
		if (cameraY + cameraHeight > world.height && canMoveVertically()) {
			cameraY = world.height - cameraHeight + 1;
		}
	}

	/**
	 * Calculate rendering rect to improve speed in contains() method
	 * later on for rendering
	 */
	private void calculateRectangles() {
		visibleRect.setBounds(cameraX - horBorderPixel, cameraY
			- vertBorderPixel, cameraWidth + horBorderPixel, cameraHeight
			+ vertBorderPixel);

		float entitySpeedX = 0;
		if (entityToFollow != null) {
			entitySpeedX = entityToFollow.speed.x;
		}
		moveRect.setBounds(cameraX + horBorderPixel / 2 - entitySpeedX, cameraY
			+ vertBorderPixel / 2, cameraWidth - horBorderPixel
			+ entitySpeedX, cameraHeight - vertBorderPixel);
	}

	/**
	 * Move the camera with the x and y offset.
	 * Using the speed of the camera. See {@link #setSpeed}
	 * This is the same as calling following method
	 * moveTo(camera.getX() + xOffset, camera.getY() + yOffset)
	 *
	 * @param xOffset The amount of pixels the camera should move on the x axis
	 * @param yOffset The amount of pixels the camera should move on the y axis
	 */
	public void scroll(float xOffset, float yOffset) {
		moveTo(cameraX + xOffset, cameraY + yOffset);
	}

	/**
	 * Position the camera so that it keeps on following the entity on the screen
	 *
	 * @param entity The entity to follow.
	 */
	public void follow(Entity entity) {
		center(entity.x, entity.y);
		checkCameraPosition();
		entityToFollow = entity;
	}

	public void stopFollowingEntity() {
		entityToFollow = null;
	}

	/**
	 * Move the camera to the given x, y coordinates.
	 * So that the x, y is in the center of the camera.
	 */
	public void center(float x, float y) {
		float cameraX = x - (cameraWidth / 2);
		float cameraY = y - (cameraHeight / 2);
		moveTo(cameraX, cameraY);
	}

	/**
	 * Move the camera smoothly to the target position.
	 * Using the speed of the camera. If no speed is set the camera
	 * will moves directly to the targetX, targetY position.
	 *
	 * @param targetX The x coordinate the camera should move to
	 * @param targetY The y coordinate the camera should move to
	 * @see #setSpeed(float)
	 */
	public void moveTo(float targetX, float targetY) {
		final boolean canMoveHorizontal = canMoveHorizontally();
		final boolean canMoveVertical = canMoveVertically();
		if (targetX < 0)
			targetX = 0;
		if (targetY < 0)
			targetY = 0;
		if (!canMoveHorizontal)
			targetX = 0;
		if (!canMoveVertical)
			targetY = 0;

		// Make sure the camera fits into the world
		if (canMoveHorizontal && !world.isWithinBounds(targetX + cameraWidth, 0)) {
			targetX = world.getWidth() - cameraWidth;
		}
		if (canMoveVertical && !world.isWithinBounds(0, targetY + cameraHeight)) {
			targetY = world.getHeight() - cameraHeight;
		}
		if (canMoveHorizontal && !world.isWithinBounds(targetX, 0)) {
			targetX = 0;
		}
		if (canMoveVertical && !world.isWithinBounds(0, targetY)) {
			targetY = 0;
		}

		if (speed.x != 0 && Math.abs(targetX - cameraX) > speed.x) {
			if (targetX > cameraX) {
				cameraX += speed.x * 2;
			} else {
				cameraX -= speed.x * 2;
			}
		} else {
			cameraX = targetX;
		}

		if (speed.y != 0 && Math.abs(targetY - cameraY) > speed.y) {
			if (targetY > cameraY) {
				cameraY += speed.y * 2;
			} else {
				cameraY -= speed.y * 2;
			}
		} else {
			cameraY = targetY;
		}
	}

	private boolean canMoveVertically() {
		return cameraHeight < world.height;
	}

	private boolean canMoveHorizontally() {
		return cameraWidth < world.width;
	}

	public boolean contains(Entity e) {
		Rectangle entity = new Rectangle(e.x, e.y, e.width, e.height);
		return visibleRect.intersects(entity);
	}

	public void setWorld(World world) {
		this.world = world;
	}

	/**
	 * Set the horizontal and vertical move speed of the camera
	 */
	public void setSpeed(float newValue) {
		setHorizontalSpeed(newValue);
		setVerticalSpeed(newValue);
	}

	/**
	 * Set the horizontal move speed of the camera
	 */
	public void setHorizontalSpeed(float newValue) {
		if (newValue < 0) {
			throw new IllegalArgumentException(
				"Move speed must be a positive number");
		}
		speed.x = newValue;
	}

	/**
	 * Set the Vertical move speed of the camera
	 */
	public void setVerticalSpeed(float newValue) {
		if (newValue < 0) {
			throw new IllegalArgumentException(
				"Move speed must be a positive number");
		}
		speed.y = newValue;
	}

	public Rectangle getVisibleRect() {
		return visibleRect;
	}

	public Rectangle getMoveRect() {
		return moveRect;
	}

	public Point screenToWorldCoordinate(float x, float y) {
		return new Point((int) (x + cameraX), (int) (y + cameraY));
	}

	public int getX() {
		return (int) cameraX;
	}

	public int getY() {
		return (int) cameraY;
	}

	public float getWidth() {
		return cameraWidth;
	}

	public float getHeight() {
		return cameraHeight;
	}
}