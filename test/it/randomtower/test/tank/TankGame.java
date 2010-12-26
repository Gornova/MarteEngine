package it.randomtower.test.tank;

import org.newdawn.slick.GameContainer;

import it.randomtower.engine.World;

public class TankGame extends World {

	public Tank tank;
	
	public TankGame(int id, GameContainer container) {
		super(id, container);
		
		tank = new Tank(300, 300);
		add(tank);

	}

}
