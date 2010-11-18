package it.randomtower.test.stateMachine;

import it.randomtower.engine.Entity;
import it.randomtower.engine.ME;
import it.randomtower.engine.WorldGameState;
import it.randomtower.engine.actors.StaticActor;
import it.randomtower.engine.states.CombatState;
import it.randomtower.engine.states.IdleState;
import it.randomtower.engine.states.MovingState;

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
		
		WorldGameState state = new WorldGameState(0); 
		
		Entity e = new StaticActor(100, 100, 100,100 ,"data/cross.png");
		e.speed = new Vector2f(8,8);
		e.stateManager.addAll(new IdleState(e),new MovingState(e),new CombatState(e));
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
