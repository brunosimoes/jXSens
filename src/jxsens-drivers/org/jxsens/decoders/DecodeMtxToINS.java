package org.jxsens.decoders;

import org.jxsens.MtxListener;
import org.jxsens.MtxPacket;
import org.jxsens.MtxReporter;
import org.jxsens.events.INSEvent;

public class DecodeMtxToINS extends MtxReporter implements MtxListener {

	public DecodeMtxToINS() {
	}

	public void newEvent(Object obj, MtxPacket mtxpacket) {
		if (mtxpacket.getMid() == MtxPacket.XSENS_MTDATA)
			notifyListeners(toINSEvent(mtxpacket));
	}

	private INSEvent toINSEvent(MtxPacket packet) {
		double d = packet.getFloatValue(0);
		double ad[] = { packet.getFloatValue(4), packet.getFloatValue(8), packet.getFloatValue(12) };
		double ad1[] = { packet.getFloatValue(16), packet.getFloatValue(20), packet.getFloatValue(24) };
		double ad2[] = { packet.getFloatValue(28), packet.getFloatValue(32), packet.getFloatValue(36) };
		double i = packet.getUnsignedShortValue(40);
		return new INSEvent(i * packet.getSamplePeriod(), ad, ad1, ad2, d);
	}

	public void newEvent(Object a, Object b) {
		newEvent(a, (MtxPacket) b);
	}
}