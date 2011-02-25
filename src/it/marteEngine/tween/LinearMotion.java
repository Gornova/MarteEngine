package it.marteEngine.tween;

import org.newdawn.slick.geom.Vector2f;

public class LinearMotion extends Motion {

	public LinearMotion(float fromX, float fromY, float toX, float toY,
			int duration, int easeFunction) {
		super(fromX,fromY,toX,toY,duration,easeFunction);
	}

	public Vector2f update() {
		super.update();

		Vector2f result = new Vector2f(lastX, lastY);

		float newX = lastX = fromX * (1 - t) + toX * (t);
		if (Math.abs(toX - newX) >= 0) {
			result.x = newX;
		}
		float newY = lastY = fromY * (1 - t) + toY * (t);
		if (Math.abs(toY - newY) >= 0) {
			result.y = newY;
		}

		return result;
	}

}
