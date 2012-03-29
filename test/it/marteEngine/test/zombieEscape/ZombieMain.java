package it.marteEngine.test.zombieEscape;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Zombie Escape
 * 
 * @author Gornova
 */
public class ZombieMain extends StateBasedGame {
	public static final int GAME_STATE = 1;
	public static AngelCodeFont font;

	public ZombieMain() {
		super("Zombie Escape MarteEngine");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		ResourceManager.loadResources("data/zombie/resources.xml");
		addState(new ZombieWorld(GAME_STATE));
	}

	public static void main(String[] argv) {
		try {
			ME.keyToggleDebug = Input.KEY_1;
			ME.keyRestart = Input.KEY_R;
			ME.keyMuteMusic = Input.KEY_M;
			ME.debugEnabled = true;
			AppGameContainer container = new AppGameContainer(new ZombieMain());
			container.setDisplayMode(800, 600, false);
			container.setTargetFrameRate(60);
			container.setShowFPS(false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
