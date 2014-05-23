package org.jxsens;

public class MtxPacket {

	public static final byte XSENS_PREAMBLE = -6;
	public static final byte XSENS_BID = -1;
	public static final byte XSENS_REQDID = 0;
	public static final byte XSENS_DEVICE_ID = 1;
	public static final byte XSENS_INITMT = 2;
	public static final byte XSENS_INITMTRESULTS = 3;
	public static final byte XSENS_PERIOD = 4;
	public static final byte XSENS_PERIODACK = 5;
	public static final byte XSENS_DATA_LENGTH = 11;
	public static final byte XSENS_WAKEUP = 62;
	public static final byte XSENS_WAKEUPACK = 63;
	public static final byte XSENS_PRODUCT_CODE = 29;
	public static final byte XSENS_FIRMWARE_REV = 19;
	public static final byte XSENS_ERROR = 66;
	public static final byte XSENS_CONFIGURATION = 13;
	public static final byte XSENS_MTDATA = 50;
	public static final byte XSENS_RESET = 64;
	public static final byte XSENS_GOTO_CONFIG = 48;
	public static final byte XSENS_GOTO_CONFIG_ACK = 49;
	public static final byte XSENS_GOTO_MEASUREMENT = 16;
	public static final byte XSENS_GOTO_MEASUREMENTACK = 17;
	public static final byte XSENS_REQCONFIGURATION = 12;
	public static final byte XSENS_REQFWREV = 18;
	public static final byte XSENS_REQPRODUCTCODE = 28;
	public static final byte XSENS_REQDATALENGTH = 10;
	public static final byte XSENS_OUTPUTSKIPFACTOR = -44;
	public static final byte XSENS_OUTPUTSKIPFACTORACK = -43;
	public static final byte XSENS_SYNCINSETTINGS = -42;
	public static final byte XSENS_SYNCINSETTINGSACK = -41;
	public static final byte XSENS_REQBAUDRATE = 24;
	public static final byte XSENS_REQBAUDRATEACK = 25;
	public static final byte XSENS_OUTPUTMODE = -48;
	public static final byte XSENS_OUTPUTMODEACK = -47;
	public static final byte XSENS_OUTPUTSETTINGS = -46;
	public static final byte XSENS_OUTPUTSETTINGSACK = -45;
	public static final byte XSENS_AMD = -94;
	public static final byte XSENS_RESET_ORIENTATION = -92;
	public static final byte XSENS_REQ_DATA = 52;
	public static final byte XSENS_RESTOREFACTORYDEFAULTS = 14;
	
	private byte mMid;
	private byte mData[];
	private byte mChecksum;
	private double mSamplePeriod;

	public MtxPacket(byte mMid, byte mData[], byte mChecksum) {
		this.mSamplePeriod = 1.0D;
		this.mMid = mMid;
		this.mData = mData;
		this.mChecksum = mChecksum;
	}

	public MtxPacket(byte mMid, byte mData[]) {
		this.mSamplePeriod = 1.0D;
		this.mMid = mMid;
		this.mData = mData;
		setValidChecksum();
	}

	public void setSamplePeriod(double d) {
		mSamplePeriod = d;
	}

	public double getSamplePeriod() {
		return mSamplePeriod;
	}

	public void setValidChecksum() {
		byte data[] = toByteArray();
		int i = 0;
		for (int j = 1; j < data.length - 1; j++)
			i += data[j];

		mChecksum = (byte) (256 - i % 256);
	}

	public boolean isChecksumValid() {
		byte msg[] = toByteArray();
		int i = 0;
		for (int j = 1; j < msg.length - 1; j++)
			i += msg[j];

		byte byte0 = (byte) (256 - i % 256);
		return byte0 == mChecksum;
	}

	public byte getChecksum() {
		return mChecksum;
	}

	public byte[] getData() {
		return mData;
	}

	public byte getMid() {
		return mMid;
	}

	public int getDataLength() {
		return mData.length;
	}

	public byte[] toByteArray() {
		byte data[];
		if (mData != null) {
			boolean flag = mData.length > 254;
			int i = mData.length + 5;
			if (flag)
				i += 2;
			data = new byte[i];
			data[0] = -6;
			data[1] = -1;
			data[2] = mMid;
			if (!flag) {
				data[3] = (byte) mData.length;
			} else {
				data[3] = -1;
				data[4] = (byte) (mData.length >> 8 & 0xff);
				data[5] = (byte) (mData.length >> 0 & 0xff);
			}
			System.arraycopy(mData, 0, data, data.length - 1 - mData.length, mData.length);
			data[data.length - 1] = mChecksum;
		} else {
			data = new byte[5];
			data[0] = -6;
			data[1] = -1;
			data[2] = mMid;
			data[3] = 0;
			data[data.length - 1] = mChecksum;
		}
		return data;
	}

	public int getUnsignedByteValue(int i) {
		int j = 0;
		j += mData[i] & 0x7f;
		if (mData[i] >> 7 != 0)
			j += 128;
		return j;
	}

	public String getDigitsFromByte(int i) {
		int j = getUnsignedByteValue(i);
		int k = j >> 4;
		int l = j & 0xf;
		return "" + k + "" + l;
	}

	public String getDeviceID(int i) {
		return "" + getDigitsFromByte(i) + "" + getDigitsFromByte(i + 1) + "" + getDigitsFromByte(i + 2) + "" + getDigitsFromByte(i + 3);
	}

	public int getUnsignedShortValue(int i) {
		return getUnsignedByteValue(i) * 256 + getUnsignedByteValue(i + 1);
	}

	public float getFloatValue(int i) {
		int j = (getUnsignedByteValue(i) << 24) + (getUnsignedByteValue(i + 1) << 16) + (getUnsignedByteValue(i + 2) << 8) + getUnsignedByteValue(i + 3);
		return Float.intBitsToFloat(j);
	}

}