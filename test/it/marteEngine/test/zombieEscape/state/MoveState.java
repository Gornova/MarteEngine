package it.marteEngine.test.zombieEscape.state;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.State;
import it.marteEngine.entity.Entity;
import it.marteEngine.test.zombieEscape.Player;
import it.marteEngine.test.zombieEscape.Ray;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.Log;

public class MoveState implements State {

	private Entity e;

	private int radar = 300;
	private Circle radarCircle;

	private Ray ray;

	private int timer;

	public MoveState(Entity zombie) {
		this.e = zombie;
	}

	public void init() {
		Log.debug("i'm waiting");
		radarCircle = new Circle(e.x + e.width / 2, e.y + e.height / 2, radar);
	}

	public void update(GameContainer container, int delta) {
		radarCircle = new Circle(e.x + e.width / 2, e.y + e.height / 2, radar);

		Entity p = ME.world.find(Player.PLAYER);
		if (ray == null) {
			ray = new Ray(e, p);
		}

		if (ray != null) {
			Entity pl = ME.world.find(Player.PLAYER);
			ray.update(e, pl);
			Vector2f point = ray.line.getEnd();
			Vector2f cur = new Vector2f(e.x, e.y);
			cur = cur.sub(point);
			cur = cur.normalise();
			e.x -= cur.x;
			e.y -= cur.y;
		}
		
		timer +=delta;
		if (timer >= 1000){
			timer = 0;
			List<Entity> onSight = e.intersect(radarCircle);
			if (onSight!= null && onSight.isEmpty()){
				e.stateManager.enter(WaitState.class);
			}
		}

	}

	public void render(Graphics g) {
		g.drawImage(e.getCurrentImage(), e.x, e.y);

		// render status image on top left of parent image
		g.drawImage(ResourceManager.getImage("move"), e.x - 10, e.y - 10);
		if (ME.debugEnabled) {
			g.draw(radarCircle);
			g.setColor(Color.red);
			if (ray != null)
				ray.render(g);
			g.resetTransform();
		}

	}

}
