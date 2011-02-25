package it.marteEngine.test.tween;

import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;
import it.marteEngine.tween.LinearMotion;
import it.marteEngine.tween.Tween;
import it.marteEngine.tween.Tweener;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Player extends Entity {

	private Tweener tweener;

	public int currentEase = 0;

	public Player(float x, float y) {
		super(x, y);
		setGraphic(ResourceManager.getImage("image"));
		define("MOVE", Input.MOUSE_LEFT_BUTTON);
		define("CHANGE_MODE", Input.MOUSE_RIGHT_BUTTON);
		define("START", Input.KEY_Z);
		define("PAUSE", Input.KEY_X);
		define("RESET", Input.KEY_C);

		tweener = new Tweener();
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);

		Input input = container.getInput();
		// change tween's ease
		if (check("CHANGE_MODE")) {
			if (currentEase + 1 <= 26) {
				currentEase++;
			} else {
				currentEase = 0;
			}
		}
		// check controls
		if (check("MOVE")) {
			// set new tween for player
			tweener.add(new Tween(new LinearMotion(x, y, input.getMouseX(),
					input.getMouseY(), 100, currentEase), true));
		}
		if (check("START")) {
			// start tween update
			tweener.start();
		}
		if (check("PAUSE")) {
			// start tween update
			tweener.pause();
		}
		if (check("RESET")) {
			// reset tween to starting position
			setPosition(tweener.reset());
		}
		// update player position according to tween
		setPosition(tweener.apply(this));
	}
}
