package org.jxsens.decoders;

import org.jxsens.events.INSEvent;

public class MtxNavigationEvent extends INSEvent {

	protected double data[];

	public MtxNavigationEvent(double data[]) {
		this.data = data;
//		System.out.println("Roll: " + data[0] + " Pitch: " + data[1] + " Yaw: " + data[2]);
//		System.out.println("Heading: " + data[0] + " Attitude: " + data[1] + " Bank: " + data[2]);
	}

	/** Applied first */
	public double getHeading(){
		return data[0];
	}

	/** Applied second */
	public double geAttitude(){
		return data[1];
	}
	
	/** Applied last */
	public double getBank(){
		return data[2];
	}

	public double[] getEulerAngles() {
		return data;
	}

}
