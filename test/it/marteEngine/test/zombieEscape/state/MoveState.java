package it.marteEngine.test.zombieEscape.state;

import it.marteEngine.ME;
import it.marteEngine.State;
import it.marteEngine.entity.Entity;
import it.marteEngine.resource.ResourceManager;
import it.marteEngine.test.zombieEscape.Player;
import it.marteEngine.test.zombieEscape.Ray;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.Log;

import java.util.List;

public class MoveState implements State {

	private Entity zombie;

	private int radar = 300;
	private Circle radarCircle;
	private Ray ray;
	private Image moveImage;

	private int timer;

	public MoveState(Entity zombie) {
		this.zombie = zombie;
	}

	public void init() {
		Log.debug("i'm waiting");
		radarCircle = new Circle(zombie.x + zombie.width / 2, zombie.y
				+ zombie.height / 2, radar);
		moveImage = ResourceManager.getImage("move");
	}

	public void update(GameContainer container, int delta) {
		radarCircle = new Circle(zombie.x + zombie.width / 2, zombie.y
				+ zombie.height / 2, radar);

		Entity p = ME.world.find(Player.PLAYER);
		if (ray == null) {
			ray = new Ray(zombie, p);
		}

		Entity player = ME.world.find(Player.PLAYER);
		ray.update(zombie, player);
		Vector2f point = ray.line.getEnd();
		Vector2f cur = new Vector2f(zombie.x, zombie.y);
		cur = cur.sub(point);
		cur = cur.normalise();
		zombie.x -= cur.x;
		zombie.y -= cur.y;

		timer += delta;
		if (timer >= 1000) {
			timer = 0;
			List<Entity> onSight = ME.world.intersect(radarCircle);
			onSight.remove(zombie);

			if (onSight.isEmpty()) {
				zombie.stateManager.enter("wait_state");
			}
		}
	}

	public void render(Graphics g) {
		g.drawImage(zombie.getCurrentImage(), zombie.x, zombie.y);

		// render status image on top left of parent image
		g.drawImage(moveImage, zombie.x - 10, zombie.y - 10);

		if (ME.debugEnabled) {
			g.draw(radarCircle);
			g.setColor(Color.red);
			if (ray != null) {
				ray.render(g);
			}
			g.resetTransform();
		}
	}

	@Override
	public String getName() {
		return "move_state";
	}
}
