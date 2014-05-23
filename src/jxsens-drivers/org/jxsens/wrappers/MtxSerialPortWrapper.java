package org.jxsens.wrappers;

import org.jxsens.MtxListener;

public class MtxSerialPortWrapper extends AbstractSerialPortWrapper implements IMtxWrapper {

	private MtxWrapper wrapper;
	
	public MtxSerialPortWrapper(String s, int b) throws Exception {
		super(s, b);
		wrapper = new MtxWrapper();
	}

	@Override
	public void packetReader() {
		wrapper.processNewData(inputStream);
	}

	@Override
	public void addListener(MtxListener listener) {
		wrapper.addListener(listener);
	}
}
