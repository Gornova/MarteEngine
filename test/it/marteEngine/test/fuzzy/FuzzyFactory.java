package it.marteEngine.test.fuzzy;

import it.marteEngine.tween.Ease;
import it.marteEngine.tween.NumTween;
import it.marteEngine.tween.Tween;
import it.marteEngine.tween.Tween.TweenerMode;
import it.marteEngine.tween.Tweener;

/**
 * A factory for various Fuzzy stuff
 */
public class FuzzyFactory {

	public static final String FADE = "fade";
	public static final String MOVE_UP = "moveUp";
	public static float time = 10;

	private FuzzyFactory() {
	}

	public static Tween getFadeTween(float t) {
		NumTween tween = new NumTween(1, 0, t, TweenerMode.ONESHOT,
				Ease.CUBE_OUT, false);
		tween.setName(FADE);
		return tween;
	}

	public static Tween getMoveUpTween(float t) {
		NumTween tween = new NumTween(0, 5, t, TweenerMode.ONESHOT,
				Ease.QUAD_IN, false);
		tween.setName(MOVE_UP);
		return tween;
	}

	public static Tweener getFadeMoveTweener() {
		return new Tweener(getFadeTween(time), getMoveUpTween(time));
	}

	public static Tweener getFadeMoveTweener(int t) {
		return new Tweener(getFadeTween(t), getMoveUpTween(t));
	}

}
