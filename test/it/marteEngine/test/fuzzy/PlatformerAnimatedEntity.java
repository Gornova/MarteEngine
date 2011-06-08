package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.entity.PlatformerEntity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class PlatformerAnimatedEntity extends PlatformerEntity {

	private boolean faceRight = true;
	private Sound jumpSnd;
	
	public PlatformerAnimatedEntity(float x, float y, String ref)
			throws SlickException {
		super(x, y, ref);
		addAnimation(ResourceManager.getSpriteSheet("left"), "left", true, 0, 0, 1, 2, 3);
		addAnimation(ResourceManager.getSpriteSheet("right"), "right", true, 0, 0, 1, 2, 3);
		
		addType(PLAYER);
		
		jumpSnd = ResourceManager.getSound("jump");
	}
	
	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		currentAnim = faceRight ? "right" : "left";
		
		super.render(container, g);
	}
	
	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);
		
		if (check(CMD_LEFT)) {
			faceRight = false;
		}
		if (check(CMD_RIGHT)) {
			faceRight = true;
		}
		
		if (speed.y < 0 && !jumpSnd.playing()){
			jumpSnd.play();
		}
	}
	
	@Override
	public void leftWorldBoundaries() {
		if (y > 0){
			ME.world.remove(this);
			ScrollingPlatformerGameWorld.playerDead = true;
		}
	}

}
