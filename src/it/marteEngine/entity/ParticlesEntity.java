package it.marteEngine.entity;

import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

public class ParticlesEntity extends Entity {
	
	public static final String EMITTERTYPE = "ParticleEmitter";

	/** used for ParticlesEntities that come with their own system because they have multiple emitters from Pedigree */
	private ParticleSystem system = null;
//	private ConfigurableEmitter emitter = null;
	private ParticleEmitter emitter = null;
	
	public ParticlesEntity(float x, float y, String emitterFile) {
		this(x, y, (ParticleEmitter) null);
		try {
			emitter = ParticleIO.loadEmitter(emitterFile);
			emitter.resetState();
			emitter.setEnabled(false);
			((ConfigurableEmitter)emitter).setPosition(x, y, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public ParticlesEntity(float x, float y, ConfigurableEmitter emitter) {
		super(x, y);
		if (emitter != null)
			this.emitter = emitter.duplicate();
		addType(EMITTERTYPE);
	}
	
	public ParticlesEntity(float x, float y, ParticleEmitter particleEmitter) {
		super(x,y);
		this.emitter = particleEmitter;
		addType(EMITTERTYPE);
	}
	
	@Override
	public void addedToWorld() {
		if (emitter != null && system == null) {
			world.particleSystem.addEmitter(emitter);
			emitter.resetState();
			emitter.setEnabled(true);
		} else if (system != null) {
			system.reset();
			system.setVisible(true);
		}
	}
	
	@Override
	public void removedFromWorld() {
		// some cleanup code
		if (emitter != null && system == null)
			world.particleSystem.removeEmitter(emitter);
		if (system != null) {
			system.setVisible(false);
			system = null;
		}
	}
	
	public void update(GameContainer container, int delta) throws SlickException {
		//TODO what do we want to react on?
		super.update(container, delta);
		if (emitter != null && emitter instanceof ConfigurableEmitter) {
			((ConfigurableEmitter)emitter).setPosition(x, y, false);
		} else if (system != null) {
			system.setPosition(x, y);
		}
		if (emitter.completed()) {
			if (system != null)
				system = null;
			this.destroy();
		}
	}
	
	public void render(GameContainer container, Graphics g)	throws SlickException {
		//TODO not really required because our particle system does the rendering, only for debugging maybe
		super.render(container, g);
	}
	

}
