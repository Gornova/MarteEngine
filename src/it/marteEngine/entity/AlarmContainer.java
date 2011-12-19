package it.marteEngine.entity;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * Holds all the alarms for a single Entity.
 * The entity is notified when an alarm is triggered by the alarmTriggered method.
 */
public class AlarmContainer {
	private Map<String, Alarm> alarms = new Hashtable<String, Alarm>();
	private Map<String, Alarm> alarmsToAdd = new Hashtable<String, Alarm>();
	private Entity entity;

	public AlarmContainer(Entity entity) {
		this.entity = entity;
	}

	public Alarm addAlarm(String name, int triggerTime, boolean oneShot, boolean startNow) {
		Alarm alarm = new Alarm(name, triggerTime, oneShot);

		if (startNow) {
			alarm.start();
		}
		alarmsToAdd.put(name, alarm);
		return alarm;
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

	public void update(int delta) {
		ArrayList<String> deadAlarms = null;
		Set<String> alarmNames = alarms.keySet();
		if (!alarmNames.isEmpty()) {
			for (String alarmName : alarmNames) {
				Alarm alarm = alarms.get(alarmName);
				if (alarm.isActive()) {
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
				if (alarm.isDead()) {
					if (deadAlarms == null)
						deadAlarms = new ArrayList<String>();
					deadAlarms.add(alarmName);
				}
			}
			if (deadAlarms != null) {
				for (String deadAlarm : deadAlarms) {
					alarms.remove(deadAlarm);
				}
			}
		}

		if (!alarmsToAdd.isEmpty()) {
			for (String alarmName : alarmsToAdd.keySet()) {
				alarms.put(alarmName, alarmsToAdd.get(alarmName));
			}
			alarmsToAdd.clear();
		}
	}
}
