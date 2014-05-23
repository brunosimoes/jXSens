package org.jxsens.filters.pf;

import java.util.Iterator;

public abstract class IParticleModifier implements IParticleFilterComponent {

	public IParticleModifier() {}

	public void applyModifier(ParticleStore particlestore) {
		for (Iterator<Particle> iterator = particlestore.iterator(); 
				iterator.hasNext(); 
				applyModifier((Particle) iterator.next())) {
		}
	}

	public abstract void applyModifier(Particle particle);
}
