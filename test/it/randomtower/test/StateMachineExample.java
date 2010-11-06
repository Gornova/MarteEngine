package it.randomtower.test;

import it.randomtower.engine.Entity;
import it.randomtower.engine.ME;
import it.randomtower.engine.StaticActor;
import it.randomtower.engine.states.CombatState;
import it.randomtower.engine.states.IdleState;
import it.randomtower.engine.states.MovingState;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class StateMachineExample extends BasicGame {

	private Entity e;

	public StateMachineExample() {
		super("StateMachineExample");
	}

	public void init(GameContainer container) throws SlickException {
		e = new StaticActor(100, 100, 100,100 ,"data/cross.png");
		e.speed = new Vector2f(8,8);
		e.stateManager.addAll(new IdleState(e),new MovingState(e),new CombatState(e));
		ME.add(e);
	}

	public void update(GameContainer container, int delta)
			throws SlickException {
		ME.update(container,delta );
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		ME.render(container, g);
	}

	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(
					new StateMachineExample(), 800, 600, false);
			container.setTargetFrameRate(30);
			container.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
