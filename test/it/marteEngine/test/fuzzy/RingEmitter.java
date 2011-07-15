package it.marteEngine.test.fuzzy;

import org.newdawn.slick.Image;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

/**
 * A stock effect for exploding rings using the particle system
 * 
 * @author kevin
 * @author Mads Horndrup
 */
public class RingEmitter implements ParticleEmitter {

	/** The x coordinate of the center of the fire effect */
	private int x;
	/** The y coordinate of the center of the fire effect */
	private int y;
	/** The particle emission rate */
	private int interval = 400;
	/** Time til the next particle */
	private int timer;
	/** The size of the initial particles */
	private float size = 10;
	/** The radius of the ring */
	private int radius = 12;

	/**
	 * Create a default fire effect at 0,0
	 */
	public RingEmitter() {
	}

	/**
	 * Create a default fire effect at x,y
	 * 
	 * @param x
	 *            The x coordinate of the fire effect
	 * @param y
	 *            The y coordinate of the fire effect
	 */
	public RingEmitter(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Create a default fire effect at x,y
	 * 
	 * @param x
	 *            The x coordinate of the fire effect
	 * @param y
	 *            The y coordinate of the fire effect
	 * @param size
	 *            The size of the particle being pumped out
	 */
	public RingEmitter(int x, int y, float size) {
		this.x = x;
		this.y = y;
		this.size = size;
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#update(org.newdawn.slick.particles.ParticleSystem,
	 *      int)
	 */
	public void update(ParticleSystem system, int delta) {
		timer -= delta;
		if (timer <= 0) {
			timer = interval;
			for (int i = 0; i < 20; i++) {
				double a = Math.PI * 2 * Math.random();
				float parX = (float) (radius * Math.cos(a));
				float parY = (float) (radius * Math.sin(a));
				Particle p = system.getNewParticle(this, 1000);
				p.setColor(1, 1, 1, 0.5f);
				p.setPosition(x, y);
				p.setSize(size);
				p.setVelocity(parX, parY, 0.005f);
			}

		}
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#updateParticle(org.newdawn.slick.particles.Particle,
	 *      int)
	 */
	public void updateParticle(Particle particle, int delta) {
		if (particle.getLife() > 600) {
			particle.adjustSize(0.07f * delta);
		} else {
			particle.adjustSize(-0.04f * delta * (size / 40.0f));
		}
		float c = 0.002f * delta;
		particle.adjustColor(c, c, c, -c / 6);
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#isEnabled()
	 */
	public boolean isEnabled() {
		return true;
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#completed()
	 */
	public boolean completed() {
		return false;
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#useAdditive()
	 */
	public boolean useAdditive() {
		return false;
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#getImage()
	 */
	public Image getImage() {
		return null;
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#usePoints(org.newdawn.slick.particles.ParticleSystem)
	 */
	public boolean usePoints(ParticleSystem system) {
		return false;
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#isOriented()
	 */
	public boolean isOriented() {
		return false;
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#wrapUp()
	 */
	public void wrapUp() {
	}

	/**
	 * @see org.newdawn.slick.particles.ParticleEmitter#resetState()
	 */
	public void resetState() {
	}

	public void moveEmitter(int newX, int newY) {
		this.x = newX;
		this.y = newY;
	}
}