package it.marteEngine.test.tween;

import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;
import it.marteEngine.tween.LinearMotion;

import it.marteEngine.tween.Motion;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Player extends Entity {

	public Motion motion;
	public int currentEase = 0;

	public Player(float x, float y) {
		super(x, y);
		setGraphic(ResourceManager.getImage("image"));
		define("MOVE", Input.MOUSE_LEFT_BUTTON);
		define("CHANGE_MODE", Input.MOUSE_RIGHT_BUTTON);
		define("START", Input.KEY_Z);
		define("PAUSE", Input.KEY_X);
		define("RESET", Input.KEY_C);
		motion = new LinearMotion(x, y, x, y, 0, 0);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);
		motion.update(delta);
		checkInput(container.getInput());

		// update player position according to tween
		setPosition(motion.getPosition());
	}

	private void checkInput(Input input) {
		if (pressed("CHANGE_MODE")) {
			if (currentEase + 1 <= 26) {
				currentEase++;
			} else {
				currentEase = 0;
			}
		}

		if (pressed("MOVE")) {
			motion = new LinearMotion(x, y, input.getMouseX(),
					input.getMouseY(), 100, currentEase);
		}
		if (pressed("START")) {
			motion.start();
		}
		if (pressed("PAUSE")) {
			motion.setActive(!motion.isActive());
		}
		if (pressed("RESET")) {
			motion.reset();
		}
	}
}
