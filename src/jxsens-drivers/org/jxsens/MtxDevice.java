package org.jxsens;

import java.io.IOException;
import java.util.BitSet;
import java.util.TooManyListenersException;

import org.jxsens.wrappers.IMtxWrapper;

public class MtxDevice extends MtxReporter implements MtxListener {

	/** Offsets for the configuration message **/
	public static int MASTER_DEVICE_ID_OFFSET = 0;
	public static int SAMPLING_PERIOD_OFFSET = 4;
	public static int OUTPUT_SKIP_FACTOR_OFFSET = 6;
	public static int NUMBER_OF_DEVICES_OFFSET = 96;
	public static int DEVICE_ID_OFFSET = 98;
	public static int DATA_LENGTH_OFFSET = 102;
	public static int OUTPUT_MODE_OFFSET = 104;
	public static int OUTPUT_SETTINGS_OFFSET = 106; //108
	
	public static final int XSENS_DATA_TEMP = 1;
	public static final int XSENS_DATA_CALIB = 2;
	public static final int XSENS_DATA_ORIEN = 4;
	public static final int XSENS_DATA_AUX = 8;
	public static final int XSENS_DATA_RAW = 16384;
	public static final int XSENS_RESET_STORE = 0;
	public static final int XSENS_RESET_HEADING = 1;
	public static final int XSENS_RESET_GLOBAL = 2;
	public static final int XSENS_RESET_OBJECT = 3;
	public static final int XSENS_RESET_ALIGN = 4;
	public static final int XSENS_OUTPUTSETTINGS_DEFAULT = 9;

	public static byte[] ORIENTATION_OUTPUT_MODE = { 0, 4 };
	public static byte[] MATRIX_OUTPUT_MODE = { (byte) (7 >> 8 & 0x40), (byte) (7 & 0xf) };
	public static byte[] MATRIX_OUTPUT_SETTINGS = { 0, 0, (byte) (9 >> 8 & 7), (byte) (9 & 0x7f) };
	public static byte[] EULER_OUTPUT_SETTINGS = { 0, 0, 0, 4 };

	private IMtxWrapper mLowLevel;
	private Lock mLock;
	private int mExpectedPacketId;
	private MtxPacket mPacket;
	private int mWaitForReplyTimeout;
	private boolean mInControlMode;
	private double mPeriod;

	public MtxDevice(IMtxWrapper wrapper) throws MtxCommException, TooManyListenersException {
		this.mLock = new Lock();
		this.mWaitForReplyTimeout = 2000;
		this.mLowLevel = wrapper;
		this.mLowLevel.addListener(this);
		reset();
		updateLocalFrequency();
	}

	public void setTimeoutTime(int i) {
		mWaitForReplyTimeout = i;
	}

	public synchronized void newEvent(Object obj, MtxPacket mtxpacket) {
		if (mExpectedPacketId != -1 && (mtxpacket.getMid() == mExpectedPacketId || mtxpacket.getMid() == MtxPacket.XSENS_ERROR)) {
			mExpectedPacketId = -1;
			mPacket = mtxpacket;
			notifyAll();
		}
		else if (mtxpacket.getMid() == MtxPacket.XSENS_MTDATA || mtxpacket.getMid() == MtxPacket.XSENS_ERROR) {
			mtxpacket.setSamplePeriod(mPeriod);
			notifyListeners(mtxpacket);
		}
	}

	public double getActualPeriod() {
		return mPeriod;
	}

	public void setConfigMode() throws MtxCommException {
		sendEitherStateMessage(new MtxPacket(MtxPacket.XSENS_GOTO_CONFIG, null));
		mInControlMode = true;
	}

	public void setMeasurementMode() throws MtxCommException {
		sendConfigMessage(new MtxPacket(MtxPacket.XSENS_GOTO_MEASUREMENT, null));
		mInControlMode = false;
	}

