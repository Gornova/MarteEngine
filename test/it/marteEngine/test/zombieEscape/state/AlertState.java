package it.marteEngine.test.zombieEscape.state;

import it.marteEngine.ME;
import it.marteEngine.State;
import it.marteEngine.entity.Entity;
import it.marteEngine.resource.ResourceManager;
import it.marteEngine.test.zombieEscape.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.util.Log;

import java.util.List;

public class AlertState implements State {

	private Entity zombie;

	private int radar = 300;
	private Circle radarCircle;
	private Image alertImage;

	private float timer = 0;

	public AlertState(Entity zombie) {
		this.zombie = zombie;
	}

	public void init() {
		Log.debug("i'm alert");
		radarCircle = new Circle(zombie.x + zombie.width / 2, zombie.y
				+ zombie.height / 2, radar);
		alertImage = ResourceManager.getImage("alert");
	}

	public void update(GameContainer container, int delta) {
		radarCircle = new Circle(zombie.x + zombie.width / 2, zombie.y
				+ zombie.height / 2, radar);
		timer += delta;
		if (timer >= 1000) {
			timer = 0;

			List<Entity> onSight = ME.world.intersect(radarCircle);
			for (Entity ent : onSight) {
				if (ent instanceof Player) {
					Log.info("enemy confirmed, need to move!");
					zombie.stateManager.enter("move_state");
				}
			}
		}
	}

	public void render(Graphics g) {
		g.drawImage(zombie.getCurrentImage(), zombie.x, zombie.y);

		// render status image on top left of parent image
		g.drawImage(alertImage, zombie.x - 10, zombie.y - 10);

		if (ME.debugEnabled) {
			g.draw(radarCircle);
		}
	}

	@Override
	public String getName() {
		return "alert_state";
	}

}
