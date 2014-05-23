package org.jxsens.decoders;

import org.jxsens.events.TimedEvent;
import org.jxsens.logging.ILoggableEvent;
import org.jxsens.logging.LogItem;
import org.jxsens.logging.LogItemBuilder;

public class MtxRawEvent extends TimedEvent implements ILoggableEvent {

	private int mAccel[];
	private int mGyro[];
	private int mMag[];
	private int mTemp;

	public MtxRawEvent() {}

	public MtxRawEvent(double d, int ai[], int ai1[], int ai2[], int i) {
		super(d);
		mAccel = ai;
		mGyro = ai1;
		mMag = ai2;
		mTemp = i;
	}

	public int[] getAccel() {
		return mAccel;
	}

	public int[] getGyro() {
		return mGyro;
	}

	public int[] getMag() {
		return mMag;
	}

	public int getTemp() {
		return mTemp;
	}

	public LogItemBuilder buildLogItem() {
		LogItemBuilder logitembuilder = new LogItemBuilder();
		logitembuilder.append(getTime());
		logitembuilder.append(mAccel);
		logitembuilder.append(mGyro);
		logitembuilder.append(mMag);
		logitembuilder.append(mTemp);
		return logitembuilder;
	}

	public void fromLogItem(LogItem logitem) {
		setTime(logitem.readDouble());
		mAccel = logitem.readIntAry(3);
		mGyro = logitem.readIntAry(3);
		mMag = logitem.readIntAry(3);
		mTemp = logitem.readInt();
	}

	@Override
	public void buildLogItem(LogItemBuilder logitembuilder) {}

}
