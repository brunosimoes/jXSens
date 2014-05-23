package org.jxsens.filters.kalman;

public class NDPositionFilter {

	private PositionFilter mFilters[];

	public NDPositionFilter(double ad[], double ad1[], int i) {
		mFilters = new PositionFilter[i];
		for (int j = 0; j < i; j++) {
			mFilters[j] = new PositionFilter(ad[j], ad1[j]);
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
