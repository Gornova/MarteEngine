package it.randomtower.test.tween;

import java.io.IOException;

import it.randomtower.engine.ME;
import it.randomtower.engine.ResourceManager;

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
public class TweenTest extends StateBasedGame {

	public static int keyRestart = Input.KEY_R;
	private static boolean ressourcesInited;

	public TweenTest() throws SlickException {
		super("Tween Test");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		initRessources();
		TweenWorld inGameState = new TweenWorld(0,container);
		addState(inGameState);
		
	}

	public static void initRessources() throws SlickException {
		if (ressourcesInited)
			return;
		try {
			ResourceManager.loadResources("data/tween/resources.xml");
		} catch (IOException e) {
			Log.error("failed to load ressource file 'data/tween/resources.xml': "
					+ e.getMessage());
			throw new SlickException("Resource loading failed!");
		}
		
		ressourcesInited = true;
	}	
	
	public static void main(String[] argv) {
		try {
			ME.keyToggleDebug = Input.KEY_1;
			AppGameContainer container = new AppGameContainer(new TweenTest());
			container.setDisplayMode(800, 600, false);
			container.setTargetFrameRate(60);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
