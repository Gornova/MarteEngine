package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.World;
import it.marteEngine.game.starcleaner.Background;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class FuzzyMenuWorld extends World {

	public static boolean gotoGame = false;

	public static Music music;

	private boolean showContinue;

	public FuzzyMenuWorld(int id) {
		super(id);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.init(container, game);

		music = ResourceManager.getMusic("song0");

		add(new Background(0, 0, ResourceManager.getImage("menu")));
		add(new FuzzySpaceEntity(180, 400, showContinue));
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		gotoGame = false;
		if (ME.playMusic) {
			music.play();
			music.setVolume(0.5f);
		}
		ME.renderParticle = false;
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.leave(container, game);

		if (music.playing()) {
			music.stop();
		}

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		super.render(container, game, g);

		FuzzyMain.font.drawString(90, 50, "Fuzzy Platformer");

		drawScaled(g, 0.5f, "http://randomtower.blogspot.com", 200, 880);
	}

	private void drawScaled(Graphics g, float scale, String text, float x,
			float y) {
		g.scale(scale, scale);
		FuzzyMain.font.drawString(x, y, text);
		g.resetTransform();
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		super.update(container, game, delta);

		if (gotoGame) {
			game.enterState(FuzzyMain.SELECT_STATE, new FadeOutTransition(),
					new FadeInTransition());
		}

		if (ME.playMusic && !music.playing()) {
			music.play();
		}
	}

}
