package it.marteEngine.test.scrollingPlatformer;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class Star extends Entity {

	public static final String STAR = "star";

	public Star(float x, float y) {
		super(x, y, ResourceManager.getImage("star"));
		setHitBox(0, 0, currentImage.getWidth(), currentImage.getHeight());
		
		addType(STAR);
	}
	
	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);
		
		if (collide(PLAYER,x,y)!=null){
			ME.world.remove(this);
		}
	}

}
