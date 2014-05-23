package org.jxsens.decoders;

import org.jxsens.MtxListener;
import org.jxsens.MtxPacket;
import org.jxsens.MtxReporter;

import Jama.Matrix;

public class DecodeMtxToAttitude extends MtxReporter implements MtxListener {

	public DecodeMtxToAttitude() {}

	public void newEvent(Object obj, MtxPacket packet) {
		if (packet.getMid() == MtxPacket.XSENS_MTDATA)
			notifyListeners(toAttitudeEvent(packet));
	}

	private AttitudeEvent toAttitudeEvent(MtxPacket packet) {
		double d = packet.getFloatValue(0);
		double ad[] = { packet.getFloatValue(4), 
						packet.getFloatValue(8), 
						packet.getFloatValue(12)};

		double ad1[] = { packet.getFloatValue(16), 
						 packet.getFloatValue(20), 
						 packet.getFloatValue(24)};
	
		double ad2[] = { packet.getFloatValue(28), 
						 packet.getFloatValue(32), 
						 packet.getFloatValue(36)};
		
		Matrix matrix = new Matrix(new double[] { 
										packet.getFloatValue(40), 
										packet.getFloatValue(44), 
										packet.getFloatValue(48),
										packet.getFloatValue(52), 
										packet.getFloatValue(56), 
										packet.getFloatValue(60), 
										packet.getFloatValue(64),
										packet.getFloatValue(68), 
										packet.getFloatValue(72) }, 3);
		
		double i = packet.getUnsignedShortValue(76);
		return new AttitudeEvent(i * packet.getSamplePeriod(), ad, ad1, ad2, d, matrix);
	}

	public void newEvent(Object a, Object b) {
		newEvent(a, (MtxPacket) b);
	}
}