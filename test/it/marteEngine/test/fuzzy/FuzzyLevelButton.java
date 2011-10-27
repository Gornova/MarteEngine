package it.marteEngine.test.fuzzy;

import it.marteEngine.entity.TextEntity;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class FuzzyLevelButton extends TextEntity {

	private boolean renderBorder;
	private boolean enableClick;

	public FuzzyLevelButton(float x, float y, Font font, String text,
			boolean enableClick) {
		super(x, y, font, text);
		this.enableClick = enableClick;
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		if (enableClick) {
			if (renderBorder) {
				g.drawRect(x, y, width, height);
			}
			super.render(container, g);
		}
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);

		Input input = container.getInput();
		if (collidePoint(input.getMouseX(), input.getMouseY())) {
			renderBorder = true;
		} else {
			renderBorder = false;
		}

		if (enableClick && renderBorder
				&& input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			if (getText().equalsIgnoreCase("boss")) {
				FuzzyMain.gotoLevel = 11;
			} else {
				FuzzyMain.gotoLevel = Integer.valueOf(getText());
			}
		}

	}

}
