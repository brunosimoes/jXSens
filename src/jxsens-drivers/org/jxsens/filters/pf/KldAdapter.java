package org.jxsens.filters.pf;

import java.util.*;

public class KldAdapter implements IParticleFilterComponent {

	public static interface BinClassifier {
		public abstract Object getBin(Particle particle);
	}

	private Random mRandom;
	private float mNormalDistZ;
	private float lEpsilon;
	private HashSet<Object> mNonEmptyBins;
	private int mMaxNoParticles;
	private int mMinNoParticles;
	private BinClassifier mBinClassifier;
	private IParticleModifier mParticleModifier;

	public KldAdapter() {
		mRandom = new Random();
		mNormalDistZ = 2.33F;
		lEpsilon = 0.01F;
		mNonEmptyBins = new HashSet<Object>();
		mMaxNoParticles = 20000;
		mMinNoParticles = 50;
	}

	public void setBinClassifier(BinClassifier binclassifier) {
		mBinClassifier = binclassifier;
	}

	public void setMinNoParticles(int i) {
		mMinNoParticles = i;
	}

	public void setMaxNoParticles(int i) {
		mMaxNoParticles = i;
	}

	public void setParticleModifier(IParticleModifier iparticlemodifier) {
		mParticleModifier = iparticlemodifier;
	}

	public void applyModifier(ParticleStore particlestore) {
		int i = 0;
		mNonEmptyBins.clear();
		Iterator<Particle> iterator = particlestore.iterator();
		ArrayList<Particle> arraylist = new ArrayList<Particle>();
		float af[] = new float[particlestore.size()];
		float f = 0.0F;
		int j = 0;
		while (iterator.hasNext()) {
			Particle particle = (Particle) iterator.next();
			f = (float) ((double) f + particle.getWeight());
			af[j++] = f;
		}
		double d = 1.7976931348623157E+308D;
		do {
			float f1 = f * mRandom.nextFloat();
			int k = 0;
			try {
				k = binarySearch(af, 0, af.length, f1);
			} catch (Exception exception) {
				System.out.println((new StringBuilder()).append(f).append("\t")
						.append(particlestore.size()).toString());
				System.exit(1);
			}
			Particle particle1 = (Particle) particlestore.get(k).makeClone();
			particle1.setWeight(1.0D);
			mParticleModifier.applyModifier(particle1);
			Object obj = mBinClassifier.getBin(particle1);
			if (!mNonEmptyBins.contains(obj)) {
				mNonEmptyBins.add(obj);
				i++;
			}
			arraylist.add(particle1);
			if (arraylist.size() > mMinNoParticles) {
				d = (float) (i - 1) / (2.0F * lEpsilon);
				double d1 = (double) (1 - 2 / (9 * (i - 1)))
						+ Math.sqrt(2 / (9 * (i - 1))) * (double) mNormalDistZ;
				d *= Math.pow(d1, 3D);
			}
		} while ((double) arraylist.size() < d
				&& arraylist.size() < mMaxNoParticles);
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
