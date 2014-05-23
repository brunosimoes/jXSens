package org.jxsens.decoders;

import org.jxsens.MtxListener;
import org.jxsens.MtxPacket;
import org.jxsens.MtxReporter;
import org.jxsens.events.INSEvent;

public class DecodeMtxRawToINS extends MtxReporter implements MtxListener {

	private INSCalibrator mCalib;

	public DecodeMtxRawToINS(INSCalibrator calibrator) {
		mCalib = calibrator;
	}

	public void newEvent(Object obj, MtxRawEvent e) {
		double ad[] = { e.getAccel()[0], e.getAccel()[1], e.getAccel()[2] };
		double ad1[] = { e.getGyro()[0], e.getGyro()[1], e.getGyro()[2] };
		double ad2[] = { e.getMag()[0], e.getMag()[1], e.getMag()[2] };

		INSEvent insevent = new INSEvent(e.getTime(), ad, ad1, ad2, e.getTemp());
		notifyListeners(mCalib.getCalibratedEvent(insevent));
	}

	public void newEvent(Object a, Object b) {
		newEvent(a, (MtxPacket) b);
	}
}