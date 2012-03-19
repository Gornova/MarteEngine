package it.marteEngine.tween;

/**
 * @author Flashpunk (www.flashpunk.net)
 */
public enum Ease {
	NONE {
		float ease(float t) {
			return 0;
		}
	},
	QUAD_IN {
		float ease(float t) {
			return quadIn(t);
		}
	},
	QUAD_OUT {
		float ease(float t) {
			return quadOut(t);
		}
	},
	QUAD_IN_OUT {
		float ease(float t) {
			return quadInOut(t);
		}
	},
	CUBE_IN {
		float ease(float t) {
			return cubeIn(t);
		}
	},
	CUBE_OUT {
		float ease(float t) {
			return cubeOut(t);
		}
	},
	CUBE_IN_OUT {
		float ease(float t) {
			return cubeInOut(t);
		}
	},
	QUART_IN {
		float ease(float t) {
			return quartIn(t);
		}
	},
	QUART_OUT {
		float ease(float t) {
			return quartOut(t);
		}
	},
	QUART_IN_OUT {
		float ease(float t) {
			return quartInOut(t);
		}
	},
	QUINT_IN {
		float ease(float t) {
			return quintIn(t);
		}
	},
	QUINT_OUT {
		float ease(float t) {
			return quintOut(t);
		}
	},
	QUINT_IN_OUT {
		float ease(float t) {
			return quintInOut(t);
		}
	},
	SINE_IN {
		float ease(float t) {
			return sineIn(t);
		}
	},
	SINE_OUT {
		float ease(float t) {
			return sineOut(t);
		}
	},
	SINE_IN_OUT {
		float ease(float t) {
			return sineInOut(t);
		}
	},
	BOUNCE_IN {
		float ease(float t) {
			return bounceIn(t);
		}
	},
	BOUNCE_OUT {
		float ease(float t) {
			return bounceOut(t);
		}
	},
	BOUNCE_IN_OUT {
		float ease(float t) {
			return bounceInOut(t);
		}
	},
	CIRC_IN {
		float ease(float t) {
			return circIn(t);
		}
	},
	CIRC_OUT {
		float ease(float t) {
			return circOut(t);
		}
	},
	CIRC_IN_OUT {
		float ease(float t) {
			return circInOut(t);
		}
	},
	EXPO_IN {
		float ease(float t) {
			return expoIn(t);
		}
	},
	EXPO_OUT {
		float ease(float t) {
			return expoOut(t);
		}
	},
	EXPO_IN_OUT {
		float ease(float t) {
			return expoInOut(t);
		}
	},
	BACK_IN {
		float ease(float t) {
			return backIn(t);
		}
	},
	BACK_OUT {
		float ease(float t) {
			return backOut(t);
		}
	},
	BACK_IN_OUT {
		float ease(float t) {
			return backInOut(t);
		}
	};

	// Easing constants.
	private static double PI = Math.PI;
	private static double PI2 = Math.PI / 2;
	private static double EL = 2 * PI / .45;
	private static double B1 = 1 / 2.75;
	private static double B2 = 2 / 2.75;
	private static double B3 = 1.5 / 2.75;
	private static double B4 = 2.5 / 2.75;
	private static double B5 = 2.25 / 2.75;
	private static double B6 = 2.625 / 2.75;

	abstract float ease(float t);

	/**
	 * @return The next Ease in the order they're declared.
	 */
	public Ease next() {
		Ease[] values = values();
		return values[(ordinal() + 1) % values.length];
	}

	/** Quadratic in. */
	public static float quadIn(float t) {
		return t * t;
	}

	/** Quadratic out. */
	public static float quadOut(float t) {
		return -t * (t - 2);
	}

	/** Quadratic in and out. */
	public static float quadInOut(float t) {
		return t <= .5 ? t * t * 2 : 1 - (--t) * t * 2;
	}

	/** Cubic in. */
	public static float cubeIn(float t) {
		return t * t * t;
	}

	/** Cubic out. */
	public static float cubeOut(float t) {
		return 1 + (--t) * t * t;
	}

	/** Cubic in and out. */
	public static float cubeInOut(float t) {
		return t <= .5 ? t * t * t * 4 : 1 + (--t) * t * t * 4;
	}

	/** Quart in. */
	public static float quartIn(float t) {
		return t * t * t * t;
	}

	/** Quart out. */
	public static float quartOut(float t) {
		return 1 - (t -= 1) * t * t * t;
	}

	/** Quart in and out. */
	public static float quartInOut(float t) {
		return (float) (t <= .5 ? t * t * t * t * 8 : (1 - (t = t * 2 - 2) * t
				* t * t) / 2 + .5);
	}

	/** Quint in. */
	public static float quintIn(float t) {
		return t * t * t * t * t;
	}

	/** Quint out. */
	public static float quintOut(float t) {
		return (t = t - 1) * t * t * t * t + 1;
	}

	/** Quint in and out. */
	public static float quintInOut(float t) {
		return ((t *= 2) < 1) ? (t * t * t * t * t) / 2 : ((t -= 2) * t * t * t
				* t + 2) / 2;
	}

	/** Sine in. */
	public static float sineIn(float t) {
		return (float) (-Math.cos(PI2 * t) + 1);
	}

	/** Sine out. */
	public static float sineOut(float t) {
		return (float) Math.sin(PI2 * t);
	}

	/** Sine in and out. */
	public static float sineInOut(float t) {
		return (float) (-Math.cos(PI * t) / 2 + .5);
	}

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

	/** Circle in. */
	public static float circIn(float t) {
		return (float) -(Math.sqrt(1 - t * t) - 1);
	}

	/** Circle out. */
	public static float circOut(float t) {
		return (float) Math.sqrt(1 - (t - 1) * (t - 1));
	}

	/** Circle in and out. */
	public static float circInOut(float t) {
		return (float) (t <= .5 ? (Math.sqrt(1 - t * t * 4) - 1) / -2 : (Math
				.sqrt(1 - (t * 2 - 2) * (t * 2 - 2)) + 1) / 2);
	}

	/** Exponential in. */
	public static float expoIn(float t) {
		return (float) Math.pow(2, 10 * (t - 1));
	}

	/** Exponential out. */
	public static float expoOut(float t) {
		return (float) (-Math.pow(2, -10 * t) + 1);
	}

	/** Exponential in and out. */
	public static float expoInOut(float t) {
		return (float) (t < .5 ? Math.pow(2, 10 * (t * 2 - 1)) / 2 : (-Math
				.pow(2, -10 * (t * 2 - 1)) + 2) / 2);
	}

	/** Back in. */
	public static float backIn(float t) {
		return (float) (t * t * (2.70158 * t - 1.70158));
	}

	/** Back out. */
	public static float backOut(float t) {
		return (float) (1 - (--t) * (t) * (-2.70158 * t - 1.70158));
	}

	/** Back in and out. */
	public static float backInOut(float t) {
		t *= 2;
		if (t < 1)
			return (float) (t * t * (2.70158 * t - 1.70158) / 2);
		t--;
		return (float) ((1 - (--t) * (t) * (-2.70158 * t - 1.70158)) / 2 + .5);
	}
}
