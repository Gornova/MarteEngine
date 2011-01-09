package it.randomtower.engine.tween;

import it.randomtower.engine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.Log;

public class Tween {

	private LinearMotion motion;
	private Entity parent;

	public Tween(Entity parent, LinearMotion motion) {
		this.parent = parent;
		this.motion = motion;
	}

	public void update(GameContainer container, int delta)
			throws SlickException {
		if (!motion.completed) {
			Vector2f result = motion.update();
			Log.debug("move to :" + result.toString());
			parent.x = result.x;
			parent.y = result.y;
		}
	}

}
