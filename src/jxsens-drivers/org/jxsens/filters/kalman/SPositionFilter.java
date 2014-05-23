package org.jxsens.filters.kalman;

import Jama.Matrix;
import java.util.ArrayList;

public class SPositionFilter extends PositionFilter {
	
	private class StepData {
		@SuppressWarnings("unused")
		double mTimeGap;
		Matrix mTrans;
		Matrix mPriorEst;
		Matrix mPostEst;
		Matrix mPriorP;
		Matrix mPostP;
	}

	protected ArrayList<StepData> mStepData;
	protected boolean mLogging;

	public SPositionFilter(double d, double d1, boolean flag) {
		super(d, d1);
		mLogging = false;
		mLogging = flag;
		if (mLogging) {
			mStepData = new ArrayList<StepData>();
		}
	}

	public void update(double d, double d1) {
		StepData stepdata = new StepData();
		if (!mFirstCycle) {
			stepdata.mTimeGap = d1;
			predict(d1);
			stepdata.mTrans = mTrans;
		} else {
			mStepData = new ArrayList<StepData>();
			stepdata.mTimeGap = 0.0D;
			mFirstCycle = false;
		}
		
		stepdata.mPriorEst = mXMinus;
		stepdata.mPriorP = mPMinus;
		computeGain();
		assimilateMeasurement(new Matrix(new double[] { d }, 1));
		computeErrorCov();
		stepdata.mPostEst = mX;
		stepdata.mPostP = mP;
		if (mLogging) {
			mStepData.add(stepdata);
		}
	}

	public Position[] getSmoothedPositions() {
		int i = mStepData.size();
		Matrix m[] = new Matrix[mStepData.size()];
		Matrix m1[] = new Matrix[mStepData.size()];
		m[i - 1] = mStepData.get(i - 1).mPostEst;
		m1[i - 1] = mStepData.get(i - 1).mPostP;
		Position aposition[] = new Position[i];
		aposition[i - 1] = new Position(
				new double[] { m[i - 1].get(0, 0) }, new double[] { mP
						.get(0, 0) });
		for (int j = i - 2; j >= 0; j--) {
            Matrix matrix = mStepData.get(j).mPostP.times(mStepData.get(j + 1).mTrans.transpose().times(mStepData.get(j + 1).mPriorP.inverse()));
            Matrix matrix1 = matrix.times(m[j + 1].minus(mStepData.get(j + 1).mPriorEst));
            m[j] = mStepData.get(j).mPostEst.plus(matrix1);
            m1[j] = mStepData.get(j).mPostP.plus(matrix.times(m1[j + 1].minus(mStepData.get(j + 1).mPriorP)).times(matrix.transpose()));
            aposition[j] = new Position(new double[] {
                m[j].get(0, 0)
            }, new double[] {
                m1[j].get(0, 0)
            });
        }

        return aposition;
    }

	public void startLogging() {
		clear();
		mLogging = true;
	}

	public void stopLogging() {
		mLogging = false;
	}

	public void clear() {
		mStepData = new ArrayList<StepData>();
	}

	public boolean isLogging() {
		return mLogging;
	}
}
