package org.jxsens.decoders;

import org.jxsens.MtxListener;
import org.jxsens.MtxPacket;
import org.jxsens.MtxReporter;

public class DecodeMtxToEuler extends MtxReporter implements MtxListener {

	public DecodeMtxToEuler() {}

	public void newEvent(Object obj, MtxPacket packet) {
		if (packet.getMid() == MtxPacket.XSENS_MTDATA)
			notifyListeners(toEulerEvent(packet));
	}

	private MtxNavigationEvent toEulerEvent(MtxPacket packet) {
		double ad[] = {packet.getFloatValue(0), packet.getFloatValue(4), packet.getFloatValue(8) };
		return new MtxNavigationEvent(ad);
	}

	public void newEvent(Object a, Object b) {
		newEvent(a, (MtxPacket) b);
	}
}