	public MtxConfiguration getConfiguration() throws MtxCommException {
		MtxPacket packet = sendConfigMessage(new MtxPacket(MtxPacket.XSENS_REQCONFIGURATION, null));
		MtxConfiguration config = new MtxConfiguration();
		config.setMasterDeviceID(packet.getDeviceID(MASTER_DEVICE_ID_OFFSET));
		config.setDeviceID(packet.getDeviceID(DEVICE_ID_OFFSET));
		config.setSamplingPeriod(packet.getUnsignedShortValue(SAMPLING_PERIOD_OFFSET));
		config.setOutputSkipFactor(packet.getUnsignedShortValue(OUTPUT_SKIP_FACTOR_OFFSET));
		config.setNumberOfDevices(packet.getUnsignedShortValue(NUMBER_OF_DEVICES_OFFSET));
		config.setDataLength(packet.getUnsignedShortValue(DATA_LENGTH_OFFSET));
		config.setOutputMode(packet.getUnsignedShortValue(OUTPUT_MODE_OFFSET));
		config.setOutputSettings(packet.getUnsignedShortValue(OUTPUT_SETTINGS_OFFSET));
		return config;
	}

	public String getFirmwareRevision() throws MtxCommException {
		MtxPacket mtxpacket = sendConfigMessage(new MtxPacket(MtxPacket.XSENS_REQFWREV, null));
		return mtxpacket.getData()[0] + "." + mtxpacket.getData()[1] + "." + mtxpacket.getData()[2];
	}

	public String requestProductCode() throws MtxCommException {
		MtxPacket mtxpacket = sendConfigMessage(new MtxPacket(MtxPacket.XSENS_REQPRODUCTCODE, null));
		return new String(mtxpacket.getData());
	}

	public int requestDataLength() throws MtxCommException {
		MtxPacket mtxpacket = sendConfigMessage(new MtxPacket(MtxPacket.XSENS_REQDATALENGTH, null));
		return mtxpacket.getUnsignedShortValue(0);
	}

	public void requestData() throws MtxCommException {
		sendMessageAndForget(new MtxPacket((byte) MtxPacket.XSENS_REQ_DATA, null));
	}

	public String requestDeviceId() throws MtxCommException {
		MtxPacket mtxpacket = sendConfigMessage(new MtxPacket((byte) MtxPacket.XSENS_REQDID, null));
		return mtxpacket.getDeviceID(0);
	}

	public int requestPeriod() throws MtxCommException {
		MtxPacket mtxpacket = sendConfigMessage(new MtxPacket((byte) MtxPacket.XSENS_PERIOD, null));
		return mtxpacket.getUnsignedShortValue(0);
	}

	public void setPeriod(int i) throws MtxCommException {
		byte data[] = { (new Integer(i >> 8)).byteValue(), (new Integer(i)).byteValue() };
		sendConfigMessage(new MtxPacket((byte) MtxPacket.XSENS_PERIOD, data));
		updateLocalFrequency();
	}

	public int requestOutputSkipFactor() throws MtxCommException {
		MtxPacket mtxpacket = sendConfigMessage(new MtxPacket((byte) MtxPacket.XSENS_OUTPUTSKIPFACTOR, null));
		return mtxpacket.getUnsignedShortValue(0);
	}

	public void setOutputSkipFactor(int i) throws MtxCommException {
		byte data[] = { (byte) i, (byte) (i >> 8) };
		sendConfigMessage(new MtxPacket((byte) MtxPacket.XSENS_OUTPUTSKIPFACTOR, data));
		updateLocalFrequency();
	}

	/**
	 * Request�the�baudrate�of�the�device
	 */

	public int requestBaudRate() throws MtxCommException {
		MtxPacket mtxpacket = sendConfigMessage(new MtxPacket(MtxPacket.XSENS_REQBAUDRATE, null));
		int baudrate = mtxpacket.getUnsignedByteValue(0);
		switch (baudrate) {

			case 0:
				return 460800;

			case 1:
				return 230400;

			case 2:
				return 115200;

			case 3:
				return 75800;

			case 4:
				return 57600;

			case 5:
				return 38400;

			case 6:
				return 28800;

			case 7:
				return 19200;

			case 8:
				return 14400;

			case 9:
				return 9600;

			case 128:
				return 921600;
		}
		return -1;
	}

