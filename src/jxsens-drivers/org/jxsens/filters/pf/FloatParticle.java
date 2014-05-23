package org.jxsens.filters.pf;

public class FloatParticle extends Particle {

	private float mState[];

	public FloatParticle(int i, double d) {
		super(d);
		mState = new float[i];
	}

	public FloatParticle(float af[], double d) {
		super(d);
		mState = af;
	}

	public FloatParticle makeClone() {
		float af[] = new float[mState.length];
		System.arraycopy(mState, 0, af, 0, mState.length);
		return new FloatParticle(af, mWeight);
	}

	public float[] getState() {
		return mState;
	}

}
