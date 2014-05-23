package org.jxsens.filters.pf;

import java.util.Iterator;

public class Normaliser implements IParticleFilterComponent {

	public Normaliser() {
	}

	public void applyModifier(ParticleStore particlestore) {
		double d = 0.0D;
		for (Iterator<Particle> iterator = particlestore.iterator(); iterator.hasNext();) {
			d += iterator.next().getWeight();
		}

		Particle particle;
		for (Iterator<Particle> it = particlestore.iterator(); it.hasNext(); ) {
			particle = it.next();
			particle.setWeight(particle.getWeight() / d);
		}
	}
}
