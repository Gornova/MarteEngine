package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Star extends Entity {

	public static final String STAR = "star";
	private Sound pickupstar;
	private RingEmitter emitter;
	private boolean emit = false;

	public Star(float x, float y) {
		super(x, y, ResourceManager.getImage("star"));
		setHitBox(0, 0, currentImage.getWidth(), currentImage.getHeight());
		
		addType(STAR);
		
		pickupstar = ResourceManager.getSound("pickupstar");
		emitter = new RingEmitter((int)x+width/2, (int)y+height/2);
	}
	
	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);
		
		if (!emit && collide(PLAYER,x,y)!=null){
			//ME.world.remove(this);
			if (!pickupstar.playing()){
				pickupstar.play();
			}
			emit = true;
			ME.ps.addEmitter(emitter);
			setAlarm("stopEmit", 60, false, true);
		}
	}
	
	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		if (!emit){
			super.render(container, g);
		} 
	}
	
	@Override
	public void alarmTriggered(String name) {
		if (name.equalsIgnoreCase("stopEmit")){
			ME.ps.removeEmitter(emitter);
			ME.world.remove(this);
		}
	}

}
