package org.jxsens.decoders;

import org.jxsens.MtxListener;
import org.jxsens.MtxReporter;
import org.jxsens.events.INSEvent;

public class INSCalibrator extends MtxReporter implements MtxListener {

	private double mAccelBias[] = { 0.0D, 0.0D, 0.0D };
	private double mAccelScale[] = { 1.0D, 1.0D, 1.0D };
	private double mAccelAlignment[] = { 0.0D, 1.5707963267948966D, 0.0D };
	private double mGyroBias[] = { 0.0D, 0.0D, 0.0D };
	private double mGyroScale[] = { 1.0D, 1.0D, 1.0D };
	@SuppressWarnings("unused")
	private double mGyroAlignment[] = { 0.0D, 0.0D, 0.0D, 0.0D, 1.5707963267948966D, 0.0D };
	
	public INSCalibrator() {
	}

	public INSCalibrator(double ad[], double ad1[], double ad2[], double ad3[], double ad4[], double ad5[]) {
		mAccelBias = ad;
		mGyroBias = ad1;
		mAccelScale = ad2;
		mGyroScale = ad3;
		mAccelScale = ad2;
		mGyroAlignment = ad5;
	}

	public void setGyroBias(double ad[]) {
		mGyroBias = ad;
	}

	public double[] getGyroBias() {
		return mGyroBias;
	}

	public void setGyroScaleFactor(double ad[]) {
		mGyroScale = ad;
	}

	public double[] getGyroScaleFactor() {
		return mGyroScale;
	}

	public void setAccelBias(double ad[]) {
		mAccelBias = ad;
	}

	public void setAccelAlignment(double ad[]) {
		mAccelAlignment = ad;
	}

	public double[] getAccelAlignment() {
		return mAccelAlignment;
	}

	public double[] getAccelBias() {
		return mAccelBias;
	}

	public void setAccelScaleFactor(double ad[]) {
		mAccelScale = ad;
	}

	public double[] getAccelScaleFactor() {
		return mAccelScale;
	}

	public void newEvent(Object obj, INSEvent insevent) {
		try {
			notifyListeners(getCalibratedEvent(insevent));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public INSEvent getCalibratedEvent(INSEvent insevent) {
		double ad[] = calibrateAccel(insevent.getSensorAccel());
		double ad1[] = calibrateGyro(insevent.getSensorAngularVelocity());
		INSEvent insevent1 = new INSEvent(insevent.getTime(), ad, ad1, insevent.getSensorMagneticField(), 0.0D);
		return insevent1;
	}

	private double[] calibrateAccel(double ad[]) {
		double ad1[] = scaleAndTranslatePoint(ad, mAccelBias, mAccelScale);
		return ad1;
	}

	private double[] calibrateGyro(double ad[]) {
		double ad1[] = scaleAndTranslatePoint(ad, mGyroBias, mGyroScale);
		return ad1;
	}

	private double[] scaleAndTranslatePoint(double ad[], double ad1[], double ad2[]) {
		return (new double[] { (ad[0] - ad1[0]) / ad2[0], (ad[1] - ad1[1]) / ad2[1], (ad[2] - ad1[2]) / ad2[2] });
	}

	public void newEvent(Object a, Object b) {
		newEvent(a, (INSEvent) b);
	}
}