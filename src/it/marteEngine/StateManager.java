package it.marteEngine;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.HashMap;
import java.util.Map;

/**
 * The state manager stores states identified by a state name. It will update
 * and render the current state. The current state can be set using the
 * {@link #enter(String)} method.
 * 
 * The state manager can be enabled/disabled by calling the
 * {@link #setActive(boolean)} method. If disabled the current state will not be
 * rendered and updated.
 */
public class StateManager {
	private final Map<String, State> states;
	private String currentStateName;
	private State currentState;
	private boolean active;

	public StateManager() {
		states = new HashMap<String, State>();
	}

	public void addAll(State... states) {
		for (State state : states) {
			add(state);
		}
	}

	/**
	 * Add a new state to the state manager. If a previous state is already
	 * assigned to the state name it is overwritten.
	 */
	public void add(State newState) {
		states.put(newState.getName(), newState);
	}

	/** Transition to the designated state. */
	public void enter(String stateNameToEnter) {
		if (states.containsKey(stateNameToEnter)) {
			currentStateName = stateNameToEnter;
			currentState = states.get(stateNameToEnter);
			currentState.init();
			setActive(true);
		} else {
			throw new IllegalArgumentException("No state for "
					+ stateNameToEnter + " states:" + states.keySet());
		}
	}

	/**
	 * Enables or disables this state manager. Setting active to false means
	 * that the current state will not receive update and render calls anymore.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	public void update(GameContainer container, int delta) {
		if (active) {
			currentState.update(container, delta);
		}
	}

	public void render(Graphics g) {
		if (active) {
			currentState.render(g);
		}
	}

	/**
	 * Useful if you need to know what state you are currently in.
	 * 
	 * @return the name of the current state, null if there is no current state.
	 */
	public String getCurrentStateName() {
		return currentStateName;
	}

	public boolean isActive() {
		return active;
	}
}
