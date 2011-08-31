package it.marteEngine.tween;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tweener is a utility container for variuos tweens that can change Entity in
 * many ways
 * 
 * @author Gornova
 */
public class Tweener {

	private List<Tween> tweens = new ArrayList<Tween>();
	private List<Tween> removeable = new ArrayList<Tween>();

	private boolean active = true;

	public Tweener(Tween... tweens) {
		if (tweens != null) {
			List<Tween> allTweens = Arrays.asList(tweens);
			this.tweens.addAll(allTweens);
			for (Tween tween : allTweens) {
				tween.setParent(this);
			}
		}
	}

	public boolean add(Tween tween) {
		return add(tween, true);
	}

	public boolean add(Tween tween, boolean start) {
		if (tween != null && tween.getParent() == null) {
			boolean result = tweens.add(tween);
			tween.setActive(true);
			return result;
		}
		return false;
	}

	public boolean remove(Tween tween) {
		if (tween == null || tween.getParent() != this)
			return false;
		removeable.add(tween);
		tween.setActive(false);
		tween.setParent(null);
		return true;
	}

	public void clearTweens() {
		for (Tween tween : tweens) {
			tween.setActive(false);
			tween.setParent(null);
		}
		tweens = new ArrayList<Tween>();
	}

	public Tween getTween(String name) {
		if (name == null || name.isEmpty())
			return null;
		for (Tween tween : tweens) {
			if (tween.getName() != null && tween.getName().equals(name))
				return tween;
		}
		return null;
	}

	public Tween getTween(int index) {
		if (index < 0 || index >= tweens.size())
			return null;
		return tweens.get(index);
	}

	public void update(int delta) {
		if (!tweens.isEmpty()) {
			for (Tween tween : tweens) {
				if (tween.isActive())
					tween.update(delta);
				if (tween.isFinished())
					tween.finish();
			}
			tweens.removeAll(removeable);
			removeable.clear();
		}
	}

	public void start() {
		for (Tween tween : tweens) {
			tween.reset();
			tween.setActive(true);
		}
	}

	public void pause() {
		for (Tween tween : tweens) {
			tween.setActive(false);
		}
	}

	public void reset() {
		this.start();
	}

}
