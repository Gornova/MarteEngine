package it.marteEngine.game.starcleaner;

import it.marteEngine.entity.PhysicsEntity;
import it.marteEngine.resource.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.Random;

public class MovingPlatform extends PhysicsEntity {

	private static String[] carry = {PLAYER};

	private boolean direction;
	private Image oneBlock = ResourceManager.getImage("platform");

	private int movement = 2;

	public MovingPlatform(float x, float y) {
		super(x, y);
		name = "platform";
		depth = 5;
		direction = (new Random()).nextBoolean();
		setHitBox(0, 0, 80, 40);
		width = 80;
		height = 40;
	}

	public void update(GameContainer container, int delta)
			throws SlickException {
		// move in the correct direction
		speed.x = direction ? movement : -movement;

		// move stuff that's on top of us, for each type of entity we can carry
		for (String obj : carry) {
			moveontop(obj, speed.x);
		}
		// move ourselves
		motion(true, true);

		// if we've stopped moving, switch directions!
		if (speed.x == 0) {
			direction = !direction;
		}
	}

	public void render(GameContainer container, Graphics g) throws SlickException {
		if (!visible)
			return;
		g.drawImage(oneBlock, x, y);
		g.drawImage(oneBlock, x + 40, y);
		super.render(container, g);
	}
}
