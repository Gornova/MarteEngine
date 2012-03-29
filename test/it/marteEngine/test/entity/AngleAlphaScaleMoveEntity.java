package it.marteEngine.test.entity;

import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;

/**
 * some smart test entity that can rotate and scale and change alpha and move
 * where most of it is done via alarms
 * 
 * @author Thomas Haaks, www.rightanglegames.com
 */
public class AngleAlphaScaleMoveEntity extends Entity {
	private static final int ROTATION_SPEED = 2;
	private float scaleDir = 0.1f;
	private float alphaDir = 0.05f;

	private String[] rotatingPlayers = {"Player", "ScaledPlayer",
			"RotatingAndAlphaPlayer", "RotatingAndScalingAndAlphaPlayer"};
	private String[] scalingPlayers = {"ScaledPlayer",
			"RotatingAndScalingAndAlphaPlayer"};
	private String[] alphaPlayers = {"RotatingAndAlphaPlayer",
			"RotatingAndScalingAndAlphaPlayer"};

	public AngleAlphaScaleMoveEntity(float x, float y, boolean changeAngle,
			boolean changeAlpha, boolean changeScale) {
		super(x, y);

		setGraphic(ResourceManager.getImage("triangle"));
		setHitBox(0, 0, width, height);

		if (changeAngle) {
			// set an alarm named "rotateMe" that is triggered every 2 update
			// calls, starts right now
			// and runs for ever
			addAlarm("rotateMe", 2, false);
		}

		if (changeScale) {
			addAlarm("scaleMe", 10, false);
		}

		if (changeAlpha) {
			addAlarm("alphaMe", 5, false);
		}
		setCentered(true);
	}

	@Override
	public void alarmTriggered(String alarmName) {
		if ("rotateMe".equals(alarmName)) {
			// let's rotate a bit
			int angle = getAngle();
			angle += ROTATION_SPEED;
			if (angle >= 360) {
				angle -= 360;
			}
			setAngle(angle);

			// we are playing tricks here: to avoid modifying the TopDownActor
			// class we retrieve the players from the world and rotate them too
			for (String name : rotatingPlayers) {
				Entity player = world.find(name);
				player.setAngle(getAngle());
			}
		} else if ("scaleMe".equals(alarmName)) {
			float scale = this.scale;
			if (scale <= 0.1f || scale >= 2.0f) {
				scaleDir *= -1;
			}
			this.scale += scaleDir;

			for (String name : scalingPlayers) {
				Entity player = world.find(name);
				player.scale = this.scale;
			}
		} else if ("alphaMe".equals(alarmName)) {
			float alpha = this.getAlpha();
			if (alpha <= 0.1f || alpha >= 1.0f) {
				alphaDir = -alphaDir;
			}

			setAlpha(getAlpha() + alphaDir);
			for (String name : alphaPlayers) {
				Entity player = world.find(name);
				player.setAlpha(this.getAlpha());
			}
		}
	}

}
