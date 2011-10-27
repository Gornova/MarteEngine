package it.marteEngine.test.stateMachine;

import it.marteEngine.State;
import it.marteEngine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class MovingState implements State {

	private Image image;

	private Entity entity;

	public MovingState(Entity e) {
		this.entity = e;
	}

	public void init() {
		try {
			image = new Image("data/move.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void render(Graphics g) {
		g.drawImage(entity.getCurrentImage(), entity.x, entity.y);
		// render status image on top left of parent image
		g.drawImage(image, entity.x - 10, entity.y - 10);
	}

	public void update(GameContainer container, int delta) {
		// move entity
		Input input = container.getInput();
		if (input.isKeyDown(Input.KEY_W)) {
			entity.y -= entity.speed.y;
		} else if (input.isKeyDown(Input.KEY_S)) {
			entity.y += entity.speed.y;
		} else if (input.isKeyDown(Input.KEY_D)) {
			entity.x += entity.speed.x;
		} else if (input.isKeyDown(Input.KEY_A)) {
			entity.x -= entity.speed.x;
		}
		if (input.isKeyPressed(Input.KEY_SPACE)) {
			entity.stateManager.enter(CombatState.class);
		}
	}

}
