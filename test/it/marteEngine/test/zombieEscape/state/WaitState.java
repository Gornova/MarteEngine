package it.marteEngine.test.zombieEscape.state;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.State;
import it.marteEngine.entity.Entity;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.util.Log;

public class WaitState implements State {

	private Entity zombie;

	private int radar = 300;
	private Circle radarCircle;
	private Image waitImage;

	public WaitState(Entity zombie) {
		this.zombie = zombie;
	}

	public void init() {
		Log.debug("i'm waiting");
		radarCircle = new Circle(zombie.x + zombie.width / 2, zombie.y
				+ zombie.height / 2, radar);
		waitImage = ResourceManager.getImage("wait");
	}

	public void update(GameContainer container, int delta) {
		radarCircle = new Circle(zombie.x + zombie.width / 2, zombie.y
				+ zombie.height / 2, radar);
		List<Entity> onSight = ME.world.intersect(radarCircle);
		onSight.remove(zombie);

		if (!onSight.isEmpty()) {
			Log.debug("enemy on sight!");
			zombie.stateManager.enter("alert_state");
		}
	}

	public void render(Graphics g) {
		g.drawImage(zombie.getCurrentImage(), zombie.x, zombie.y);

		// render status image on top left of parent image
		g.drawImage(waitImage, zombie.x - 10, zombie.y - 10);

		if (ME.debugEnabled) {
			g.draw(radarCircle);
		}
	}

	@Override
	public String getName() {
		return "wait_state";
	}
}
