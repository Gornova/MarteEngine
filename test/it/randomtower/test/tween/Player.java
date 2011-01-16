package it.randomtower.test.tween;

import it.randomtower.engine.ResourceManager;
import it.randomtower.engine.entity.Entity;
import it.randomtower.engine.tween.LinearMotion;
import it.randomtower.engine.tween.Tween;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Player extends Entity {

	private Tween tween;

	public int currentEase = 0;

	public Player(float x, float y) {
		super(x, y);
		setGraphic(ResourceManager.getImage("image"));
		define("MOVE", Input.MOUSE_LEFT_BUTTON);
		define("CHANGE_MODE",Input.MOUSE_RIGHT_BUTTON);
		define("START",Input.KEY_SPACE);
		define("RESET",Input.KEY_ENTER);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);

		Input input = container.getInput();

		if (check("CHANGE_MODE")){
			if (currentEase+1 <=26 ){
				currentEase++;
			} else {
				currentEase = 0;
			}
		}
		if (check("MOVE")) {
			tween = new Tween(this, new LinearMotion(x, y, input.getMouseX(),
					input.getMouseY(), 100, currentEase),true);
		}

		if (check("START")){
			tween.start();
		}
		
		if (check("RESET")){
			tween.reset();
		}		
		
		if (tween != null) {
			tween.update(container, delta);
		}

	}

}
