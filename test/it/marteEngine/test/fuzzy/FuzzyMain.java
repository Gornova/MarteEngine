package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.resource.ResourceManager;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Scrolling Platformer Test
 * 
 * @author Gornova
 */
public class FuzzyMain extends StateBasedGame {

	public static final int MENU_STATE = 0;
	public static final int GAME_STATE = 1;
	public static final int WIN_STATE = 2;
	public static final int SELECT_STATE = 3;

	public static Font font;
	public static Integer gotoLevel = -1;

	public FuzzyMain() {
		super("Fuzzy");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		ResourceManager.loadResources("data/fuzzy/resources.xml");

		font = ResourceManager.getFont("font");

		FuzzyGameWorld gameState = new FuzzyGameWorld(GAME_STATE);
		FuzzyMenuWorld menuState = new FuzzyMenuWorld(MENU_STATE);
		FuzzyWinWorld winState = new FuzzyWinWorld(WIN_STATE);
		FuzzySelectLevelWorld selectState = new FuzzySelectLevelWorld(
				SELECT_STATE);

		addState(menuState);
		addState(gameState);
		addState(winState);
		addState(selectState);
	}

	public static void main(String[] argv) throws SlickException {
			// ME.keyToggleDebug = Input.KEY_1;
			// ME.keyRestart = Input.KEY_R;
			ME.keyMuteMusic = Input.KEY_M;
			ME.keyFullScreen = Input.KEY_F12;
			ME.playMusic = true;
			AppGameContainer container = new AppGameContainer(new FuzzyMain());
			container.setDisplayMode(640, 480, false);
			container.setTargetFrameRate(50);
			container.setShowFPS(false);
			container.start();
	}
}
