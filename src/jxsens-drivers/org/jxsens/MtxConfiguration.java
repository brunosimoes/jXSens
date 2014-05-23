package org.jxsens;

public class MtxConfiguration {

	private String mMasterDeviceId;
	private int mSamplingPeriod;
	private int mOutputSkipFactor;
	private int mNumberOfDevices;
	private String mDeviceId;
	private int mDataLength;
	private int mOutputMode;
	private int mOutputSettings;

	public MtxConfiguration() {}

	public void setMasterDeviceID(String s) {
		mMasterDeviceId = s;
	}

	public String getMasterDeviceID() {
		return mMasterDeviceId;
	}

	public void setDeviceID(String s) {
		mDeviceId = s;
	}

	public String getDeviceID() {
		return mDeviceId;
	}

	public void setSamplingPeriod(int i) {
		mSamplingPeriod = i;
	}

	public int getSamplingPeriod() {
		return mSamplingPeriod;
	}

	public void setOutputSkipFactor(int i) {
		mOutputSkipFactor = i;
	}

	public int getOutputSkipFactor() {
		return mOutputSkipFactor;
	}

	public void setNumberOfDevices(int i) {
		mNumberOfDevices = i;
	}

	public int getNumberOfDevices() {
		return mNumberOfDevices;
	}

	public void setDataLength(int i) {
		mDataLength = i;
	}

	public int getDataLength() {
		return mDataLength;
	}

	public void setOutputMode(int i) {
		mOutputMode = i;
	}

	public int getOutputMode() {
		return mOutputMode;
	}

	public void setOutputSettings(int i) {
		mOutputSettings = i;
	}

	public int getOutputSettings() {
		return mOutputSettings;
	}

}