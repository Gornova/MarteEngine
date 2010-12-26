package it.randomtower.test.entity;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import it.randomtower.engine.ResourceManager;
import it.randomtower.engine.entity.Entity;

/**
 * some smart test entity that can rotate and scale and change alpha and move
 * where most of it is done via alarms
 * @author Thomas Haaks, www.rightanglegames.com
 *
 */
public class AngleAlphaScaleMoveEntity extends Entity {
	private float scaleDir = -0.1f;

	public AngleAlphaScaleMoveEntity(float x, float y, boolean changeAngle, boolean changeAlpha, boolean changeScale, boolean move) {
		super(x, y);
		
		// load and get the image that we are showing
		if (ResourceManager.getImage("ship") == null) {
			try {
				ResourceManager.loadImage("ship", "data/triangle.png", null);
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.setGraphic(ResourceManager.getImage("ship"));
		
		if (changeAngle) {
			// set an alarm named "rotateMe" that is triggered every 2 update calls, starts right now
			// and runs for ever
			this.setAlarm("rotateMe", 2, false, true);
		}
		
		if (changeScale) {
			this.setAlarm("scaleMe", 10, false, true);
		}
		this.setCentered(true);
	}
	
	@Override
	public void alarmTriggered(String alarmName) {
		if ("rotateMe".equals(alarmName)) {
			// let's rotate a bit
			this.angle = this.angle + 2;
			if (angle >= 360)
				angle -= 360;
			
			// we are playing tricks here: to avoid modifying the TopDownActor class we
			// retrieve the players from the world and rotate them too
			Entity player = world.find(PLAYER);
			if (player != null)
				player.angle = this.angle;
			Entity scaledPlayer = world.find("ScaledPlayer");
			if (scaledPlayer != null)
				scaledPlayer.angle = this.angle;
		} else if ("scaleMe".equals(alarmName)) {
			this.scale += scaleDir;
			if (this.scale <= 0.1f || this.scale >= 2.0f)
				scaleDir *= -1;
			
			Entity scaledPlayer = world.find("ScaledPlayer");
			if (scaledPlayer != null)
				scaledPlayer.scale = this.scale;
		}
	}

}
