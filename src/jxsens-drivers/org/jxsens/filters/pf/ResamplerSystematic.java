package org.jxsens.filters.pf;

import java.util.*;

public class ResamplerSystematic implements IParticleFilterComponent {

	private Random mRandom;

	public ResamplerSystematic() {
		mRandom = new Random();
	}

	public void applyModifier(ParticleStore pstore) {
		Iterator<Particle> itr = pstore.iterator();
		ArrayList<Particle> arraylist = new ArrayList<Particle>();
		float f = 0.0F;
		float af[] = new float[pstore.size()];
		int i = 0;
		while (itr.hasNext()) {
			Particle particle = (Particle) itr.next();
			f = (float) ((double) f + particle.getWeight());
			af[i++] = f;
		}
		float f1 = mRandom.nextFloat() / (float) pstore.size();
		int j = 0;
		for (int k = 0; k < pstore.size(); k++) {
			for (float f2 = f * (f1 + (float) k / (float) pstore.size()); f2 > af[j]; j++) {
			}
			Particle particle1 = (Particle) pstore.get(j).makeClone();
			particle1.setWeight(1.0F / (float) pstore.size());
			arraylist.add(particle1);
		}

		pstore.setParticles(arraylist);
	}
}
