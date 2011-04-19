package it.randomtower.test.entity;

import it.randomtower.engine.ME;
import it.randomtower.engine.World;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ParticleEntityTest  extends StateBasedGame {

	public ParticleEntityTest() {
		super("ParticleEntityTest");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		World world = new World(0,container); 
		prepareTestScenario(container, world);
		addState(world);
	}

	private void prepareTestScenario(GameContainer container, World world) {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		try {
			ME.keyRestart = Input.KEY_R;
			ME.debugEnabled = true;
			
			AppGameContainer container = new AppGameContainer(
					new ParticleEntityTest(), 800, 600, false);
			container.setTargetFrameRate(60);
			container.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
