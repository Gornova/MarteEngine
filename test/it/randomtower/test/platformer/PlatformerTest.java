package it.randomtower.test.platformer;

import it.randomtower.engine.ME;
import it.randomtower.engine.ResourceManager;

import java.io.IOException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

/**
 * Platformer Test
 * 
 * @author Gornova
 */
public class PlatformerTest extends StateBasedGame {

	public static final int GAME_STATE = 0;
	private static boolean ressourcesInited;

	public PlatformerTest() {
		super("PlatformerTest MarteEngine");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		initRessources();
		PlatformerGameWorld gameState = new PlatformerGameWorld(
				GAME_STATE);

		addState(gameState);
	}

	public static void initRessources() throws SlickException {
		if (ressourcesInited)
			return;
		try {
			ResourceManager.loadResources("data/platformer/resources.xml");
		} catch (IOException e) {
			Log.error("failed to load ressource file 'data/platformer/resources.xml': "
					+ e.getMessage());
			throw new SlickException("Resource loading failed!");
		}
		
		ressourcesInited = true;
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
