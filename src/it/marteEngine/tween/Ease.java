package it.marteEngine.tween;

import org.newdawn.slick.util.Log;

/**
 * 
 * @author Flashpunk (www.flashpunk.net)
 * 
 */
public class Ease {

	// Easing constants.
	/** @private */
	private static double PI = Math.PI;
	/** @private */
	private static double PI2 = Math.PI / 2;
	/** @private */
	@SuppressWarnings("unused")
	private static double EL = 2 * PI / .45;
	/** @private */
	private static double B1 = 1 / 2.75;
	/** @private */
	private static double B2 = 2 / 2.75;
	/** @private */
	private static double B3 = 1.5 / 2.75;
	/** @private */
	private static double B4 = 2.5 / 2.75;
	/** @private */
	private static double B5 = 2.25 / 2.75;
	/** @private */
	private static double B6 = 2.625 / 2.75;

	public static final int NONE = -1;

	public static final int QUAD_IN = 0;

	/** Quadratic in. */
	public static float quadIn(float t) {
		return t * t;
	}

	public static final int QUAD_OUT = 1;

	/** Quadratic out. */
	public static float quadOut(float t) {
		return -t * (t - 2);
	}

	public static final int QUAD_IN_OUT = 2;

	/** Quadratic in and out. */
	public static float quadInOut(float t) {
		return t <= .5 ? t * t * 2 : 1 - (--t) * t * 2;
	}

	public static final int CUBE_IN = 3;

	/** Cubic in. */
	public static float cubeIn(float t) {
		return t * t * t;
	}

	public static final int CUBE_OUT = 4;

	/** Cubic out. */
	public static float cubeOut(float t) {
		return 1 + (--t) * t * t;
	}

	public static final int CUBE_IN_OUT = 5;

	/** Cubic in and out. */
	public static float cubeInOut(float t) {
		return t <= .5 ? t * t * t * 4 : 1 + (--t) * t * t * 4;
	}

	public static final int QUART_IN = 6;

	/** Quart in. */
	public static float quartIn(float t) {
		return t * t * t * t;
	}

	public static final int QUART_OUT = 7;

	/** Quart out. */
	public static float quartOut(float t) {
		return 1 - (t -= 1) * t * t * t;
	}

	public static final int QUART_IN_OUT = 8;

	/** Quart in and out. */
	public static float quartInOut(float t) {
		return (float) (t <= .5 ? t * t * t * t * 8 : (1 - (t = t * 2 - 2) * t
				* t * t) / 2 + .5);
	}

	public static final int QUINT_IN = 9;

	/** Quint in. */
	public static float quintIn(float t) {
		return t * t * t * t * t;
	}

	public static final int QUINT_OUT = 10;

	/** Quint out. */
	public static float quintOut(float t) {
		return (t = t - 1) * t * t * t * t + 1;
	}

	public static final int QUINT_IN_OUT = 11;

	/** Quint in and out. */
	public static float quintInOut(float t) {
		return ((t *= 2) < 1) ? (t * t * t * t * t) / 2 : ((t -= 2) * t * t * t
				* t + 2) / 2;
	}

	public static final int SINE_IN = 12;

	/** Sine in. */
	public static float sineIn(float t) {
		return (float) (-Math.cos(PI2 * t) + 1);
	}

	public static final int SINE_OUT = 13;

	/** Sine out. */
	public static float sineOut(float t) {
		return (float) Math.sin(PI2 * t);
	}

	public static final int SINE_IN_OUT = 14;

	/** Sine in and out. */
	public static float sineInOut(float t) {
		return (float) (-Math.cos(PI * t) / 2 + .5);
	}

	public static final int BOUNCE_IN = 15;

	/** Bounce in. */
	public static float bounceIn(float t) {
		t = 1 - t;
		if (t < B1)
			return (float) (1 - 7.5625 * t * t);
		if (t < B2)
			return (float) (1 - (7.5625 * (t - B3) * (t - B3) + .75));
		if (t < B4)
			return (float) (1 - (7.5625 * (t - B5) * (t - B5) + .9375));
		return (float) (1 - (7.5625 * (t - B6) * (t - B6) + .984375));
	}

