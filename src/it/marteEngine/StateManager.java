package it.marteEngine;

import java.util.Collections;
import java.util.LinkedList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class StateManager {
	private LinkedList<State> states;
	private State currentState;

	public StateManager() {
		states = new LinkedList<State>();
	}

	public void add(State s) {
		states.add(s);
		if (currentState == null) {
			currentState = s;
			currentState.init();
		}
	}

	public void addAll(State... st) {
		if (st != null && st.length > 0) {
			Collections.addAll(states, st);
			if (currentState == null) {
				currentState = st[0];
				currentState.init();
			}
		}
	}

	/** Transition to the designated state. */
	public void enter(Class<?> c) {
		for (State s : states) {
			if (s.getClass().getCanonicalName()
					.equalsIgnoreCase(c.getCanonicalName())) {
				currentState = s;
				currentState.init();
				return;
			}
		}
	}

	/** Useful if you need to know what state you are currently in. */
	public Object currentState() {
		return currentState;
	}

	public void update(GameContainer container, int delta) {
		currentState.update(container, delta);
	}

	public void render(Graphics g) {
		currentState.render(g);
	}
}