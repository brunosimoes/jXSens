package org.jxsens.filters.pf;

import java.util.*;

public class ResamplerMultinomial implements IParticleFilterComponent {

	private Random mRandom;

	public ResamplerMultinomial() {
		mRandom = new Random();
	}

	public void applyModifier(ParticleStore particlestore) {
		Iterator<Particle> iterator = particlestore.iterator();
		ArrayList<Particle> arraylist = new ArrayList<Particle>();
		float f = 0.0F;
		float af[] = new float[particlestore.size()];
		int i = 0;
		while (iterator.hasNext()) {
			Particle particle = (Particle) iterator.next();
			f = (float) ((double) f + particle.getWeight());
			af[i++] = f;
		}
		for (int j = 0; j < particlestore.size(); j++) {
			float f1 = mRandom.nextFloat() * f;
			int k = binarySearch(af, 0, af.length, f1);
			Particle particle1 = (Particle) particlestore.get(k).makeClone();
			particle1.setWeight(1.0D);
			arraylist.add(particle1);
		}

		particlestore.setParticles(arraylist);
	}

	private int binarySearch(float af[], int i, int j, float f) {
		do {
			if (af[i] > f) {
				return i;
			}
			int k = (i + j) / 2;
			if (f < af[k]) {
				j = k;
			} else {
				i = k + 1;
			}
		} while (true);
	}
}
