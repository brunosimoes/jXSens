package org.jxsens.filters.kalman;

import Jama.Matrix;

public class PositionFilter extends KalmanFilter {

	protected double mMaxAccel;
	protected boolean mFirstCycle;

	public PositionFilter(double d, double d1) {
		mFirstCycle = true;
		mMaxAccel = d;
		mR = new Matrix(new double[] { d1 }, 1);
		mH = new Matrix(new double[] { 1.0D, 0.0D }, 1);
	}

	public void seed(double d, double d1, double d2, double d3) {
		Matrix matrix = new Matrix(new double[] { d, d1 }, 2);
		Matrix matrix1 = new Matrix(new double[] { d2, 0.0D, 0.0D, d3 }, 2);
		seedFilter(matrix, matrix1);
		mFirstCycle = true;
	}

	public void update(double d, double d1) {
		if (!mFirstCycle) {
			predict(d1);
		} else {
			mFirstCycle = false;
		}
		computeGain();
		assimilateMeasurement(new Matrix(new double[] { d }, 1));
		computeErrorCov();
	}

	public Position getPredictedPosition(double d) {
		if (d < 0.0D) {
			throw new IllegalArgumentException(
					"The time step must be greater or equal to 0");
		}
		if (d == 0.0D) {
			return new Position(new double[] { getMeasurement(mX).get(0, 0) },
					new double[] { mP.get(0, 0) });
		} else {
			predict(d);
			return new Position(new double[] { getMeasurement(mXMinus)
					.get(0, 0) }, new double[] { mPMinus.get(0, 0) });
		}
	}

	public Velocity getEstimatedVelocity() {
		return new Velocity(new double[] { mX.get(1, 0) }, new double[] { mP
				.get(1, 1) });
	}

	protected void predict(double d) {
		mQ = new Matrix(new double[] { 2D * d * d, 3D * d, 3D * d, 6D }, 2);
		mQ = mQ.times((mMaxAccel * mMaxAccel * d) / 6D);
		mTrans = new Matrix(new double[] { 1.0D, 0.0D, d, 1.0D }, 2);
		project();
	}
}
