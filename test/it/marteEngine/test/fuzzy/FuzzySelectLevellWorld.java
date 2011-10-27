package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.World;
import it.marteEngine.entity.TextEntity;
import it.marteEngine.game.starcleaner.Background;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class FuzzySelectLevellWorld extends World {

	public static int gotoLevel = -1;

	public int completition = 0;

	public FuzzySelectLevellWorld(int id) {
		super(id);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.init(container, game);
		add(new Background(0, 0, ResourceManager.getImage("menu")));
		add(new TextEntity(160, 20, FuzzyMain.font, "Select Level"));
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);

		ME.renderParticle = false;
		checkLevelAndCompletition();
		FuzzyMain.gotoLevel = -1;

		int levelIndex = 0;
		int[] result = FuzzyUtil.loadLevel();
		if (result[0] != -1) {
			levelIndex = result[0];
		} else {
			levelIndex = 1;
		}

		add(new FuzzyLevelButton(40, 150, FuzzyMain.font, "1",
				levelIndex >= 1 ? true : false));
		add(new FuzzyLevelButton(160, 150, FuzzyMain.font, "2",
				levelIndex >= 2 ? true : false));
		add(new FuzzyLevelButton(280, 150, FuzzyMain.font, "3",
				levelIndex >= 3 ? true : false));
		add(new FuzzyLevelButton(400, 150, FuzzyMain.font, "4",
				levelIndex >= 4 ? true : false));
		add(new FuzzyLevelButton(520, 150, FuzzyMain.font, "5",
				levelIndex >= 5 ? true : false));

		add(new FuzzyLevelButton(40, 250, FuzzyMain.font, "6",
				levelIndex >= 6 ? true : false));
		add(new FuzzyLevelButton(160, 250, FuzzyMain.font, "7",
				levelIndex >= 7 ? true : false));
		add(new FuzzyLevelButton(280, 250, FuzzyMain.font, "8",
				levelIndex >= 8 ? true : false));
		add(new FuzzyLevelButton(400, 250, FuzzyMain.font, "9",
				levelIndex >= 9 ? true : false));
		add(new FuzzyLevelButton(520, 250, FuzzyMain.font, "10",
				levelIndex >= 10 ? true : false));

		add(new FuzzyLevelButton(240, 400, FuzzyMain.font, "Boss",
				levelIndex >= 11 ? true : false));

		if (!FuzzyMenuWorld.music.playing()) {
			FuzzyMenuWorld.music.play();
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		super.render(container, game, g);

		if (completition > 0) {
			drawScaled(g, 0.5f, completition + " %", 25, 880);
		}
		// if (deads != -1) {
		// drawScaled(g, 0.5f, "Deaths : " + deads, 900, 880);
		// }
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		super.update(container, game, delta);

		if (container.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			game.enterState(FuzzyMain.MENU_STATE, new FadeOutTransition(),
					new FadeInTransition());
		}

		if (FuzzyMain.gotoLevel > 0) {
			game.enterState(FuzzyMain.GAME_STATE, new FadeOutTransition(),
					new FadeInTransition());
		}
	}

	private void checkLevelAndCompletition() {
		int[] result = FuzzyUtil.loadLevel();
		if (result[0] != -1) {
			completition = (result[0] * 100) / 12;
		}
		// if (result[1] != -1) {
		// deads = result[1];
		// }
	}

	private void drawScaled(Graphics g, float scale, String text, float x,
			float y) {
		g.scale(scale, scale);
		FuzzyMain.font.drawString(x, y, text);
		g.resetTransform();
	}

}
