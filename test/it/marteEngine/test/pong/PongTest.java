package it.marteEngine.test.pong;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;

import java.io.IOException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

/**
 * Pong clone
 * 
 * @author RandomTower
 * @project MarteEngine
 */
public class PongTest extends StateBasedGame {

	public static final int INTRO_STATE = 0;
	public static final int GAME_STATE = 1;
	private static boolean ressourcesInited;
	
	public PongTest() {
		super("Pong Slick Marte Engine clone 1.0");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		initRessources();

		addState(new PongIntroState(INTRO_STATE));
		addState(new PongGameState(GAME_STATE));		
	}

	public static void initRessources() throws SlickException {
		if (ressourcesInited)
			return;
		try {
			ResourceManager.loadResources("data/pong/resources.xml");
		} catch (IOException e) {
			Log.error("failed to load ressource file 'data/pong/resources.xml': "
					+ e.getMessage());
			throw new SlickException("Resource loading failed!");
		}
		
		ressourcesInited = true;
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
