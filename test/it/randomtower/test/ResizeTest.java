package it.randomtower.test;
import it.randomtower.engine.Bullet;
import it.randomtower.engine.Camera;
import it.randomtower.engine.ME;
import it.randomtower.engine.Map;
import it.randomtower.engine.ResizeSquare;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class ResizeTest extends BasicGame {

    public static int keyRestart = Input.KEY_R;

    public ResizeTest() {
	super("Resize Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
	ME.container = container;
	Image cursor = new Image("data/cross.png", false, 0, Color.white);

	container.setMouseCursor(cursor, 0, 0);
	// create square
	ResizeSquare square = new ResizeSquare(100, 100, 8, 8, "data/8.png");

	// add entities
	ME.add(square);

	new Map("data/level1.tmx");

	// set screen camera
	ME.setCamera(new Camera(square, container.getWidth(), ME.container
		.getHeight()));
    }

    @Override
    public void update(GameContainer container, int delta)
	    throws SlickException {
	ME.update(container, delta);
	if (keyRestart != -1) {
	    if (container.getInput().isKeyPressed(keyRestart)) {
		ME.clear();
		init(container);
	    }
	}
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
	if (button == 0) {
	    ResizeSquare rs = ((ResizeSquare)ME.find(ResizeSquare.NAME));
	    Bullet b = new Bullet(rs.x, rs.y, "data/bullet.png", calculateDirection(x, y,
		    ME.container.getInput().getMouseX(), ME.container
			    .getInput().getMouseY()));
	    ME.add(b);
	}
    }

    public Vector2f calculateDirection(float x, float y, int mousex, int mousey) {
	float rotation = ((ResizeSquare)ME.find(ResizeSquare.NAME)).rotation;
	float xForce = 10f * (float) Math.sin(Math.toRadians(rotation));
	float yForce = 10f * (float) Math.cos(Math.toRadians(rotation));
	return new Vector2f(-xForce, yForce);
    }    
    
    public void render(GameContainer container, Graphics g)
	    throws SlickException {
	ME.render(container, g);
    }

    public static void main(String[] argv) {
	try {
	    ME.keyToggleDebug = Input.KEY_1;
	    AppGameContainer container = new AppGameContainer(new ResizeTest());
	    container.setDisplayMode(800, 600, false);
	    container.setTargetFrameRate(60);
	    container.start();
	} catch (SlickException e) {
	    e.printStackTrace();
	}
    }

}
