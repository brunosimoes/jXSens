package org.jxsens.logging;

public interface ILoggableEvent {

	public abstract void buildLogItem(LogItemBuilder logitembuilder);
	public abstract void fromLogItem(LogItem logitem);
	
}