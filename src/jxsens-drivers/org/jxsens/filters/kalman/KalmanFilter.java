package org.jxsens.filters.kalman;

import Jama.Matrix;

public abstract class KalmanFilter {

	protected Matrix mX;
	protected Matrix mXMinus;
	protected Matrix mP;
	protected Matrix mPMinus;
	protected Matrix mTrans;
	protected Matrix mH;
	protected Matrix mQ;
	protected Matrix mR;
	protected Matrix mK;

	public KalmanFilter() {
	}

	protected void seedFilter(Matrix matrix, Matrix matrix1) {
		mPMinus = matrix1;
		mXMinus = matrix;
	}

	protected void computeGain() {
		mK = mPMinus.times(mH.transpose()).times(
				mH.times(mPMinus).times(mH.transpose()).plus(mR).inverse());
	}

	protected void assimilateMeasurement(Matrix matrix) {
		mX = mXMinus.plus(mK.times(matrix.minus(mH.times(mXMinus))));
	}

	protected void computeErrorCov() {
		mP = Matrix.identity(mK.getRowDimension(), mH.getColumnDimension())
				.minus(mK.times(mH)).times(mPMinus);
	}

	protected void project() {
		mXMinus = mTrans.times(mX);
		mPMinus = mTrans.times(mP).times(mTrans.transpose()).plus(mQ);
	}

	protected Matrix getMeasurement(Matrix matrix) {
		return mH.times(matrix);
	}
}
