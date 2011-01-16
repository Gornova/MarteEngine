package it.randomtower.engine.tween;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tweener {

	private List<Tween> tweens = new ArrayList<Tween>();

	public Tweener(Tween... tweens) {
		if (tweens != null) {
			this.tweens.addAll(Arrays.asList(tweens));
		}
	}
	
	public boolean add(Tween tween){
		return tweens.add(tween);
	}
	
	public boolean remove(Tween tween){
		return tweens.remove(tween);
	}
	
	
}
