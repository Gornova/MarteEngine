package it.randomtower.test.entity;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import it.randomtower.engine.RessourceManager;
import it.randomtower.engine.entity.Entity;

/**
 * some smart test entity that can rotate and scale and change alpha and move
 * where most of it is done via alarms
 * @author Thomas Haaks, www.rightanglegames.com
 *
 */
public class AngleAlphaScaleMoveEntity extends Entity {

	public AngleAlphaScaleMoveEntity(float x, float y, boolean changeAngle, boolean changeAlpha, boolean changeScale, boolean move) {
		super(x, y);
		
		// load and get the image that we are showing
		try {
			RessourceManager.loadImage("ship", "data/triangle.png", null);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setGraphic(RessourceManager.getImage("ship"));
		
		if (changeAngle) {
			// set an alarm named "rotateMe" that is triggered every 2 update calls, starts right now
			// and runs for ever
			this.setAlarm("rotateMe", 2, false, true);
		}
	}
	
	@Override
	public void alarmTriggered(String alarmName) {
		if ("rotateMe".equals(alarmName)) {
			// let's rotate a bit
			this.angle = this.angle + 2;
			if (angle >= 360)
				angle -= 360;
		}
	}

}
