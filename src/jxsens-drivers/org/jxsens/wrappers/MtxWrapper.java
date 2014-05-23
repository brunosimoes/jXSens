package org.jxsens.wrappers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jxsens.MtxPacket;
import org.jxsens.MtxReporter;

public class MtxWrapper extends MtxReporter {

	private static final int STATE_PREAMBLE = 0;
	private static final int STATE_BID = 1;
	private static final int STATE_MID = 2;
	private static final int STATE_LEN = 3;
	private static final int STATE_EXTLEN = 4;
	private static final int STATE_DATA = 5;
	private static final int STATE_CHECKSUM = 6;
	
	private int mState;
	private int mNoReadBytes;
	private byte mMid;
	private byte mData[];

	public MtxWrapper() throws Exception {
		mState = 0;
		mNoReadBytes = 1;
	}

	protected void send(OutputStream outputstream, byte msg[]) throws IOException {
		outputstream.write(msg);
		outputstream.flush();
	}

	protected void processNewData(InputStream inputstream) {
		try {
			while (true) {

				if (inputstream.available() < mNoReadBytes)
					break;

				byte data[] = new byte[mNoReadBytes];
				inputstream.read(data);

				switch (mState) {
					case STATE_PREAMBLE:
						if (data[0] == MtxPacket.XSENS_PREAMBLE)
							mState = STATE_BID;
						break;

					case STATE_BID:
						if (data[0] == MtxPacket.XSENS_BID)
							mState = STATE_MID;
						else
							mState = STATE_PREAMBLE;
						break;

					case STATE_MID:
						mMid = data[0];
						mState = STATE_LEN;
						break;

					case STATE_LEN:
						if (data[0] == MtxPacket.XSENS_BID) {
							mState = STATE_EXTLEN;
							mNoReadBytes = 2;
						} else {
							mNoReadBytes = data[0];
							if (mNoReadBytes < 0)
								mState = 0;
							else if (mNoReadBytes == 0) {
								mNoReadBytes = 1;
								mState = STATE_CHECKSUM;
								mData = null;
							} else {
								mState = STATE_DATA;
								mData = new byte[mNoReadBytes];
							}
						}
						break;

					case STATE_EXTLEN:
						mNoReadBytes = data[0] * 256 + data[1];
						mState = STATE_DATA;
						mData = new byte[mNoReadBytes];
						break;

					case STATE_DATA:
						System.arraycopy(data, 0, mData, 0, data.length);
						mState = STATE_CHECKSUM;
						mNoReadBytes = 1;
						break;

					case STATE_CHECKSUM:
						processXsensMessage(mMid, mData, data[0]);
						mState = STATE_PREAMBLE;
						mNoReadBytes = 1;
						break;
				}
			}
		} catch (IOException ioexception) {
			System.err.println("Error occurred when reading from serial port");
			ioexception.printStackTrace();
			System.exit(1);
		}
	}

	private void processXsensMessage(byte mMid, byte mData[], byte mChecksum) {
		notifyListeners(new MtxPacket(mMid, mData, mChecksum));
	}
}
