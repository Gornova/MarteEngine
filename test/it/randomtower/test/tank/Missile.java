package it.randomtower.test.tank;

import it.randomtower.engine.ME;
import it.randomtower.engine.ResourceManager;
import it.randomtower.engine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 * Little missile Tank can fire
 * 
 * @author Gornova
 */
public class Missile extends Entity {

	public Missile(float x, float y, int angle) {
		super(x, y);
		this.angle =angle;
		setGraphic(ResourceManager.getImage("missile"));
		
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
	
	@Override
	public void collisionResponse(Entity other) {
		ME.world.remove(this);
	}
}
