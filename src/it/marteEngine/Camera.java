package it.marteEngine;

import it.marteEngine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 * 
 * for now let's just assume that the camera covers the whole screen, starting
 * at 0,0 so no clipping area necessary
 * 
 */
public class Camera {

	private World myWorld;
	/** coordinates of the top left corner of the camera area in the world */
	public float cameraX;
	public float cameraY;
	/** width and height of the render area for this camera */
	private float renderWidth;
	private float renderHeight;
	private Vector2f maxSpeed = null;
	private int horBorderPixel = 0;
	private int vertBorderPixel = 0;
	private Rectangle visibleRect = null;
	private Rectangle moveRect = null;

	private Entity follow;

	public Camera(World world, Entity toFollow, int width, int height) {
		this(world, toFollow, width, height, -1, -1, null);
	}

	public Camera(World world, Entity toFollow, int width, int height,
			int horBorderPixel, int vertBorderPixel, Vector2f maxSpeed) {
		this.cameraX = 0;
		this.cameraY = 0;
		this.renderWidth = width;
		this.renderHeight = height;
		this.follow = toFollow;
		this.horBorderPixel = horBorderPixel;
		this.vertBorderPixel = vertBorderPixel;
		this.maxSpeed = maxSpeed;
		if (toFollow != null) {
			// on startup position camera that toFollow is in the center of the
			// screen
			this.cameraX = follow.x - (this.renderWidth / 2);
			this.cameraY = follow.y - (this.renderHeight / 2);
		}
		this.myWorld = world;
		this.visibleRect = new Rectangle(cameraX - horBorderPixel, cameraY
				- vertBorderPixel, renderWidth + horBorderPixel, renderHeight
				+ vertBorderPixel);
		this.moveRect = new Rectangle(cameraX - horBorderPixel, cameraY
				- vertBorderPixel, renderWidth + horBorderPixel, renderHeight
				+ vertBorderPixel);
		setCamera();
	}

	public void update(GameContainer container, int delta)
			throws SlickException {
		setCamera();
	}

	private void setCamera() {
		// position camera so that follow is on the center of the screen

		if (follow != null
				&& !moveRect.contains(follow.x + follow.width / 2, follow.y
						+ follow.height / 2)) {
			float targetCX = follow.x - (this.renderWidth / 2);
			float targetCY = follow.y - (this.renderHeight / 2);
			// now smoothly move camera on position cameraX, cameraY to position
			// targetCX, targetCY, using
			// maxSpeed
			if (maxSpeed != null) {
				if (Math.abs(targetCX - cameraX) > maxSpeed.x) {
					if (targetCX > cameraX)
						cameraX += maxSpeed.x * 2;
					else
						cameraX -= maxSpeed.x * 2;
				} else
					cameraX = targetCX;
				if (Math.abs(targetCY - cameraY) > maxSpeed.y) {
					if (targetCY > cameraY)
						cameraY += maxSpeed.y * 2;
					else
						cameraY -= maxSpeed.y * 2;
				} else
					cameraY = targetCY;
			} else {
				// move camera directly to new position
				cameraX = targetCX;
				cameraY = targetCY;
			}
			// recalculate worldX and worldY based on translateX and translateY
		}
		// Log.debug("setCamera(): cameraX = " + cameraX + ", cameraY = " +
		// cameraY + ", follow.x = " + follow.x + ", follow.y = " + follow.y);
		// do some border checking. we want to stay inside the world with our
		// container
		if (cameraX < 0)
			cameraX = 0;
		if (cameraX + renderWidth > myWorld.width)
			cameraX = myWorld.width - renderWidth + 1;
		if (cameraY < 0)
			cameraY = 0;
		if (cameraY + renderHeight > myWorld.height)
			cameraY = myWorld.height - renderHeight + 1;
		// Log.debug("setCamera2(): cameraX = " + cameraX + ", cameraY = " +
		// cameraY + ", follow.x = " + follow.x + ", follow.y = " + follow.y);
		// also calculate rendering rect to improve speed in contains() method
		// later on for rendering
		visibleRect.setBounds(cameraX - horBorderPixel, cameraY
				- vertBorderPixel, renderWidth + horBorderPixel, renderHeight
				+ vertBorderPixel);
		moveRect.setBounds(cameraX + horBorderPixel / 2 - follow.speed.x,
				cameraY + vertBorderPixel / 2, renderWidth - horBorderPixel
						+ follow.speed.x, renderHeight - vertBorderPixel);
	}

	public boolean contains(Entity e) {
		Rectangle entity = new Rectangle(e.x, e.y, e.width, e.height);
		return visibleRect.intersects(entity);
	}

	public World getMyWorld() {
		return myWorld;
	}

	public void setMyWorld(World myWorld) {
		this.myWorld = myWorld;
	}

	public Entity getFollow() {
		return follow;
	}

	public void setFollow(Entity follow) {
		this.follow = follow;
	}

	public Rectangle getVisibleRect() {
		return visibleRect;
	}

	public Rectangle getMoveRect() {
		return moveRect;
	}

}
