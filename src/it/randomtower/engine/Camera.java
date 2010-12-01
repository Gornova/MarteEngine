package it.randomtower.engine;

import it.randomtower.engine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class Camera {

	public float x;
	public float y;
	private float width;
	private float height;

	private Entity follow;

	public Camera(Entity toFollow, int width, int height) {
		this.width = width;
		this.height = height;
		this.follow = toFollow;
		setCamera();
	}

	public void update(GameContainer container, int delta)
			throws SlickException {
		setCamera();
	}

	private void setCamera() {
		x = -(follow.x - (width / 2));
		y = -(follow.y - (height / 2));
	}

	public boolean contains(Entity e) {
		Rectangle camera = new Rectangle(x - 10, y - 10, width + 10,
				height + 10);
		Rectangle entity = new Rectangle(e.x, e.y, e.width, e.height);
		return camera.intersects(entity);
	}

}
