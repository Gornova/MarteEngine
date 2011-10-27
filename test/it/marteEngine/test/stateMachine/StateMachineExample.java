package it.marteEngine.test.stateMachine;

import it.marteEngine.ME;
import it.marteEngine.World;
import it.marteEngine.actor.StaticActor;
import it.marteEngine.entity.Entity;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class StateMachineExample extends StateBasedGame {

	public StateMachineExample() {
		super("StateMachineExample");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {

		World state = new World(0, container);

		Entity e = new StaticActor(100, 100, 100, 100, "data/cross.png");
		e.speed = new Vector2f(8, 8);
		e.stateManager.addAll(new IdleState(e), new MovingState(e),
				new CombatState(e));
		state.add(e);

		addState(state);
	}

	public static void main(String[] argv) {
		try {
			ME.keyRestart = Input.KEY_R;
			AppGameContainer container = new AppGameContainer(
					new StateMachineExample(), 800, 600, false);
			container.setTargetFrameRate(30);
			container.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
