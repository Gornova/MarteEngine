package it.marteEngine.test.resize;

import it.marteEngine.ME;
import it.marteEngine.entity.Entity;
import it.marteEngine.test.tank.Missile;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * Placed on top of a Tank, let it to fire Missiles {@link Missile}
 * 
 * @author Gornova
 */
public class Turret extends Entity {

	private static final String FIRE = "fire";
	// parent is Tank reference
	private Entity parent;
	
	public Turret(Entity parent) {
		super(parent.x,parent.y);
		// position turret on top of parent position
		this.parent = parent;
		this.setCentered(true);
		define(FIRE,Input.MOUSE_LEFT_BUTTON);
	}
	
	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		// set turret position to parent
		x = parent.x+parent.width/2;
		y = parent.y+parent.height/2;

		// calculate heading of turret
		Input input = container.getInput();
		float mx = input.getMouseX();
		float my = input.getMouseY();
		// TODO Going to add the offset here! HACK
		//mx -= 20;
		//my -= 15;
		angle = (int) calculateAngle(x, y, mx, my);
	
		// add new Missile when player fire
		if (check(FIRE)){
			Bullet b = new Bullet(x, y, "data/bullet.png", angle);
			b.setCentered(true);
			ME.world.add(b);
		}
		
		super.update(container, delta);
	}
	
}
