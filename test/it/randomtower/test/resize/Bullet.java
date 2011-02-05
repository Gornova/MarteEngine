package it.randomtower.test.resize;

import it.randomtower.engine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Bullet extends Entity {

	public float fireSpeed = 0.5f;
	public static final String NAME = "BULLET";

	public Bullet(float x, float y, String ref, int angle) throws SlickException {
		super(x, y);
		this.angle =angle;
		currentImage = new Image(ref);
		
		addType(SOLID);
		setHitBox(0, 0, 8, 8);
	}
	
	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		float dx = 0;
		float dy = 0;
		Vector2f vectorSpeed = calculateVector(angle, 8);
		dx += vectorSpeed.x;
		dy += vectorSpeed.y;
		x+=dx;
		y+=dy;
		
		collide(SOLID, x, y);
		
		super.update(container, delta);
	}
	
}
