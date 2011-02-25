package it.marteEngine.tween;

import it.marteEngine.entity.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.Log;

/**
 * Tweener is a utility container for variuos tween that can change Entity in many ways
 * 
 * @author Gornova
 */
public class Tweener {

	public int MODE_DELETE = 0;

	private List<Tween> tweens = new ArrayList<Tween>();

	private int current = -1;

	private int mode = 0;

	public Tweener(Tween... tweens) {
		if (tweens != null) {
			this.tweens.addAll(Arrays.asList(tweens));
			current = this.tweens.size() - 1;
		}
	}

	public boolean add(Tween tween) {
		if (tween != null) {
			boolean result = tweens.add(tween);
			if (current <= 0) {
				current = tweens.size() - 1;
			}
			return result;
		}
		return false;
	}

	public Vector2f apply(Entity parent) {
		if (!tweens.isEmpty() && tweens.get(current) != null) {
			Vector2f value = tweens.get(current).apply(parent);
			if (value == null) {
				if (mode == 0) {
					tweens.remove(current);
					current = tweens.size() - 1;
					if (current >= 0){
						tweens.get(current).setStartPosition(parent.previousx, parent.previousy);
					}
					Log.debug("Current Tweener size: " + tweens.size());
				}
			}
			return value;
		}
		return null;
	}

	public void start() {
		if (!tweens.isEmpty() && tweens.get(current) != null) {
			tweens.get(current).start();
		}
	}

	public void pause() {
		if (!tweens.isEmpty() && tweens.get(current) != null) {
			tweens.get(current).pause();
		}		
	}

	public Vector2f reset() {
		if (!tweens.isEmpty() && tweens.get(current) != null) {
			return tweens.get(current).reset();
		}
		return null;
	}

}
