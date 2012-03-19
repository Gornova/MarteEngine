package it.marteEngine.test.tween;

import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;
import it.marteEngine.tween.Ease;
import it.marteEngine.tween.LinearMotion;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Player extends Entity {

	public LinearMotion motion;
	public Ease currentEase = Ease.NONE;

	public Player(float x, float y) {
		super(x, y);
		setGraphic(ResourceManager.getImage("image"));
		define("MOVE", Input.MOUSE_LEFT_BUTTON);
		define("CHANGE_MODE", Input.MOUSE_RIGHT_BUTTON);
		define("START", Input.KEY_Z);
		define("PAUSE", Input.KEY_X);
		define("RESET", Input.KEY_C);

		motion = null;
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);
		if (motion != null)
			motion.update(delta);

		Input input = container.getInput();
		// change tween's ease
		if (check("CHANGE_MODE")) {
			currentEase = currentEase.next();
		}
		// check controls
		if (check("MOVE")) {
			// set new tween for player
			motion = new LinearMotion(x, y, input.getMouseX(),
					input.getMouseY(), 100, currentEase);
		}
		if (check("START")) {
			// start tween update
			motion.start();
		}
		if (check("PAUSE")) {
			// start tween update
			motion.pause();
		}
		if (check("RESET")) {
			// reset tween to starting position
			motion.reset();
			setPosition(motion.getPosition());
		}
		// update player position according to tween
		if (motion != null)
			setPosition(motion.getPosition());
	}
}
