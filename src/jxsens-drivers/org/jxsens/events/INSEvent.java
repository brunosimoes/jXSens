package org.jxsens.events;

import org.jxsens.logging.ILoggableEvent;
import org.jxsens.logging.LogItem;
import org.jxsens.logging.LogItemBuilder;

public class INSEvent extends TimedEvent implements ILoggableEvent {

	protected double mSensorAccel[];
	protected double mSensorAngularVelocity[];
	protected double mSensorMagneticField[];
	protected double mTemperature;
	
	public INSEvent() {
		mTemperature = 4.9406564584124654E-324D;
	}

	public INSEvent(double d, double ad[], double ad1[], double ad2[], double d1) {
		super(d);
		mTemperature = 4.9406564584124654E-324D;
		mTemperature = d1;
		mSensorAccel = ad;
		mSensorAngularVelocity = ad1;
		mSensorMagneticField = ad2;
	}

	public INSEvent(double d, double ad[], double ad1[], double ad2[]) {
		super(d);
		mTemperature = 4.9406564584124654E-324D;
		mSensorAccel = ad;
		mSensorAngularVelocity = ad1;
		mSensorMagneticField = ad2;
	}

	public double getTemperature() {
		return mTemperature;
	}

	public double[] getSensorAccel() {
		return mSensorAccel;
	}

	public double[] getSensorAngularVelocity() {
		return mSensorAngularVelocity;
	}

	public double[] getSensorMagneticField() {
		return mSensorMagneticField;
	}

	public void buildLogItem(LogItemBuilder logitembuilder) {
		logitembuilder.append(getTime());
		logitembuilder.append(mSensorAccel);
		logitembuilder.append(mSensorAngularVelocity);
		logitembuilder.append(mSensorMagneticField);
		logitembuilder.append(mTemperature);
	}

	public void fromLogItem(LogItem logitem) {
		setTime(Double.parseDouble(logitem.readStr()));
		mSensorAccel = logitem.readDoubleAry(3);
		mSensorAngularVelocity = logitem.readDoubleAry(3);
		mSensorMagneticField = logitem.readDoubleAry(3);
		mTemperature = Double.parseDouble(logitem.readStr());
	}
	
}