package org.jxsens.viewer.demos.euler;

import org.jxsens.MtxListener;
import org.jxsens.decoders.MtxNavigationEvent;
import org.jxsens.viewer.jogl.LinesPointsStrip;

public class NavigationEvent extends LinesPointsStrip implements MtxListener {

	protected double data[];

	public NavigationEvent() {}

	public boolean isEmpty(){
		return data == null;
	}
	
	/*  Applied first */
	public double getHeading() {
		return data[0];
	}

	/*  Applied second */
	public double getPitch() {
		return data[1];
	}

	/*  Applied last */
	public double getBank() {
		return data[2];
	}

	@Override
	public void newEvent(Object a, Object b) {
		this.data = ((MtxNavigationEvent) b).getEulerAngles();
		// System.out.println("Heading: " + data[0] + " Attitude: " + data[1] +
		// " Bank: " + data[2]);
	}

}
