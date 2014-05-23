package org.jxsens.filters.pf;

public abstract class Particle {

	protected double mWeight;

	public abstract Object makeClone();

	public Particle(double d) {
		mWeight = d;
	}

	public double getWeight() {
		return mWeight;
	}

	public void setWeight(double d) {
		mWeight = d;
	}
}
