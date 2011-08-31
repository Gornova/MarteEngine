package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.World;
import it.marteEngine.entity.Entity;
import it.marteEngine.entity.TextEntity;
import it.marteEngine.game.starcleaner.Background;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class FuzzyMenuWorld extends World {

	public static boolean gotoGame = false;

	private Music music;

	private boolean showContinue;

	public FuzzyMenuWorld(int id) {
		super(id);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.init(container, game);
		add(new Background(0, 0, ResourceManager.getImage("menu")));
		add(new Space(220, 400));
		add(new TextEntity(160, 100, ResourceManager.getAngelCodeFont("font"),
				"Beta demo 2"));

		music = ResourceManager.getMusic("song0");

		int levelSave = FuzzyUtil.loadLevel();
		if (levelSave != -1) {
			showContinue = true;
		}

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
			game.enterState(FuzzyMain.GAME_STATE, new FadeOutTransition(),
					new FadeInTransition());
		}

		if (ME.playMusic && !music.playing()) {
			music.play();
		}
	}

	private class Space extends Entity {

		private boolean up = false;

		public Space(float x, float y) {
			super(x, y);
		}

		@Override
		public void addedToWorld() {
			setAlarm("jump", 15, false, true);
		}

		@Override
		public void render(GameContainer container, Graphics g)
				throws SlickException {
			super.render(container, g);

			if (!showContinue) {
				drawScaled(g, 0.6f, "Press Space to start", x, y);
			} else {
				drawScaled(g, 0.6f, "Press Space to continue", x, y);
			}

		}

		@Override
		public void alarmTriggered(String name) {
			if (name.equalsIgnoreCase("jump")) {
				up = up ? false : true;
			}
		}

		@Override
		public void update(GameContainer container, int delta)
				throws SlickException {
			super.update(container, delta);

			if (up) {
				y--;
			} else {
				y++;
			}

			if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
				FuzzyMenuWorld.gotoGame = true;
			}
		}

	}

}
