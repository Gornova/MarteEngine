package it.randomtower.test.resize;

import it.randomtower.engine.ME;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ResizeTest extends StateBasedGame {

	public static int keyRestart = Input.KEY_R;

	public ResizeTest() {
		super("Resize Test");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		ME.container = container;
		ResizeGameState inGameState = new ResizeGameState(0);
		addState(inGameState);
	}

	public static void main(String[] argv) {
		try {
			ME.keyToggleDebug = Input.KEY_1;
			AppGameContainer container = new AppGameContainer(new ResizeTest());
			container.setDisplayMode(800, 600, false);
			container.setTargetFrameRate(60);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
