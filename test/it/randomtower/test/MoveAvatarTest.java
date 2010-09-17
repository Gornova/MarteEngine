package it.randomtower.test;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import it.randomtower.engine.*;

public class MoveAvatarTest extends BasicGame {

    public static int keyRestart = Input.KEY_R;

    public MoveAvatarTest() {
	super("Move Avatar Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
	ME.container = container;
	// create player
	TopDownActor player = new TopDownActor(400, 400,"data/link.png");
	// create sword relative to player
	Sword sword = new Sword(player.x, player.x, "data/sword.png", player);
	// create temple
	StaticActor temple = new StaticActor(150, 150,48,48,"data/tiles.png",0,6);

	// add entities
	ME.add(player);
	ME.add(temple);
	ME.add(sword);

	// set screen camera
	ME.setCamera(new Camera(player,container.getWidth(),ME.container.getHeight()));
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
	//ME.scale(3, 3);
	ME.render(container, g);
    }

    public static void main(String[] argv) {
	try {
	    ME.keyToggleDebug = Input.KEY_1;
	    AppGameContainer container = new AppGameContainer(
		    new MoveAvatarTest());
	    container.setDisplayMode(800, 600, false);
	    container.setTargetFrameRate(60);
	    container.start();
	} catch (SlickException e) {
	    e.printStackTrace();
	}
    }

}
