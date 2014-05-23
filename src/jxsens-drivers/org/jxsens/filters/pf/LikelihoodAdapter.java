package org.jxsens.filters.pf;

import java.util.*;

public class LikelihoodAdapter implements IParticleFilterComponent {

	private Random mRandom;
	private int mMaxNoParticles;
	private double mWeightThreshold;
	private IParticleModifier mParticleModifier;

	public LikelihoodAdapter() {
		mRandom = new Random();
		mMaxNoParticles = 10000;
		mWeightThreshold = 1E+020D;
	}

	public void setWeightThreshold(double d) {
		mWeightThreshold = d;
	}

	public void setParticleCap(int i) {
		mMaxNoParticles = i;
	}

	public void setParticleModifier(IParticleModifier iparticlemodifier) {
		mParticleModifier = iparticlemodifier;
	}

	public void applyModifier(ParticleStore particlestore) {
		Iterator<Particle> iterator = particlestore.iterator();
		ArrayList<Particle> arraylist = new ArrayList<Particle>();
		double ad[] = new double[particlestore.size()];
		double d = 0.0D;
		int i = 0;
		while (iterator.hasNext()) {
			Particle particle = iterator.next();
			d += particle.getWeight();
			ad[i++] = d;
		}
		double d1 = 0.0D;
        System.out.println("Sum\t\t\t" + d);
        do {
            double d2 = d * mRandom.nextDouble();
            int j = binarySearch(ad, 0, ad.length, d2);
            Particle particle1 = (Particle)particlestore.get(j).makeClone();
            particle1.setWeight(1.0D);
            mParticleModifier.applyModifier(particle1);
            d1 += particle1.getWeight();
            arraylist.add(particle1);
        } 
        while(d1 < mWeightThreshold && arraylist.size() < mMaxNoParticles);
        System.out.println("Count\t" + arraylist.size());
        particlestore.setParticles(arraylist);
    }

	private int binarySearch(double ad[], int i, int j, double d) {
		do {
			if (ad[i] > d) {
				return i;
			}
			int k = (i + j) / 2;
			if (d < ad[k]) {
				j = k;
			} else {
				i = k + 1;
			}
		} while (true);
	}
}
