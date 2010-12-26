package it.randomtower.test.tank;

import it.randomtower.engine.ResourceManager;
import it.randomtower.engine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Tank extends Entity {

	private static final String FORWARD = "forward";
	private static final String BACKWARD = "backward";
	private static final String ROTATE_RIGHT = "rotate right";
	private static final String ROTATE_LEFT = "rotate left";

	public Tank(float x, float y) {
		super(x, y);
		
		this.setGraphic(ResourceManager.getImage("redTank"));
		
		this.setCentered(true);
		
		define(FORWARD, Input.KEY_W, Input.KEY_UP);
		define(BACKWARD, Input.KEY_S, Input.KEY_DOWN);
		define(ROTATE_LEFT,Input.KEY_A,Input.KEY_LEFT);
		define(ROTATE_RIGHT,Input.KEY_D,Input.KEY_RIGHT);
	}
	
	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		
		float dx=0;
		float dy=0;
		
		if (check(FORWARD)){
			Vector2f speed = calculateVector(angle, 2);
			dx += speed.x;
			dy += speed.y;
			x+=dx;
			y+=dy;
		} else if (check(BACKWARD)){
			y += 2;
		} 
		if (check(ROTATE_LEFT)){
			angle-=2;
		} else if (check(ROTATE_RIGHT)){
			angle+=2;
		}
		
		if (angle >= 360)
			angle -= 360;
		
		super.update(container, delta);
	}
	
}
