package it.marteEngine.entity;

import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

public class EmitterEntity extends Entity {
	
	public static final String EMITTERTYPE = "ParticleEmitter";

//	private ParticleSystem system = null;
	private ConfigurableEmitter emitter = null;
	
	public EmitterEntity(float x, float y, Image particle, String emitterFile) {
		this(x, y, particle, (ConfigurableEmitter) null);
		try {
			emitter = ParticleIO.loadEmitter(emitterFile);
			emitter.setEnabled(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public EmitterEntity(float x, float y, Image particle, ConfigurableEmitter emitter) {
		this(x, y, particle, emitter, 100);
	}

	public EmitterEntity(float x, float y, Image particle, ConfigurableEmitter emitter, int maxParticles) {
		super(x, y);
		if (emitter != null)
			this.emitter = emitter.duplicate();
		addType(EMITTERTYPE);
	}
	
	@Override
	public void addedToWorld() {
		if (emitter != null) {
			world.particleSystem.addEmitter(emitter);
			emitter.setEnabled(true);
		}
	}
	
	@Override
	public void removedFromWorld() {
		// some cleanup code
		if (emitter != null)
			world.particleSystem.removeEmitter(emitter);
	}
	
	public void update(GameContainer container, int delta) throws SlickException {
		//TODO what do we want to react on?
		super.update(container, delta);
		if (emitter != null) {
			emitter.setPosition(x, y);
		}
		if (emitter.completed()) {
			this.destroy();
		}
	}
	
	public void render(GameContainer container, Graphics g)	throws SlickException {
		//TODO not really required because our particle system does the rendering, only for debugging maybe
		super.render(container, g);
	}
	

}
