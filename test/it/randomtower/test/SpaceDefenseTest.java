package it.randomtower.test;
import it.randomtower.engine.AIControlledSpaceShip;
import it.randomtower.engine.Bullet;
import it.randomtower.engine.ME;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class SpaceDefenseTest extends BasicGame {

    public static int keyRestart = Input.KEY_R;

    public SpaceDefenseTest() {
	super("Space Defense Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
	ME.container = container;
	// create player
	AIControlledSpaceShip ai = new AIControlledSpaceShip(50, container.getHeight()/2, "data/ball.png");
	
	//fireBullet(container);
	
	ME.add(ai);

    }

    public void fireBullet(GameContainer container,float y) {
	//TODO: 
	//x =  container.getWidth()
	//y = container.getHeight()/2
	float x = 100;
	Bullet bullet = new Bullet(x,y , "data/ball.png", new Vector2f(-1, 0));
	bullet.fireSpeed = 2;
	ME.add(bullet);
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
	if (button == 0){
	    fireBullet(ME.container, y);
	}
    }
    
    @Override
    public void update(GameContainer container, int delta)
	    throws SlickException {
	ME.update(container, delta);
	if (keyRestart != -1){
	    if (container.getInput().isKeyPressed(keyRestart)) {
		ME.clear();
		init(container);
	    }
	}
    }

    public void render(GameContainer container, Graphics g)
	    throws SlickException {
	ME.render(container, g);
    }

    public static void main(String[] argv) {
	try {
	    ME.keyToggleDebug = Input.KEY_1;
	    AppGameContainer container = new AppGameContainer(
		    new SpaceDefenseTest());
	    container.setDisplayMode(800, 600, false);
	    container.setTargetFrameRate(60);
	    container.start();
	} catch (SlickException e) {
	    e.printStackTrace();
	}
    }

}
