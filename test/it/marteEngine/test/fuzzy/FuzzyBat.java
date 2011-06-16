package it.marteEngine.test.fuzzy;

import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;
import it.marteEngine.entity.PhysicsEntity;
import it.marteEngine.tween.Ease;
import it.marteEngine.tween.NumTween;
import it.marteEngine.tween.Tween.TweenerMode;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class FuzzyBat extends Entity {

	public static final String BAT = "bat";

	private float moveSpeed = 1;

	protected boolean faceRight = true;

	private NumTween fadeTween = new NumTween(1, 0, 10, TweenerMode.ONESHOT,
			Ease.CUBE_OUT, false);

	private boolean fade;

	public FuzzyBat(float x, float y) throws SlickException {
		super(x, y);
		addAnimation(ResourceManager.getSpriteSheet("batLeft"), "move", true, 0,
				0, 1, 2 );
		addType(BAT,SOLID);
		setHitBox(0, 0, 32, 32);
		// make Slime sloow
		//maxSpeed.x = 1;
		speed.x = 1;
	}
	
	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);
		
		if (faceRight && collide(SOLID,x+1,y)!=null){
			faceRight = false;
		} else if (!faceRight && collide(SOLID,x-1,y)!=null){
			faceRight = true;
		}

		if (faceRight){
			speed.x = 1;
		} else {
			speed.x = -1;
		}
	}
	

}
