package it.marteEngine.test.tank;

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
 * Tank test is an effort to build a little tank-fighting game
 * 
 * @author Gornova
 */
public class TankTest extends StateBasedGame {
	private static boolean resourcesLoaded;

	public TankTest() throws SlickException {
		super("Tank Test");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		loadResources();
		TankGame inGameState = new TankGame(0, container);
		addState(inGameState);
	}

	public static void loadResources() throws SlickException {
		if (resourcesLoaded)
			return;
		try {
			ResourceManager.loadResources("data/tank/resources.xml");
		} catch (IOException e) {
			Log.error("failed to load resource file 'data/tank/resources.xml': "
					+ e.getMessage());
			throw new SlickException("Resource loading failed!");
		}

		resourcesLoaded = true;
	}

	public static void main(String[] argv) {
		try {
			ME.keyToggleDebug = Input.KEY_1;
			AppGameContainer container = new AppGameContainer(new TankTest());
			container.setDisplayMode(800, 600, false);
			container.setTargetFrameRate(60);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