	public static final int BOUNCE_OUT = 16;

	/** Bounce out. */
	public static float bounceOut(float t) {
		if (t < B1)
			return (float) (7.5625 * t * t);
		if (t < B2)
			return (float) (7.5625 * (t - B3) * (t - B3) + .75);
		if (t < B4)
			return (float) (7.5625 * (t - B5) * (t - B5) + .9375);
		return (float) (7.5625 * (t - B6) * (t - B6) + .984375);
	}

	public static final int BOUNCE_IN_OUT = 17;

	/** Bounce in and out. */
	public static float bounceInOut(float t) {
		if (t < .5) {
			t = 1 - t * 2;
			if (t < B1)
				return (float) ((1 - 7.5625 * t * t) / 2);
			if (t < B2)
				return (float) ((1 - (7.5625 * (t - B3) * (t - B3) + .75)) / 2);
			if (t < B4)
				return (float) ((1 - (7.5625 * (t - B5) * (t - B5) + .9375)) / 2);
			return (float) ((1 - (7.5625 * (t - B6) * (t - B6) + .984375)) / 2);
		}
		t = t * 2 - 1;
		if (t < B1)
			return (float) ((7.5625 * t * t) / 2 + .5);
		if (t < B2)
			return (float) ((7.5625 * (t - B3) * (t - B3) + .75) / 2 + .5);
		if (t < B4)
			return (float) ((7.5625 * (t - B5) * (t - B5) + .9375) / 2 + .5);
		return (float) ((7.5625 * (t - B6) * (t - B6) + .984375) / 2 + .5);
	}

	public static final int CIRC_IN = 18;

	/** Circle in. */
	public static float circIn(float t) {
		return (float) -(Math.sqrt(1 - t * t) - 1);
	}

	public static final int CIRC_OUT = 19;

	/** Circle out. */
	public static float circOut(float t) {
		return (float) Math.sqrt(1 - (t - 1) * (t - 1));
	}

	public static final int CIRC_IN_OUT = 20;

	/** Circle in and out. */
	public static float circInOut(float t) {
		return (float) (t <= .5 ? (Math.sqrt(1 - t * t * 4) - 1) / -2 : (Math
				.sqrt(1 - (t * 2 - 2) * (t * 2 - 2)) + 1) / 2);
	}

	public static final int EXPO_IN = 21;

	/** Exponential in. */
	public static float expoIn(float t) {
		return (float) Math.pow(2, 10 * (t - 1));
	}

	public static final int EXPO_OUT = 22;

	/** Exponential out. */
	public static float expoOut(float t) {
		return (float) (-Math.pow(2, -10 * t) + 1);
	}

	public static final int EXPO_IN_OUT = 23;

	/** Exponential in and out. */
	public static float expoInOut(float t) {
		return (float) (t < .5 ? Math.pow(2, 10 * (t * 2 - 1)) / 2 : (-Math
				.pow(2, -10 * (t * 2 - 1)) + 2) / 2);
	}

	public static final int BACK_IN = 24;

	/** Back in. */
	public static float backIn(float t) {
		return (float) (t * t * (2.70158 * t - 1.70158));
	}

	public static final int BACK_OUT = 25;

	/** Back out. */
	public static float backOut(float t) {
		return (float) (1 - (--t) * (t) * (-2.70158 * t - 1.70158));
	}

	public static final int BACK_IN_OUT = 26;

	/** Back in and out. */
	public static float backInOut(float t) {
		t *= 2;
		if (t < 1)
			return (float) (t * t * (2.70158 * t - 1.70158) / 2);
		t--;
		return (float) ((1 - (--t) * (t) * (-2.70158 * t - 1.70158)) / 2 + .5);
	}

