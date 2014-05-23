package org.jxsens.logging;

public class LogItem {

	private String mData[];
	private int mIndex;

	public LogItem(String s) {
		mData = s.split("\t");
	}

	public LogItem(String as[]) {
		mData = as;
	}

	public String readStr() {
		return mData[mIndex++];
	}

	public String[] readStrAry(int i) {
		String as[] = new String[i];
		for (int j = 0; j < i; j++)
			as[j] = readStr();

		return as;
	}

	public float readFloat() {
		return Float.parseFloat(mData[mIndex++]);
	}

	public float[] readFloatAry(int i) {
		float af[] = new float[i];
		for (int j = 0; j < i; j++)
			af[j] = readFloat();

		return af;
	}

	public short readShort() {
		return Short.parseShort(mData[mIndex++]);
	}

	public short[] readShortAry(int i) {
		short aword0[] = new short[i];
		for (int j = 0; j < i; j++)
			aword0[j] = readShort();

		return aword0;
	}

	public int readInt() {
		return Integer.parseInt(mData[mIndex++]);
	}

	public int[] readIntAry(int i) {
		int ai[] = new int[i];
		for (int j = 0; j < i; j++)
			ai[j] = readInt();

		return ai;
	}

	public double readDouble() {
		return Double.parseDouble(mData[mIndex++]);
	}

	public double[] readDoubleAry(int i) {
		double ad[] = new double[i];
		for (int j = 0; j < i; j++)
			ad[j] = readDouble();

		return ad;
	}

	public long readLong() {
		return Long.parseLong(mData[mIndex++]);
	}

	public long[] readLongAry(int i) {
		long al[] = new long[i];
		for (int j = 0; j < i; j++)
			al[j] = readLong();

		return al;
	}

}