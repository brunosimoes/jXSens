package org.jxsens.decoders;

import org.jxsens.events.INSEvent;
import org.jxsens.logging.LogItem;
import org.jxsens.logging.LogItemBuilder;
import org.jxsens.util.MathUtils;

import Jama.Matrix;

public class AttitudeEvent extends INSEvent {

	protected double mGlobalAccel[];
	protected double mGlobalAngularVelocity[];
	protected double mGlobalMagneticField[];
	protected Matrix mSensorToGlobalRotationMatrix;
	protected Matrix mGlobalToSensorRotationMatrix;

	public AttitudeEvent() {}

	public AttitudeEvent(INSEvent e, Matrix matrix) {
		super(e.getTime(), e.getSensorAccel(), e.getSensorAngularVelocity(), e.getSensorMagneticField(), e.getTemperature());
		setMatrix(matrix);
	}

	public AttitudeEvent(double d, double ad[], double ad1[], double ad2[], double d1, Matrix matrix) {
		super(d, ad, ad1, ad2, d1);
		setMatrix(matrix);
	}

	public AttitudeEvent(double d, double ad[], double ad1[], double ad2[], Matrix matrix) {
		super(d, ad, ad1, ad2);
		setMatrix(matrix);
	}

	private void setMatrix(Matrix matrix) {
		mSensorToGlobalRotationMatrix = matrix;
		mGlobalToSensorRotationMatrix = matrix.transpose();
		mGlobalAccel = MathUtils.matrixTimesVector(mSensorToGlobalRotationMatrix, mSensorAccel);
		mGlobalAngularVelocity = MathUtils.matrixTimesVector(mSensorToGlobalRotationMatrix, mSensorAngularVelocity);
		if (mSensorMagneticField != null)
			mGlobalMagneticField = MathUtils.matrixTimesVector(mSensorToGlobalRotationMatrix, mSensorMagneticField);
	}

	public double[] getGlobalAccel() {
		return mGlobalAccel;
	}

	public double[] getGlobalAngularVelocity() {
		return mGlobalAngularVelocity;
	}

	public double[] getGlobalMagneticField() {
		return mGlobalMagneticField;
	}

	public Matrix getSensorToGlobalRotationMatrix() {
		return mSensorToGlobalRotationMatrix;
	}

	public Matrix getGlobalToSensorRotationMatrix() {
		return mGlobalToSensorRotationMatrix;
	}

	public void buildLogItem(LogItemBuilder logitembuilder) {
		super.buildLogItem(logitembuilder);
		logitembuilder.append(mSensorToGlobalRotationMatrix.getColumnPackedCopy());
	}

	public void fromLogItem(LogItem logitem) {
		super.fromLogItem(logitem);
		setMatrix(new Matrix(logitem.readDoubleAry(9), 3));
	}
}