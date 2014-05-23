package org.jxsens.filters.pf;

import java.util.ArrayList;

public class CompoundParticleModifier extends IParticleModifier {

	private ArrayList<IParticleModifier> mMeasurements;

	public CompoundParticleModifier() {
		mMeasurements = new ArrayList<IParticleModifier>();
	}

	public void addMeasurement(IParticleModifier iparticlemodifier) {
		mMeasurements.add(iparticlemodifier);
	}

	public void clearMeasurements() {
		mMeasurements.clear();
	}

	public void removeMeasurement(IParticleModifier iparticlemodifier) {
		mMeasurements.remove(iparticlemodifier);
	}

	public void applyModifier(Particle particle) {
		for (int i = 0; i < mMeasurements.size(); i++) {
			((IParticleModifier) mMeasurements.get(i)).applyModifier(particle);
		}
	}
}
