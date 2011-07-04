package it.marteEngine.test.zombieEscape;

import it.marteEngine.entity.Entity;
import it.marteEngine.test.zombieEscape.state.AlertState;
import it.marteEngine.test.zombieEscape.state.MoveState;
import it.marteEngine.test.zombieEscape.state.WaitState;

import org.newdawn.slick.Image;

public class Zombie extends Entity {

	public static final String ZOMBIE = "zombie";

	public Zombie(float x, float y, Image image) {
		super(x, y, image);

		setHitBox(0, 0, image.getWidth(), image.getHeight());

		addType(ZOMBIE);

		stateManager.add(new WaitState(this));
		stateManager.add(new AlertState(this));
		stateManager.add(new MoveState(this));
		
	}

}
