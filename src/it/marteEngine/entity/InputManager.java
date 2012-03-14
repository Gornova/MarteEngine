package it.marteEngine.entity;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Allows to bind keyboard and mouse input to a text command.
 */
public class InputManager {
	private Map<String, int[]> keyboardCommands;
	private Map<String, int[]> mouseCommands;
	private Input input;

	public InputManager() {
		keyboardCommands = new HashMap<String, int[]>();
		mouseCommands = new HashMap<String, int[]>();
	}

	public InputManager(Input input) {
		this();
		this.input = input;
	}

	/**
	 * Bind a command to 1 or more keyboard key(s). If the command already has a
	 * key mapping, then the new key will overwrite the previous key.
	 *
	 * @param command
	 *            Name of the command
	 * @param keys
	 *            Keyboard keys from the {@link Input} class
	 */
	public void bindToKey(String command, int... keys) {
		keyboardCommands.put(command, keys);
	}

	/**
	 * Bind a command to 1 or more mouse button(s) If the command already has a
	 * key mapping, then the new key will overwrites the previous key.
	 *
	 * @param command
	 *            Name of the command
	 * @param buttons
	 *            Mouse buttons from the {@link Input} class
	 */
	public void bindToMouse(String command, int... buttons) {
		for (int button : buttons) {
			// There is a maximum of 10 mouse buttons in Input
			if (button >= 0 && button <= 10) {
				mouseCommands.put(command, buttons);
			} else {
				throw new IllegalArgumentException("Button " + button
						+ " is not a mouse button.");
			}
		}
	}

	/**
	 * True if a command is currently down. isDown keeps on returning true until
	 * the command is released.
	 */
	public boolean isDown(String command) {
		boolean keyDown = checkKeyboardKeyDown(command);
		boolean mouseButtonDown = checkMouseButtonDown(command);
		return keyDown || mouseButtonDown;
	}

	private boolean checkKeyboardKeyDown(String command) {
		if (keyboardCommands.containsKey(command)) {
			int[] keyboardKeys = keyboardCommands.get(command);
			for (int keyboardKey : keyboardKeys) {
				if (input.isKeyDown(keyboardKey)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkMouseButtonDown(String command) {
		if (mouseCommands.containsKey(command)) {
			int[] mouseButtons = mouseCommands.get(command);
			for (int mouseButton : mouseButtons) {
				if (input.isMouseButtonDown(mouseButton)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check if a command has been pressed since this method was last called for
	 * that command. When a command is pressed true will be returned once until
	 * the command is released.
	 */
	public boolean isPressed(String command) {
		boolean keyPressed = checkKeyboardKeyPressed(command);
		boolean mouseButtonPressed = checkMouseButtonPressed(command);
		return keyPressed || mouseButtonPressed;
	}

	private boolean checkKeyboardKeyPressed(String command) {
		if (keyboardCommands.containsKey(command)) {
			int[] keyboardKeys = keyboardCommands.get(command);
			for (int keyboardKey : keyboardKeys) {
				if (input.isKeyPressed(keyboardKey)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkMouseButtonPressed(String command) {
		if (mouseCommands.containsKey(command)) {
			int[] mouseButtons = mouseCommands.get(command);
			for (int mouseButton : mouseButtons) {
				if (input.isMousePressed(mouseButton)) {
					return true;
				}
			}
		}
		return false;
	}

	public void setInput(Input input) {
		this.input = input;
	}

	/**
	 * Clears all input binds to this command
	 */
	public void clearBinds(String command) {
		keyboardCommands.remove(command);
		mouseCommands.remove(command);
	}

	/**
	 * Get a list of names of the keyboard and mouse input mapped to the given
	 * command.
	 */
	public List<String> getBinds(String command) {
		List<String> list = new ArrayList<String>();

		for (String keyCommand : keyboardCommands.keySet()) {
			if (keyCommand.equals(command)) {
				int[] keys = keyboardCommands.get(keyCommand);
				for (int key : keys) {
					list.add(Input.getKeyName(key));
				}
			}
		}

		for (String mouseCommand : mouseCommands.keySet()) {
			if (mouseCommand.equals(command)) {
				int[] buttons = mouseCommands.get(mouseCommand);
				for (int button : buttons) {
					String mouseButtonName = getMouseButtonName(button);
					list.add(mouseButtonName);
				}
			}
		}
		return list;
	}

	private String getMouseButtonName(int button) {
		String mouseButtonName;
		switch (button) {
			case Input.MOUSE_LEFT_BUTTON :
				mouseButtonName = "Left mouse button";
				break;
			case Input.MOUSE_MIDDLE_BUTTON :
				mouseButtonName = "Middle mouse button";
				break;
			case Input.MOUSE_RIGHT_BUTTON :
				mouseButtonName = "Right mouse button";
				break;
			default :
				mouseButtonName = Mouse.getButtonName(button);
		}
		return mouseButtonName;
	}
}