package it.marteEngine.test.tween;

import it.marteEngine.ResourceManager;

import java.io.IOException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

/**
 * Test for Tweening
 * 
 * @author Gornova
 */
public class TweenTest extends StateBasedGame {

	private static boolean resourcesInited;

	public TweenTest() throws SlickException {
		super("Tween Test");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		initResources();
		TweenWorld inGameState = new TweenWorld(0, container);
		addState(inGameState);

	}

	public static void initResources() throws SlickException {
		if (resourcesInited)
			return;
		try {
			ResourceManager.loadResources("data/tween/resources.xml");
		} catch (IOException e) {
			Log.error("failed to load resource file 'data/tween/resources.xml': "
					+ e.getMessage());
			throw new SlickException("Resource loading failed!");
		}

		resourcesInited = true;
	}

	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(new TweenTest());
			container.setDisplayMode(800, 600, false);
			container.setTargetFrameRate(60);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
