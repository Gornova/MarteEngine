package it.randomtower.test.pong;

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
public class PongTest extends StateBasedGame {

	public PongTest() {
		super("Pong Slick Marte Engine clone 1.0");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		ME.container = container;
		
		PongGameState gameState = new PongGameState(0);
		
		addState(gameState);
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
