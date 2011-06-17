package it.marteEngine.test.particles;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;

import java.io.IOException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

public class ParticlesTest extends StateBasedGame {

	public static int keyRestart = Input.KEY_R;
	private static boolean ressourcesInited;

	public ParticlesTest() throws SlickException {
		super("Particles Test");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		initRessources();
		ParticlesWorld inGameState = new ParticlesWorld(0,container);
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ME.keyToggleDebug = Input.KEY_1;
			AppGameContainer container = new AppGameContainer(new ParticlesTest());
			container.setDisplayMode(800, 600, false);
			container.setTargetFrameRate(60);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
