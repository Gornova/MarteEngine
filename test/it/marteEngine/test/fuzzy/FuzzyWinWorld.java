package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.World;
import it.marteEngine.game.starcleaner.Background;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class FuzzyWinWorld extends World {

	private boolean gotoMenu;

	public FuzzyWinWorld(int id) {
		super(id);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.init(container, game);
		add(new Background(0, 0, ResourceManager.getImage("menu")));
		add(new FuzzySpaceEntity(180, 400, true));
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		gotoMenu = false;
		ME.renderParticle = false;
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.leave(container, game);

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		super.render(container, game, g);

		FuzzyMain.font.drawString(40, 20, "Congratulations!");
		FuzzyMain.font.drawString(120, 120, "You have win!!");

		drawScaled(g, 0.5f, "http://randomtower.blogspot.com", 200, 880);
	}

	private void drawScaled(Graphics g, float scale, String text, float x,
			float y) {
		g.scale(scale, scale);
		FuzzyMain.font.drawString(x, y, text);
		g.resetTransform();
	}

	@Override
	public void keyPressed(int key, char c) {
		if (Input.KEY_SPACE == key) {
			gotoMenu = true;
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		super.update(container, game, delta);

		if (gotoMenu) {
			game.enterState(FuzzyMain.MENU_STATE, new FadeOutTransition(),
					new FadeInTransition());
			return;
		}
	}

}
