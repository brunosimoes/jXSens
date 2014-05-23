package org.jxsens.decoders;

import org.jxsens.MtxListener;
import org.jxsens.MtxPacket;
import org.jxsens.MtxReporter;

public class DecodeMtxToRaw extends MtxReporter implements MtxListener {

	public DecodeMtxToRaw() {}

	public void newEvent(Object obj, MtxPacket mtxpacket) {
		if (mtxpacket.getMid() == MtxPacket.XSENS_MTDATA)
			notifyListeners(parseAsRawPacket(mtxpacket));
	}

	private MtxRawEvent parseAsRawPacket(MtxPacket packet) {
		int ai[] = { packet.getUnsignedShortValue(0), packet.getUnsignedShortValue(2), packet.getUnsignedShortValue(4) };
		int ai1[] = { packet.getUnsignedShortValue(6), packet.getUnsignedShortValue(8), packet.getUnsignedShortValue(10) };
		int ai2[] = { packet.getUnsignedShortValue(12), packet.getUnsignedShortValue(14), packet.getUnsignedShortValue(16) };
		int i = packet.getUnsignedShortValue(18);
		double j = packet.getUnsignedShortValue(20);
		return new MtxRawEvent(j * packet.getSamplePeriod(), ai, ai1, ai2, i);
	}

	public void newEvent(Object a, Object b) {
		newEvent(a, (MtxPacket) b);
	}
}