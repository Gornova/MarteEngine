package it.randomtower.test;
import it.randomtower.engine.BallActor;
import it.randomtower.engine.ME;
import it.randomtower.engine.PongBarActor;
import it.randomtower.engine.StaticActor;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * Pong clone
 * 
 * @author RandomTower
 * @project MarteEngine
 */
public class PongTest extends BasicGame {

    /**
     * Points for victory
     */
    private static final int VICTORY = 5;

    /**
     * Timer for a new ball
     */
    private int newBallTimer = 0;

    private boolean start = true;

    private boolean victory = false;

    private int lastDir = 1;

    public PongTest() {
	super("Pong Slick Marte Engine clone 1.0");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
	ME.container = container;
	ME.clear();
	// create player
	ME.add(new PongBarActor(20, container.getHeight() / 2, "data/bar.PNG",
		"player1", Input.KEY_W, Input.KEY_S));
	ME.add(new PongBarActor(container.getWidth() - 26, container
		.getHeight() / 2, "data/bar.png", "player2", Input.KEY_UP,
		Input.KEY_DOWN));
	ME.add(new StaticActor(0, 0, container.getWidth(), 1, null));
	ME.add(new StaticActor(0, container.getHeight(), container.getWidth(),
		1, null));

	newBallTimer = 0;

	resetScore();
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
	ME.add(ball);
    }

    @Override
    public void update(GameContainer container, int delta)
	    throws SlickException {

	if (start) {
	    if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
		start = false;
	    }
	    return;
	}
	if (victory) {
	    if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
		victory = false;
		start = true;
		init(container);
	    }
	    return;
	}

	if (!start && container.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
	    start = true;
	}

	// game victory stop check
	int score1 = (Integer) ME.attributes.get("score1");
	int score2 = (Integer) ME.attributes.get("score2");
	if (score1 >= VICTORY || score2 >= VICTORY) {
	    victory = true;
	}

	ME.update(container, delta);
	if (container.getInput().isKeyPressed(Input.KEY_R)) {
	    init(container);
	}
	// if there is no ball, add one after 2 seconds
	if (ME.find(BallActor.NAME) == null && newBallTimer <= 0) {
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

    public void render(GameContainer container, Graphics g)
	    throws SlickException {

	if (start) {
	    g.scale(6, 6);
	    g.drawString("PONG!", 25, 5);
	    g.resetTransform();

	    g.drawString(
		    "PRESS SPACE TO START or R to restart, ESC return here ",
		    100, 250);
	    g.drawString("First player reach 5 points wins!", 100, 300);

	    g.drawString("LEFT PLAYER CONTROLS ARE W and S", 100, 350);
	    g.drawString("RIGHT PLAYER CONTROLS ARE UP ARROW and DOWN ARROW",
		    100, 400);

	    g.drawString("2010 - http://randomtower.blogspot.com", 100,
		    container.getHeight() - 40);

	    return;
	}
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

	ME.render(container, g);
    }

    public static void main(String[] argv) {
	try {
	    ME.keyToggleDebug = Input.KEY_1;
	    AppGameContainer container = new AppGameContainer(new PongTest());
	    container.setDisplayMode(640, 480, false);
	    container.setTargetFrameRate(60);
	    container.setShowFPS(false);
	    container.start();
	} catch (SlickException e) {
	    e.printStackTrace();
	}
    }

}
