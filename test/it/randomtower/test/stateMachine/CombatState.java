package it.randomtower.test.stateMachine;

import it.randomtower.engine.State;
import it.randomtower.engine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class CombatState implements State {

	private Image image;

	private Entity entity;

	public CombatState(Entity e) {
		this.entity = e;
	}

	public void init() {
		try {
			image = new Image("data/sword-icon.png");
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
		Input input = container.getInput();		
		if (input.isKeyPressed(Input.KEY_SPACE)) {
			entity.stateManager.enter(IdleState.class);
		}
	}
	
}