	/**
	 * This�message�changes�the�baudrate�of�the�communication�interface.
	 * The�new�baudrate�will�be�stored�in�non volatile�memory�and�will�become�
	 * active�after�issuing�the�Reset�message�or�power�cycle.
	 */

	public void setBaudRate(int baudrate) throws MtxCommException {
		byte data = 0;
		switch (baudrate) {
			case 460800:
				data = 0;
				break;

			case 230400:
				data = 1;
				break;

			case 115200:
				data = 2;
				break;

			case 75800:
				data = 3;
				break;

			case 57600:
				data = 4;
				break;

			case 38400:
				data = 5;
				break;

			case 28800:
				data = 6;
				break;

			case 19200:
				data = 7;
				break;

			case 14400:
				data = 8;
				break;

			case 9600:
				data = 9;
				break;

			case 921600:
				data = -128;
				break;

			default:
				data = (byte) baudrate;
				break;
		}
		sendConfigMessage(new MtxPacket(MtxPacket.XSENS_REQBAUDRATE, new byte[] { data }));
	}

	public static BitSet fromByteArray(byte[] bytes) {
		BitSet bits = new BitSet();
		for (int i = 0; i < bytes.length * 8; i++) {
			if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
				bits.set(i);
			}
		}
		for (int k = bits.size() - 1; k >= 0; k--) {
			System.out.print((bits.get(k)) ? "1" : "0");
		}
		System.out.println();
		return bits;
	}

	/**
	 * @param data Two bytes
	 * @throws MtxCommException
	 */
	public void setOutputMode(byte[] data) throws MtxCommException {
		fromByteArray(data);
		sendConfigMessage(new MtxPacket(MtxPacket.XSENS_OUTPUTMODE, data));
	}

	public int requestOutputMode() throws MtxCommException {
		MtxPacket mtxpacket = sendConfigMessage(new MtxPacket(MtxPacket.XSENS_OUTPUTMODE, null));
		return mtxpacket.getUnsignedShortValue(0);
	}

	public void setOutputSettings(byte[] data) throws MtxCommException {
		fromByteArray(data);
		sendConfigMessage(new MtxPacket(MtxPacket.XSENS_OUTPUTSETTINGS, data));
	}

	public int requestOutputSettings() throws MtxCommException {
		MtxPacket mtxpacket = sendConfigMessage(new MtxPacket(MtxPacket.XSENS_OUTPUTSETTINGS, null));
		return mtxpacket.getUnsignedShortValue(2);
	}

	public String initMt() throws MtxCommException {
		MtxPacket mtxpacket = sendConfigMessage(new MtxPacket(MtxPacket.XSENS_INITMT, null));
		return mtxpacket.getDeviceID(0);
	}

	public void restoreFactoryDefaults() throws MtxCommException {
		sendConfigMessage(new MtxPacket(MtxPacket.XSENS_RESTOREFACTORYDEFAULTS, null));
	}

	public void reset() throws MtxCommException {
		mLock.acquireLock();
		sendMessageAndWaitForReply(new MtxPacket(MtxPacket.XSENS_RESET, null), MtxPacket.XSENS_WAKEUP);
		sendMessageAndForget(new MtxPacket(MtxPacket.XSENS_WAKEUPACK, null));
		mInControlMode = true;
		mLock.releaseLock();
	}

	public void setAdaptToMagneticDisturbances(boolean flag) throws MtxCommException {
		byte data[] = new byte[2];
		if (flag)
			data[1] = 1;
		sendEitherStateMessage(new MtxPacket((byte) MtxPacket.XSENS_AMD, data));
	}

	public boolean getAdaptToMagneticDisturbances() throws MtxCommException {
		MtxPacket mtxpacket = sendEitherStateMessage(new MtxPacket((byte) MtxPacket.XSENS_AMD, null));
		return mtxpacket.getUnsignedByteValue(1) == 1;
	}

	public void resetOrientation(int i) throws MtxCommException {
		byte data[] = { 0, (byte) i };
		if (i == 0)
			sendConfigMessage(new MtxPacket(MtxPacket.XSENS_RESET_ORIENTATION, data));
		else
			sendMeasurementMessage(new MtxPacket(MtxPacket.XSENS_RESET_ORIENTATION, data));
	}

	private synchronized MtxPacket sendConfigMessage(MtxPacket mtxpacket) throws MtxCommException {
		MtxPacket mtxpacket2;
		mLock.acquireLock();
		if (!mInControlMode) {
			mLock.releaseLock();
			throw new MtxCommException(MtxCommException.WRONG_MODE);
		}
		MtxPacket mtxpacket1 = sendMessageAndWaitForReply(mtxpacket, (byte) (mtxpacket.getMid() + 1));
		if (mtxpacket1.getMid() == MtxPacket.XSENS_ERROR)
			throw new MtxCommException(mtxpacket1.getUnsignedByteValue(0));
		mtxpacket2 = mtxpacket1;
		mLock.releaseLock();
		return mtxpacket2;
	}

	private synchronized MtxPacket sendMeasurementMessage(MtxPacket mtxpacket) throws MtxCommException {
		MtxPacket mtxpacket2;
		mLock.acquireLock();
		if (mInControlMode) {
			mLock.releaseLock();
			throw new MtxCommException(MtxCommException.WRONG_MODE);
		}

		MtxPacket mtxpacket1 = sendMessageAndWaitForReply(mtxpacket, (byte) (mtxpacket.getMid() + 1));
		if (mtxpacket1.getMid() == MtxPacket.XSENS_ERROR)
			throw new MtxCommException(mtxpacket1.getUnsignedByteValue(0));

		mtxpacket2 = mtxpacket1;
		mLock.releaseLock();
		return mtxpacket2;
	}

	private synchronized MtxPacket sendEitherStateMessage(MtxPacket mtxpacket) throws MtxCommException {
		MtxPacket mtxpacket2;
		mLock.acquireLock();
		MtxPacket mtxpacket1 = sendMessageAndWaitForReply(mtxpacket, (byte) (mtxpacket.getMid() + 1));
		if (mtxpacket1.getMid() == MtxPacket.XSENS_ERROR)
			throw new MtxCommException(mtxpacket1.getUnsignedByteValue(0));
		mtxpacket2 = mtxpacket1;
		mLock.releaseLock();
		return mtxpacket2;
	}

	private synchronized void sendMessageAndForget(MtxPacket mtxpacket) throws MtxCommException {
		try {
			mLowLevel.send(mtxpacket.toByteArray());
		} 
		catch (IOException ioexception) {
			throw new MtxCommException(ioexception, 1);
		}
	}

	private synchronized MtxPacket sendMessageAndWaitForReply(MtxPacket mtxpacket, byte byte0) throws MtxCommException {
		mExpectedPacketId = byte0;
		try {
			mLowLevel.send(mtxpacket.toByteArray());
		} 
		catch (IOException ioexception) {
			throw new MtxCommException(ioexception, 1);
		}
		try {
			wait(mWaitForReplyTimeout);
		} 
		catch (InterruptedException interruptedexception) {
		}
		if (mExpectedPacketId == -1) {
			if (mPacket.getMid() == MtxPacket.XSENS_ERROR)
				throw new MtxCommException(mPacket.getUnsignedByteValue(0));
			else
				return mPacket;
		} 
		else 
			throw new MtxCommException(0);
	}

	private void updateLocalFrequency() throws MtxCommException {
		int i = requestOutputSkipFactor();
		int j = requestPeriod();
		mPeriod = (double) j / 115.2D;
		mPeriod *= i + 1;
		mPeriod /= 1000D;
	}

	public void newEvent(Object obj, Object obj1) {
		newEvent(obj, (MtxPacket) obj1);
	}
}