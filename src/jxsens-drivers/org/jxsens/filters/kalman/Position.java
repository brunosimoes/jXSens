package org.jxsens.filters.kalman;

public class Position {

	private double mPosition[];
	private double mVariance[];

	public Position(double ad[]) {
		if (ad == null) {
			throw new IllegalArgumentException("Position array cannot be null");
		} else {
			mPosition = ad;
			return;
		}
	}

	public Position(double ad[], double ad1[]) {
		if (ad == null) {
			throw new IllegalArgumentException("Position array cannot be null");
		}
		if (ad.length != ad1.length) {
			throw new IllegalArgumentException(
					"Position and variance arrays must be of the same length");
		} else {
			mPosition = ad;
			mVariance = ad1;
			return;
		}
	}

	public double[] getPosition() {
		return mPosition;
	}

	public double[] getVariance() {
		return mVariance;
	}

	public double getDimensionality() {
		return (double) mPosition.length;
	}
}
