package it.marteEngine.test.fuzzy;

import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class FuzzyBat extends Entity {

	public static final String BAT = "bat";

	private float moveSpeed = 3;

	protected boolean faceRight = true;

	public FuzzyBat(float x, float y) throws SlickException {
		super(x, y);
		addAnimation(ResourceManager.getSpriteSheet("batLeft"), "moveLeft", true, 0,
				0, 1, 2 );
		addAnimation(ResourceManager.getSpriteSheet("batRight"), "moveRight", true, 0,
				0, 1, 2 );		
		addType(BAT,SOLID);
		setHitBox(0, 0, 32, 32);
		speed.x = moveSpeed;
		currentAnim = "moveRight"; 
	}
	
	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);
		
		if (faceRight && collide(SOLID,x+1,y)!=null){
			faceRight = false;
			currentAnim = "moveLeft";
		} else if (!faceRight && collide(SOLID,x-1,y)!=null){
			faceRight = true;
			currentAnim = "moveRight";
		}

		if (faceRight){
			speed.x = moveSpeed; 
		} else {
			speed.x = moveSpeed * -1;
		}
	}
	

}
