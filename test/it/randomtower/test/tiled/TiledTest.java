package it.randomtower.test.tiled;

import it.randomtower.engine.ResourceManager;

import java.io.IOException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

/**
 * Test for Tiled (http://mapeditor.org) support 
 * 
 * @author Gornova
 */
public class TiledTest extends StateBasedGame {

	public static int keyRestart = Input.KEY_R;
	private static boolean ressourcesInited;

	public TiledTest() throws SlickException {
		super("Tiled Integration Test");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		// init test resources 
		initRessources();
		addState(new TiledWorld(1, container));
	}

	public static void initRessources() throws SlickException {
		if (ressourcesInited)
			return;
		try {
			ResourceManager.loadResources("data/tiled/resources.xml");
		} catch (IOException e) {
			Log.error("failed to load ressource file 'data/tiled/resources.xml': "
					+ e.getMessage());
			throw new SlickException("Resource loading failed!");
		}
		
		ressourcesInited = true;
	}	
	
	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(new TiledTest());
			container.setDisplayMode(800, 600, false);
			container.setTargetFrameRate(60);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
