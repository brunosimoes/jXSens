package org.jxsens.events;

public abstract class TimedEvent {

	private double time;

	public TimedEvent() {}

	public TimedEvent(double value) {
		this.time = value;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double value) {
		this.time = value;
	}
}
