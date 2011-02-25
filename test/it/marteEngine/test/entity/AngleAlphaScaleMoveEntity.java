package it.marteEngine.test.entity;

import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;

import org.newdawn.slick.SlickException;

/**
 * some smart test entity that can rotate and scale and change alpha and move
 * where most of it is done via alarms
 * @author Thomas Haaks, www.rightanglegames.com
 *
 */
public class AngleAlphaScaleMoveEntity extends Entity {
	private float scaleDir = -0.1f;
	private float alphaDir = -0.05f;
	
	private String[] rotatingPlayers = {"Player", "ScaledPlayer", "RotatingAndAlphaPlayer", "RotatingAndScalingAndAlphaPlayer" };
	private String[] scalingPlayers = {"ScaledPlayer", "RotatingAndScalingAndAlphaPlayer" };
	private String[] alphaPlayers = {"RotatingAndAlphaPlayer", "RotatingAndScalingAndAlphaPlayer" };

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
		this.setHitBox(0, 0, width, height);
		
		if (changeAngle) {
			// set an alarm named "rotateMe" that is triggered every 2 update calls, starts right now
			// and runs for ever
			this.setAlarm("rotateMe", 2, false, true);
		}
		
		if (changeScale) {
			this.setAlarm("scaleMe", 10, false, true);
		}
		
		if (changeAlpha) {
			this.setAlarm("alphaMe", 5, false, true);
		}
		this.setCentered(true);
	}
	
	@Override
	public void alarmTriggered(String alarmName) {
		if ("rotateMe".equals(alarmName)) {
			// let's rotate a bit
			int angle = this.getAngle();
			angle += 2;
			if (angle >= 360)
				angle -= 360;
			this.setAngle(angle);
			
			// we are playing tricks here: to avoid modifying the TopDownActor class we
			// retrieve the players from the world and rotate them too
			for (String name : rotatingPlayers) {
				Entity player = world.find(name);
				if (player != null)
					player.setAngle(this.getAngle());
			}
		} else if ("scaleMe".equals(alarmName)) {
			this.scale += scaleDir;
			if (this.scale <= 0.1f || this.scale >= 2.0f)
				scaleDir *= -1;
			
			for (String name : scalingPlayers) {
				Entity player = world.find(name);
				if (player != null)
					player.scale = this.scale;
			}
		} else if ("alphaMe".equals(alarmName)) {
			float alpha = this.getAlpha();
			this.setAlpha(alpha+alphaDir);
			alpha = this.getAlpha();
			if (alpha <= 0.1f || alpha >= 1.0f)
				alphaDir = -alphaDir;
			
			for (String name : alphaPlayers) {
				Entity player = world.find(name);
				if (player != null)
					player.setAlpha(this.getAlpha());
			}
		}
	}

}
