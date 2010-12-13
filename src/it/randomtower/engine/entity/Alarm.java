package it.randomtower.engine.entity;

public class Alarm {
	/** the name of this alarm */
	private String name;
	/** is this alarm active or not */
	private boolean active = false;
	/** the time in update calls to trigger */
	private int triggerTime;
	/** the current update call counter for this alarm */
	private int counter = 0;
	/** is this a one time alarm or an endless looping one */
	private boolean oneShotAlaram = true;
	/** marked as dead, don't want to remove alarms while iterating them! */
	private boolean dead = false;
	
	/**
	 * create a new Alarm object
	 * @param name the name of this alarm
	 * @param triggerTime after how many update calls should this alarm trigger
	 * @param oneShot shall this alarm remove itself after one trigger or shall it run for ever
	 */
	public Alarm(String name, int triggerTime, boolean oneShot) {
		this.name = name;
		this.triggerTime = triggerTime;
		this.counter = 0;
		this.oneShotAlaram = oneShot;
		this.active = false;
	}
	
	public void start() {
		this.counter = 0;
		this.active = true;
	}
	
	public void pause() {
		this.active = false;
	}
	
	public void resume() {
		this.active = true;
	}

	public boolean isActive() {
		return active;
	}

	/**
	 * called by World if alarm is active. Don't mess around with it.
	 */
	public boolean update() {
		this.counter++;
		if (this.counter >= this.triggerTime)
			return true;
		return false;
	}

	public String getName() {
		return name;
	}

	public int getTriggerTime() {
		return triggerTime;
	}

	public int getCounter() {
		return counter;
	}

	public boolean isOneShotAlaram() {
		return oneShotAlaram;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}
	
}
