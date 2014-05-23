package org.jxsens.logging;

public class LogItemBuilder {

	private StringBuilder lData;

	public LogItemBuilder() {
		lData = new StringBuilder();
	}

	public void append(String s) {
		lData.append(s + "\t");
	}

	public void append(float f) {
		lData.append(f + "\t");
	}

	public void append(float af[]) {
		for (int i = 0; i < af.length; i++)
			append(af[i]);

	}

	public void append(int i) {
		lData.append(i + "\t");
	}

	public void append(int ai[]) {
		for (int i = 0; i < ai.length; i++)
			append(ai[i]);

	}

	public void append(double d) {
		lData.append(d + "\t");
	}

	public void append(double ad[]) {
		for (int i = 0; i < ad.length; i++)
			append(ad[i]);

	}

	public void append(byte b) {
		lData.append(b + "\t");
	}

	public void append(byte b[]) {
		for (int i = 0; i < b.length; i++)
			append(b[i]);

	}

	public void append(short s) {
		lData.append(s + "\t");
	}

	public void append(short s[]) {
		for (int i = 0; i < s.length; i++)
			append(s[i]);
	}

	public void append(long l) {
		lData.append(l + "\t");
	}

	public void append(long al[]) {
		for (int i = 0; i < al.length; i++)
			append(al[i]);
	}

	public LogItem toTSVLine() {
		return new LogItem(lData.toString());
	}

	public String toString() {
		return lData.toString();
	}
}
