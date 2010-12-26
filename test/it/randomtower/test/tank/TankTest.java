package it.randomtower.test.tank;

import it.randomtower.engine.ME;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class TankTest extends StateBasedGame {

	public static int keyRestart = Input.KEY_R;

	public TankTest() {
		super("Tank Test");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		TankGame inGameState = new TankGame(0,container);
		addState(inGameState);
		
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
