package it.marteEngine.achievements;

import java.util.ArrayList;
import java.util.List;

public class Achievement {
	
	private static ArrayList<Achievement> achievements = new ArrayList<Achievement>();
	
	/** the name of this achievement */
	private String name;
	/** the category. many achievements can belong to the same category (like mouse click). you increase all achievements that
	 * share the same category
	 */
	private String category;
	/** a longer description which could be shown on some message window */
	private String description;
	/** the current value */
	private int counter;
	/** the target value to reach. if counter is >= target this achievement will be unlocked */
	private final int target;
	/** did we already unlock this achievement (reached the goal = target) or not */
	private boolean unlocked;
	
	private Achievement(String name, String category, String description, int startValue, int targetValue) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.counter = startValue;
		this.target = targetValue;
		this.unlocked = false;
	}
	
	
	public static boolean addAchievement(String name, String category, String description, int startValue, int targetValue) {
		if (getAchievement(name) != null)
			return false;
		Achievement achievement = new Achievement(name, category, description, startValue, targetValue);
		achievements.add(achievement);
		return true;
	}
	
	public static List<Achievement> increaseAchievements(String category, int increment) {
		List<Achievement> unlocked = new ArrayList<Achievement>();
		
		for (Achievement achievement : achievements) {
			if (achievement.category.equalsIgnoreCase(category)) {
				achievement.counter += increment;
				if (achievement.counter >= achievement.target) {
					achievement.unlocked = true;
					unlocked.add(achievement);
				}
			}
		}
		if (unlocked.size() > 0)
			return unlocked;
		return (null);
	}
	
	public static final Achievement getAchievement(String name) {
		for (Achievement achievement : achievements) {
			if (achievement.name.equals(name))
				return achievement;
		}
		return null;
	}
	
	public static final List<Achievement> getAchievementsForCategory(String category) {
		ArrayList<Achievement> categoryList = new ArrayList<Achievement>();
		for (Achievement achievement : achievements) {
			if (achievement.category.equalsIgnoreCase(category))
				categoryList.add(achievement);
		}
		if (categoryList.size() > 0)
			return categoryList;
		return null;
	}

	public static final List<Achievement> getUnlockedAchievements() {
		ArrayList<Achievement> unlockedList = new ArrayList<Achievement>();
		for (Achievement achievement : achievements) {
			if (achievement.unlocked)
				unlockedList.add(achievement);
		}
		if (unlockedList.size() > 0)
			return unlockedList;
		return null;
	}


	public static final List<Achievement> getLockedAchievements() {
		ArrayList<Achievement> lockedList = new ArrayList<Achievement>();
		for (Achievement achievement : achievements) {
			if (!achievement.unlocked)
				lockedList.add(achievement);
		}
		if (lockedList.size() > 0)
			return lockedList;
		return null;
	}

	public static final List<Achievement> getAllAchievements() {
		if (achievements.size() > 0)
			return achievements;
		return null;
	}
	
	public String getName() {
		return name;
	}


	public String getDescription() {
		return description;
	}


	public int getCounter() {
		return counter;
	}


	public int getTarget() {
		return target;
	}


	public boolean isUnlocked() {
		return unlocked;
	}

}
