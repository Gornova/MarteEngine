package it.marteEngine.test.scrollingPlatformer;

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
 * Scrolling Platformer Test
 * 
 * @author Gornova
 */
public class ScrollingPlatformerTest extends StateBasedGame {

	public static final int GAME_STATE = 0;
	private static boolean ressourcesInited;

	public ScrollingPlatformerTest() {
		super("ScrollingPlatformerTest MarteEngine");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		initRessources();
		ScrollingPlatformerGameWorld gameState = new ScrollingPlatformerGameWorld(
				GAME_STATE);

		addState(gameState);
	}

	public static void initRessources() throws SlickException {
		if (ressourcesInited)
			return;
		try {
			ResourceManager.loadResources("data/scrollingPlatformer/resources.xml");
		} catch (IOException e) {
			Log.error("failed to load ressource file 'data/scrollingPlatformer/resources.xml': "
					+ e.getMessage());
			throw new SlickException("Resource loading failed!");
		}
		
		ressourcesInited = true;
	}	
	
	public static void main(String[] argv) {
		try {
			ME.keyToggleDebug = Input.KEY_1;
			//ME.keyRestart = Input.KEY_R;
			AppGameContainer container = new AppGameContainer(
					new ScrollingPlatformerTest());
			container.setDisplayMode(640, 480, false);
			container.setTargetFrameRate(60);
			container.setShowFPS(false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
