package it.randomtower.test.tank;

import java.io.IOException;

import it.randomtower.engine.ME;
import it.randomtower.engine.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

public class TankTest extends StateBasedGame {

	public static int keyRestart = Input.KEY_R;
	private static boolean ressourcesInited;

	public TankTest() throws SlickException {
		super("Tank Test");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		initRessources();
		TankGame inGameState = new TankGame(0,container);
		addState(inGameState);
		
	}

	public static void initRessources() throws SlickException {
		if (ressourcesInited)
			return;
		try {
			ResourceManager.loadResources("data/tank/resources.xml");
		} catch (IOException e) {
			Log.error("failed to load ressource file 'data/tank/resources.xml': "
					+ e.getMessage());
			throw new SlickException("Resource loading failed!");
		}
		
		ressourcesInited = true;
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
