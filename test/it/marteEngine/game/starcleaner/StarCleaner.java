package it.marteEngine.game.starcleaner;

import it.marteEngine.ResourceManager;

import java.io.IOException;

import it.marteEngine.SFX;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

public class StarCleaner extends StateBasedGame {

	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	public static final int TILESIZE = 40;

	// static finals for the game state ids, defined here to avoid duplicate
	// gamestate definitions
	public static final int TITLE_STATE = 1;
	public static final int INGAME_STATE = 2;
	public static final int NEXTLEVEL_STATE = 3;
	public static final int GAMEOVER_STATE = 4;
	public static final int CREDITS_STATE = 5;
	public static final int INSTRUCTIONS_STATE = 6;
	public static final int OPTIONS_STATE = 7;

	/** volume for SFX and music */
	public static float musicVolume = 1.0f;
	public static float sfxVolume = 1.0f;

	public static boolean wizardMode = false;

	public static boolean ressourcesInited = false;

	private AppGameContainer container;

	public StarCleaner() {
		super("Star Cleaner");
		Globals.game = this;
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		if (container instanceof AppGameContainer) {
			this.container = (AppGameContainer) container;
		}
		addState(new TitleState(TITLE_STATE));
		// addState(new CreditsState());
		// addState(new InstructionsState());
		addState(new IngameState(INGAME_STATE));
		// addState(new NextLevelState());
		// addState(new GameOverState());
		// addState(new OptionsState());

		applyGameOptions();
	}

	/**
	 * @see org.newdawn.slick.state.StateBasedGame#keyPressed(int, char)
	 */
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);

		if (key == Input.KEY_F2) {
			if (container != null) {
				try {
					container.setFullscreen(!container.isFullscreen());
					if (container.isFullscreen())
						container.setMouseGrabbed(true);
				} catch (SlickException e) {
					Log.error(e);
				}
			}
		}
	}

	private void applyGameOptions() {
		SFX.setMusicVolume(1.0f);
		SFX.setSoundVolume(1.0f);
	}

	public static void initRessources() throws SlickException {
		if (ressourcesInited)
			return;
		try {
			ResourceManager.loadResources("data/starcleaner/res/resources.xml");
		} catch (IOException e) {
			Log.error("failed to load resource file 'data/starcleaner/res/resources.xml': "
					+ e.getMessage());
			throw new SlickException("Resource loading failed!");
		}

		ressourcesInited = true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 1 && args[0].equals("wizard"))
			wizardMode = true;

		// ME.debugEnabled = true;

		try {
			// Log.setVerbose(false);
			AppGameContainer container = new AppGameContainer(new StarCleaner());
			container.setDisplayMode(WIDTH, HEIGHT, false);
			// container.setShowFPS(false);
			// switch off mouse cursor
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
