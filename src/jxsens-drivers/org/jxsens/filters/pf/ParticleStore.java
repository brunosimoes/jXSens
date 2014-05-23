package org.jxsens.filters.pf;

import java.util.ArrayList;
import java.util.Iterator;

import org.jxsens.MtxListener;
import org.jxsens.MtxReporter;

public class ParticleStore extends MtxReporter implements MtxListener {

	private ArrayList<Particle> mParticles;

	public ParticleStore(ArrayList<Particle> arraylist) {
		mParticles = arraylist;
	}

	public int size() {
		return mParticles.size();
	}

	public Particle get(int i) {
		return mParticles.get(i);
	}

	public Iterator<Particle> iterator() {
		return mParticles.iterator();
	}

	public void setParticles(ArrayList<Particle> arraylist) {
		mParticles = arraylist;
	}

	public void newEvent(Object obj, IParticleFilterComponent pfc) {
		applyFilterComponent(pfc);
	}

	public void applyFilterComponent(IParticleFilterComponent pfc) {
		pfc.applyModifier(this);
		notifyListeners(this);
	}

	public void normaliseWeights() {
		double d = 0.0D;
		for (int i = 0; i < mParticles.size(); i++) {
			d += ((Particle) mParticles.get(i)).getWeight();
		}

		for (int j = 0; j < mParticles.size(); j++) {
			Particle particle = (Particle) mParticles.get(j);
			particle.setWeight(particle.getWeight() / d);
		}
	}

	public double getEss() {
		double d = getCoeffOfVariation();
		double d1 = (double) mParticles.size() / (1.0D + d);
		return d1;
	}

	public double getCoeffOfVariation() {
		double d = 0.0D;
		int i = mParticles.size();
		for (int j = 0; j < i; j++) {
			Particle particle = (Particle) mParticles.get(j);
			d += ((double) i * particle.getWeight() - 1.0D)
					* ((double) i * particle.getWeight() - 1.0D);
		}

		d /= i;
		return d;
	}

	public void newEvent(Object a, Object b) {
		newEvent(a, (IParticleFilterComponent) b);
	}
}
