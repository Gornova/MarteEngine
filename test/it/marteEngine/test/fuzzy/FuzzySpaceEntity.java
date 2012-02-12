package it.marteEngine.test.fuzzy;

import it.marteEngine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class FuzzySpaceEntity extends Entity {

	private boolean up = false;
	private boolean showContinue;

	public FuzzySpaceEntity(float x, float y, boolean showContinue) {
		super(x, y);
		this.showContinue = showContinue;
	}

	@Override
	public void addedToWorld() {
		addAlarm("jump", 15, false, true);
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		super.render(container, g);

		if (!showContinue) {
			drawScaled(g, 0.6f, "Press Space to start", x, y);
		} else {
			drawScaled(g, 0.6f, "Press Space to continue", x, y);
		}

	}

	@Override
	public void alarmTriggered(String name) {
		if (name.equalsIgnoreCase("jump")) {
			up = up ? false : true;
		}
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);

		if (up) {
			y--;
		} else {
			y++;
		}

		if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			FuzzyMenuWorld.gotoGame = true;
		}
	}

	private void drawScaled(Graphics g, float scale, String text, float x,
			float y) {
		g.scale(scale, scale);
		FuzzyMain.font.drawString(x, y, text);
		g.resetTransform();
	}

}
