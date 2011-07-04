package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;
import it.marteEngine.tween.Ease;
import it.marteEngine.tween.LinearMotion;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class FuzzyBat extends Entity {

	public static final String BAT = "bat";

	private float moveSpeed = 3;

	protected boolean faceRight = true;
	
	private Vector2f distanceFromPlayer = null;

	private LinearMotion motion;

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
//		distanceFromPlayer= getDistanceFromPlayer();
//
//		if (distanceFromPlayer!=null){
//			if (Math.abs(distanceFromPlayer.x) <=32*3 || Math.abs(distanceFromPlayer.y) <= 32*3 ){
//				if (motion==null){
//					setMoveTo(ME.world.find(Entity.PLAYER));
//				} else {
//					motion.update(delta);
//					if (!motion.isFinished()){
//						this.x = motion.getX();
//						this.y = motion.getY();
//					} else {
//						motion = null;
//					}
//					return ;
//				}
//			}
//		}
		
		
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
	
	private Vector2f getDistanceFromPlayer(){
		Entity p = ME.world.find(Entity.PLAYER);
		if (p==null){
			return null;
		}
		Vector2f distance = new Vector2f();
		distance.x = this.x - p.x;
		distance.y = this.y - p.y;
		return distance;
	}
	
	private void setMoveTo(Entity e){
		motion = new LinearMotion(this.x, this.y, e.x, e.y, 100, Ease.QUAD_OUT);
	}

}
