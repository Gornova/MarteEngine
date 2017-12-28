package it.marteEngine.test.pong;

import it.marteEngine.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class PongIntroState extends World {

	public PongIntroState(int id) {
		super(id);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// there is no entity here, so i don't need to render anything

		g.scale(6, 6);
		g.drawString("PONG!", 25, 5);
		g.resetTransform();

		g.drawString("PRESS SPACE TO START or R to restart, ESC return here ",
				100, 250);
		g.drawString("First player reach 5 points wins!", 100, 300);

		g.drawString("LEFT PLAYER CONTROLS ARE W and S", 100, 350);
		g.drawString("RIGHT PLAYER CONTROLS ARE UP ARROW and DOWN ARROW", 100,
				400);

		g.drawString("2010 - http://randomtower.blogspot.com", 100,
				container.getHeight() - 40);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// there is no entity here, so i don't need to update anything
		if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			game.enterState(PongTest.GAME_STATE);
		}
	}

}
