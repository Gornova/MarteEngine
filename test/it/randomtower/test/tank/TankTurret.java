package it.randomtower.test.tank;

import it.randomtower.engine.ME;
import it.randomtower.engine.ResourceManager;
import it.randomtower.engine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * Placed on top of a Tank, let it to fire Missiles {@link Missile}
 * 
 * @author Gornova
 */
public class TankTurret extends Entity {

	private static final String FIRE = "fire";
	// parent is Tank reference
	private Tank parent;
	
	public TankTurret(Tank parent) {
		super(parent.x,parent.y);
		// position turret on top of parent position
		this.parent = parent;
		this.setGraphic(ResourceManager.getSpriteSheet("tank").getSubImage(2, 0));
		
		this.setCentered(true);
		
		define(FIRE,Input.MOUSE_LEFT_BUTTON);
	}
	
	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		// set turret position to parent
		x = parent.x;
		y = parent.y;

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
			Missile m = new Missile(x, y, angle);
			m.setCentered(true);
			ME.world.add(m);
		}
		
		super.update(container, delta);
	}
	
}
