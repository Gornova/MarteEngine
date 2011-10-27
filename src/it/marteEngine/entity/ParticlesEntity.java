package it.marteEngine.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleSystem;

public class ParticlesEntity extends Entity {

	private ParticleSystem system = null;
	private ConfigurableEmitter emitter = null;

	public ParticlesEntity(float x, float y, Image particle,
			ConfigurableEmitter emitter) {
		this(x, y, particle, emitter, 100);
	}

	public ParticlesEntity(float x, float y, Image particle,
			ConfigurableEmitter emitter, int maxParticles) {
		super(x, y);
		system = new ParticleSystem(particle);
		if (emitter != null)
			system.addEmitter(emitter.duplicate());
	}

	public ParticlesEntity(float x, float y, ParticleSystem configuredSystem) {
		super(x, y);
		system = configuredSystem;
	}

	public void setBlendingMode(int mode) {
		system.setBlendingMode(mode);
	}

	public void update(GameContainer container, int delta)
			throws SlickException {
		// TODO fertigmachen
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		// TODO fertigmachen
	}

}
