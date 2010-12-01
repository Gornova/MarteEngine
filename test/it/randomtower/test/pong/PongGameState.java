package it.randomtower.test.pong;

import it.randomtower.engine.ME;
import it.randomtower.engine.World;
import it.randomtower.engine.actors.StaticActor;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class PongGameState extends World {

	/**
	 * Timer for a new ball
	 */
	private int newBallTimer = 0;

	private int lastDir = 1;

	/**
	 * Points for victory
	 */
	private static final int VICTORY = 5;

	private boolean victory = false;

	public PongGameState(int id) {
		super(id);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

		super.init(container, game);
		// create players
		add(new PongBarActor(20, container.getHeight() / 2, "data/bar.PNG",
				"player1", Input.KEY_W, Input.KEY_S));
		add(new PongBarActor(container.getWidth() - 26,
				container.getHeight() / 2, "data/bar.png", "player2",
				Input.KEY_UP, Input.KEY_DOWN));
		add(new StaticActor(0, 0, container.getWidth(), 1, null));
		add(new StaticActor(0, container.getHeight(), container.getWidth(), 1,
				null));

		newBallTimer = 0;

		resetScore();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		super.render(container, game, g);

		if (victory) {
			int score1 = (Integer) ME.attributes.get("score1");
			int score2 = (Integer) ME.attributes.get("score2");

			if (score1 >= VICTORY) {
				g.drawString("PLAYER 1 WINS!!", 150, 300);
			} else if (score2 >= VICTORY) {
				g.drawString("PLAYER 2 WINS!!", 150, 300);
			}

			g.drawString("PRESS SPACE TO play again!", 150, 400);
			return;
		}

		g.drawString("SCORE : " + ME.attributes.get("score1"), 50, 20);
		g.drawString("SCORE : " + ME.attributes.get("score2"),
				container.getWidth() - 150, 20);

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (victory) {
			if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
				victory = false;
				resetScore();
				game.enterState(PongTest.GAME_STATE);
			}
			return;
		}

		if (container.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			game.enterState(0);
		}

		// game victory stop check
		int score1 = (Integer) ME.attributes.get("score1");
		int score2 = (Integer) ME.attributes.get("score2");
		if (score1 >= VICTORY || score2 >= VICTORY) {
			victory = true;
		}

		super.update(container, game, delta);

		if (container.getInput().isKeyPressed(Input.KEY_R)) {
			leave(container, game);
		}
		// if there is no ball, add one after 2 seconds
		if (find(BallActor.NAME) == null && newBallTimer <= 0) {
			newBallTimer = 2000;
		}
		if (newBallTimer > 0) {
			newBallTimer -= delta;
			if (newBallTimer <= 0) {
				newBallTimer = 0;
				addNewBall(container);
			}
		}

	}

	private void resetScore() {
		ME.attributes.put("score1", 0);
		ME.attributes.put("score2", 0);
	}

	private void addNewBall(GameContainer container) {
		BallActor ball = new BallActor(container.getWidth() / 2,
				container.getHeight() / 2, "data/ball.png");
		if (lastDir > 0) {
			lastDir *= -1;
		} else {
			lastDir *= -1;
		}
		ball.mySpeed.x *= lastDir;
		add(ball);
	}

}
