package org.jxsens.filters.kalman;

public class NDSPositionFilter {

	private SPositionFilter mFilters[];
	private boolean mLogging;

	public NDSPositionFilter(double ad[], double ad1[], int i, boolean flag) {
		mLogging = false;
		mLogging = flag;
		mFilters = new SPositionFilter[i];
		for (int j = 0; j < i; j++) {
			mFilters[j] = new SPositionFilter(ad[j], ad1[j], flag);
		}
	}

	public void seed(double ad[], double ad1[], double ad2[], double ad3[]) {
		for (int i = 0; i < mFilters.length; i++) {
			mFilters[i].seed(ad[i], ad1[i], ad2[i], ad3[i]);
		}
	}

	public void update(double ad[], double d) {
		for (int i = 0; i < mFilters.length; i++) {
			mFilters[i].update(ad[i], d);
		}
	}

	public Position getPredictedPosition(double d) {
		double ad[] = new double[mFilters.length];
		double ad1[] = new double[mFilters.length];
		for (int i = 0; i < mFilters.length; i++) {
			ad[i] = mFilters[i].getPredictedPosition(d).getPosition()[0];
			ad1[i] = mFilters[i].getPredictedPosition(d).getVariance()[0];
		}

		return new Position(ad, ad1);
	}

	public Position[] getSmoothedPositions() {
		Position aposition[][] = new Position[mFilters.length][];
		for (int i = 0; i < mFilters.length; i++) {
			aposition[i] = mFilters[i].getSmoothedPositions();
		}

		int j = aposition[0].length;
		Position aposition1[] = new Position[j];
		for (int k = 0; k < j; k++) {
			double ad[] = new double[mFilters.length];
			double ad1[] = new double[mFilters.length];
			for (int l = 0; l < mFilters.length; l++) {
				ad[l] = aposition[l][k].getPosition()[0];
				ad1[l] = aposition[l][k].getVariance()[0];
			}

			aposition1[k] = new Position(ad, ad1);
		}

		return aposition1;
	}

	public void startLogging() {
		mLogging = true;
		for (int i = 0; i < mFilters.length; i++) {
			mFilters[i].startLogging();
		}
		clear();
	}

	public void stopLogging() {
		mLogging = false;
		for (int i = 0; i < mFilters.length; i++) {
			mFilters[i].stopLogging();
		}
	}

	public void clear() {
		for (int i = 0; i < mFilters.length; i++) {
			mFilters[i].clear();
		}
	}

	public boolean isLogging() {
		return mLogging;
	}

	public Velocity getEstimatedVelocity() {
		double ad[] = new double[mFilters.length];
		double ad1[] = new double[mFilters.length];
		for (int i = 0; i < mFilters.length; i++) {
			ad[i] = mFilters[i].getEstimatedVelocity().getVelocity()[0];
			ad1[i] = mFilters[i].getEstimatedVelocity().getVariance()[0];
		}
		return new Velocity(ad, ad1);
	}
}
