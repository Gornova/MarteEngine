package it.marteEngine.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Holds all the alarms for a single Entity. The entity is notified when an
 * alarm is triggered by the alarmTriggered method.
 */
public class AlarmContainer {
	private Map<String, Alarm> alarms = new HashMap<String, Alarm>();
	private Map<String, Alarm> alarmsToAdd = new HashMap<String, Alarm>();
	private Entity entity;

	public AlarmContainer(Entity entity) {
		this.entity = entity;
	}

	public void addAlarm(Alarm alarm, boolean startNow) {
		if (startNow) {
			alarm.start();
		}
		alarmsToAdd.put(alarm.getName(), alarm);
	}

	public void update(int delta) {
		List<String> deadAlarms = null;
		Set<String> alarmNames = alarms.keySet();

		for (String alarmName : alarmNames) {
			Alarm alarm = alarms.get(alarmName);
			if (alarm.isActive()) {
				updateActiveAlarm(alarm, delta);
			}
			if (alarm.isDead()) {
				if (deadAlarms == null) {
					deadAlarms = new ArrayList<String>();
				}
				deadAlarms.add(alarmName);
			}
		}
		if (deadAlarms != null) {
			for (String deadAlarm : deadAlarms) {
				alarms.remove(deadAlarm);
			}
		}

		if (!alarmsToAdd.isEmpty()) {
			for (String alarmName : alarmsToAdd.keySet()) {
				alarms.put(alarmName, alarmsToAdd.get(alarmName));
			}
			alarmsToAdd.clear();
		}
	}

	private void updateActiveAlarm(Alarm alarm, int delta) {
		boolean retval = alarm.update(delta);
		if (retval) {
			entity.alarmTriggered(alarm.getName());
			if (alarm.isOneShotAlaram()) {
				alarm.setActive(false);
			} else {
				alarm.start();
			}
		}
	}

	public boolean restartAlarm(String name) {
		Alarm alarm = alarms.get(name);
		if (alarm != null) {
			alarm.start();
			return true;
		}
		return false;
	}

	public boolean pauseAlarm(String name) {
		Alarm alarm = alarms.get(name);
		if (alarm != null) {
			alarm.pause();
			return true;
		}
		return false;

	}

	public boolean resumeAlarm(String name) {
		Alarm alarm = alarms.get(name);
		if (alarm != null) {
			alarm.resume();
			return true;
		}
		return false;
	}

	public boolean destroyAlarm(String name) {
		Alarm alarm = alarms.get(name);
		if (alarm != null) {
			alarm.setDead(true);
			return true;
		}
		return false;
	}

	public boolean hasAlarm(String name) {
		return alarms.containsKey(name);
	}
}