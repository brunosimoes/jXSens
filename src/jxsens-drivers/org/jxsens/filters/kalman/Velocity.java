package org.jxsens.filters.kalman;

public class Velocity {

	private double mVelocity[];
	private double mVariance[];

	public Velocity(double ad[]) {
		if (ad == null)
			throw new IllegalArgumentException("Velocity array cannot be null");
		else {
			mVelocity = ad;
			return;
		}
	}

	public Velocity(double ad[], double ad1[]) {
		if (ad == null) {
			throw new IllegalArgumentException("Velocity array cannot be null");
		}
		if (ad.length != ad1.length) {
			throw new IllegalArgumentException(
					"Velocity and variance arrays must be of the same length");
		} else {
			mVelocity = ad;
			mVariance = ad1;
			return;
		}
	}

	public double[] getVelocity() {
		return mVelocity;
	}

	public double[] getVariance() {
		return mVariance;
	}

	public double getDimensionality() {
		return (double) mVelocity.length;
	}
}
