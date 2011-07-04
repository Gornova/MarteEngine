package it.marteEngine.test.zombieEscape;


import it.marteEngine.ME;
import it.marteEngine.ResourceManager;

import java.io.IOException;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

/**
 * Zombie Escape
 * 
 * @author Gornova
 */
public class ZombieMain extends StateBasedGame {

	public static final int MENU_STATE = 0;
	public static final int GAME_STATE = 1;

	private static boolean ressourcesInited;
	
	public static AngelCodeFont font;


	public ZombieMain() {
		super("Zombie Escape MarteEngine");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		initRessources();
		
		ZombieWorld world = new ZombieWorld(GAME_STATE);
		// make the world a bit bigger than the screen to force camera scrolling
		world.setWidth(2000);
		world.setHeight(2000);	
		
		ME.world = world;
		
		addState(world);
		
		
	}

	public static void initRessources() throws SlickException {
		if (ressourcesInited)
			return;
		try {
			ResourceManager
					.loadResources("data/zombie/resources.xml");
		} catch (IOException e) {
			Log.error("failed to load ressource file 'data/zombie/resources.xml': "
					+ e.getMessage());
			throw new SlickException("Resource loading failed!");
		}

		ressourcesInited = true;
	}

	public static void main(String[] argv) {
		try {
			ME.keyToggleDebug = Input.KEY_1;
			ME.keyRestart = Input.KEY_R;
			ME.keyMuteMusic = Input.KEY_M;
			ME.debugEnabled = true;
			AppGameContainer container = new AppGameContainer(
					new ZombieMain());
			container.setDisplayMode(800, 600, false);
			container.setTargetFrameRate(60);
			container.setShowFPS(false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
