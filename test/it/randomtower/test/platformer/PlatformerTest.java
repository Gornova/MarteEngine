package it.randomtower.test.platformer;

import it.randomtower.engine.ME;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Pong clone
 * 
 * @author RandomTower
 * @project MarteEngine
 */
public class PlatformerTest extends StateBasedGame {

	public static final int GAME_STATE = 0;

	public PlatformerTest() {
		super("PlatformerTest MarteEngine");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {

		PlatformerGameWorld gameState = new PlatformerGameWorld(
				GAME_STATE);

		addState(gameState);
	}

	public static void main(String[] argv) {
		try {
			ME.keyToggleDebug = Input.KEY_1;
			AppGameContainer container = new AppGameContainer(
					new PlatformerTest());
			container.setDisplayMode(640, 480, false);
			container.setTargetFrameRate(60);
			container.setShowFPS(false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