	public static String getName(int easeFunction) {
		switch (easeFunction) {
		case Ease.QUAD_IN:
			return "QUAD_IN";
		case Ease.QUAD_OUT:
			return "QUAD_OUT";
		case Ease.QUAD_IN_OUT:
			return "QUAD_IN_OUT";
		case Ease.CUBE_IN:
			return "CUBE_IN";
		case Ease.CUBE_OUT:
			return "CUBE_OUT";
		case Ease.CUBE_IN_OUT:
			return "CUBE_IN_OUT";
		case Ease.QUART_IN:
			return "QUART_IN";
		case Ease.QUART_OUT:
			return "QUART_OUT";
		case Ease.QUART_IN_OUT:
			return "QUART_IN_OUT";
		case Ease.QUINT_IN:
			return "QUINT_IN";
		case Ease.QUINT_OUT:
			return "QUINT_OUT";
		case Ease.QUINT_IN_OUT:
			return "QUINT_IN_OUT";
		case Ease.SINE_IN:
			return "SINE_IN";
		case Ease.SINE_OUT:
			return "SINE_OUT";
		case Ease.SINE_IN_OUT:
			return "SINE_IN_OUT";
		case Ease.BOUNCE_IN:
			return "BOUNCE_IN";
		case Ease.BOUNCE_OUT:
			return "BOUNCE_OUT";
		case Ease.BOUNCE_IN_OUT:
			return "BOUNCE_IN_OUT";
		case Ease.CIRC_IN:
			return "CIRC_IN";
		case Ease.CIRC_OUT:
			return "CIRC_OUT";
		case Ease.CIRC_IN_OUT:
			return "CIRC_IN_OUT";
		case Ease.EXPO_IN:
			return "EXPO_IN";
		case Ease.EXPO_OUT:
			return "EXPO_OUT";
		case Ease.EXPO_IN_OUT:
			return "EXPO_IN_OUT";
		case Ease.BACK_IN:
			return "BACK_IN";
		case Ease.BACK_OUT:
			return "BACK_OUT";
		case Ease.BACK_IN_OUT:
			return "BACK_IN_OUT";
		default:
			Log.warn("Easing function not mapped " + easeFunction);
			break;
		}
		return "NONE";
	}

	public static float ease(int easeFunction, float t) {
		switch (easeFunction) {
		case Ease.QUAD_IN:
			return Ease.quadIn(t);
		case Ease.QUAD_OUT:
			return Ease.quadOut(t);
		case Ease.QUAD_IN_OUT:
			return Ease.quadInOut(t);
		case Ease.CUBE_IN:
			return Ease.cubeIn(t);
		case Ease.CUBE_OUT:
			return Ease.cubeOut(t);
		case Ease.CUBE_IN_OUT:
			return Ease.cubeInOut(t);
		case Ease.QUART_IN:
			return Ease.quartIn(t);
		case Ease.QUART_OUT:
			return Ease.quartOut(t);
		case Ease.QUART_IN_OUT:
			return Ease.quartInOut(t);
		case Ease.QUINT_IN:
			return Ease.quintIn(t);
		case Ease.QUINT_OUT:
			return Ease.quintOut(t);
		case Ease.QUINT_IN_OUT:
			return Ease.quintInOut(t);
		case Ease.SINE_IN:
			return Ease.sineIn(t);
		case Ease.SINE_OUT:
			return Ease.sineOut(t);
		case Ease.SINE_IN_OUT:
			return Ease.sineInOut(t);
		case Ease.BOUNCE_IN:
			return Ease.bounceIn(t);
		case Ease.BOUNCE_OUT:
			return Ease.bounceOut(t);
		case Ease.BOUNCE_IN_OUT:
			return Ease.bounceInOut(t);
		case Ease.CIRC_IN:
			return Ease.circIn(t);
		case Ease.CIRC_OUT:
			return Ease.circOut(t);
		case Ease.CIRC_IN_OUT:
			return Ease.circInOut(t);
		case Ease.EXPO_IN:
			return Ease.expoIn(t);
		case Ease.EXPO_OUT:
			return Ease.expoOut(t);
		case Ease.EXPO_IN_OUT:
			return Ease.expoInOut(t);
		case Ease.BACK_IN:
			return Ease.backIn(t);
		case Ease.BACK_OUT:
			return Ease.backOut(t);
		case Ease.BACK_IN_OUT:
			return Ease.backInOut(t);
		default:
			Log.warn("Easing function not mapped " + easeFunction);
			break;
		}
		return 0;
	}

}