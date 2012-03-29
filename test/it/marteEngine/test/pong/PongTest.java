package it.marteEngine.test.pong;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;

import java.io.IOException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Pong clone
 * 
 * @author RandomTower
 */
public class PongTest extends StateBasedGame {

	public static final int INTRO_STATE = 0;
	public static final int GAME_STATE = 1;

	public PongTest() {
		super("Pong Slick Marte Engine clone 1.0");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		ResourceManager.loadResources("data/pong/resources.xml");
		addState(new PongIntroState(INTRO_STATE));
		addState(new PongGameState(GAME_STATE));
	}

	public static void main(String[] argv) {
		try {
			ME.keyToggleDebug = Input.KEY_1;
			AppGameContainer container = new AppGameContainer(new PongTest());
			container.setDisplayMode(640, 480, false);
			container.setTargetFrameRate(60);
			container.setShowFPS(false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
