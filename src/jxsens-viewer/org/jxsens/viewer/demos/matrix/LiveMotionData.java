package org.jxsens.viewer.demos.matrix;

import javax.media.opengl.GLAutoDrawable;

import org.jxsens.MtxListener;
import org.jxsens.events.INSEvent;
import org.jxsens.viewer.jogl.LinesPointsStrip;

import Jama.Matrix;

public class LiveMotionData extends LinesPointsStrip implements MtxListener {

	private int mWindowSize;
	@SuppressWarnings("unused")
	private float mGravity;
	@SuppressWarnings("unused")
	private float mPrevious[][];
	@SuppressWarnings("unused")
	private int mIndex;
	@SuppressWarnings("unused")
	private float mAccumulated[];
	@SuppressWarnings("unused")
	private Matrix m;
	@SuppressWarnings("unused")
	private int samples = 0;

	public LiveMotionData(float f) {
		mWindowSize = 10;
		mPrevious = new float[mWindowSize][];
		mAccumulated = new float[3];
		setPointSize(3F);
		mGravity = f;
	}

	@Override
	public void display(GLAutoDrawable glautodrawable) {
		synchronized (this) {
			super.display(glautodrawable);
		}
	}

	public void newEvent(Object obj, INSEvent insevent) {
//		synchronized (this) {
//			if (mPrevious[mIndex] != null) {
//				mAccumulated[0] -= mPrevious[mIndex][0];
//				mAccumulated[1] -= mPrevious[mIndex][1];
//				mAccumulated[2] -= mPrevious[mIndex][2];
//			}
//			mPrevious[mIndex] = (new float[] { (float) insevent.getSensorAccel()[0], (float) insevent.getSensorAccel()[1], (float) insevent.getSensorAccel()[2] });
//			mAccumulated[0] += mPrevious[mIndex][0];
//			mAccumulated[1] += mPrevious[mIndex][1];
//			mAccumulated[2] += mPrevious[mIndex][2];
//			mIndex++;
//			mIndex %= mWindowSize;
//			clearPoints();
//			addPoint(new float[] { 0.0F, 0.0F, 0.0F });
//
//			m = ((AttitudeEvent) insevent).getGlobalToSensorRotationMatrix();
//
//			samples++;
//			try {
//				x = (mAccumulated[0] / (double) mWindowSize);
//				y = (mAccumulated[1] / (double) mWindowSize);
//				z = (mAccumulated[2] / (double) mWindowSize);
//
//				double a = Math.asin(x / Math.sqrt(x * x + y * y + z * z));
//				double b = Math.asin(y / Math.sqrt(x * x + y * y + z * z));
//				double c = Math.asin(z / Math.sqrt(x * x + y * y + z * z));
//
//				// System.out.println("\nroll: " + a);
//				// System.out.println("pitch: " + b);
//				// System.out.println("samples: " + samples);
//
//				// System.out.printf("\n" + x +" "+ y +" "+ z);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			addPoint(new float[] { mAccumulated[0] / mWindowSize, mAccumulated[1] / mWindowSize, mAccumulated[2] / mWindowSize });
//			float f = ((float) Math.abs(MyMath.length(mAccumulated) - (mGravity * mWindowSize)) / mWindowSize) * 2.5F;
//			setLineColour(Math.max(0.0F, f), Math.max(0.0F, 1.0F - f), 0.0F, 1.0F);
//			setPointColour(Math.max(0.0F, f), Math.max(0.0F, 1.0F - f), 0.0F, 1.0F);
//		}
	}

	double x = 0, y = 0, z = 0;

	public void setMatrix(Matrix m) {
		this.m = m;
	}

//	public Quaternion getQuaternion() {
//
//		if (m == null || m.trace() + 1 <= 0) {
//			return null;
//		}
//		double x, y, z, w;
//
//		// double w = Math.sqrt( Math.max( 0, 1 + m.get(0, 0) + m.get(1, 1) +
//		// m.get(2,
//		// 2) ) ) / 2;
//		// double x = Math.sqrt( Math.max( 0, 1 + m.get(0, 0) - m.get(1, 1) -
//		// m.get(2,
//		// 2) ) ) / 2;
//		// double y = Math.sqrt( Math.max( 0, 1 - m.get(0, 0) + m.get(1, 1) -
//		// m.get(2,
//		// 2) ) ) / 2;
//		// double z = Math.sqrt( Math.max( 0, 1 - m.get(0, 0) - m.get(1, 1) +
//		// m.get(2,
//		// 2) ) ) / 2;
//		//		 
//		//		
//		// double w = Math.sqrt(1.0 + m.get(0, 0) + m.get(1, 1) + m.get(2, 2)) /
//		// 2.0;
//		// double w4 = (4.0 * w);
//		// double x = (m.get(2, 1) - m.get(1, 2)) / w4;
//		// double y = (m.get(0, 2) - m.get(2, 0)) / w4;
//		// double z = (m.get(1, 0) - m.get(0, 1)) / w4;
//		double T = m.trace() + 1;
//		if (false) {
//
//			double S = 0.5 / Math.sqrt(T);
//
//			w = 0.25 / S;
//
//			x = (m.get(2, 1) - m.get(1, 2)) * S;
//
//			y = (m.get(0, 2) - m.get(2, 0)) * S;
//
//			z = (m.get(1, 0) - m.get(0, 1)) * S;
//		} else {
//			if ((m.get(0, 0) > m.get(1, 1)) & (m.get(0, 0) > m.get(2, 2))) {
//				double S = Math.sqrt(1.0 + m.get(0, 0) - m.get(1, 1) - m.get(2, 2)) * 2;
//				x = 0.25 * S;
//				y = (m.get(0, 1) + m.get(1, 0)) / S;
//				z = (m.get(0, 2) + m.get(2, 0)) / S;
//				w = (m.get(2, 1) - m.get(1, 2)) / S; // correction
//
//			} else if (m.get(1, 1) > m.get(2, 2)) {
//				double S = Math.sqrt(1.0 + m.get(1, 1) - m.get(0, 0) - m.get(2, 2)) * 2;
//				x = (m.get(0, 1) + m.get(1, 0)) / S;
//				y = 0.25 * S;
//				z = (m.get(1, 2) + m.get(2, 1)) / S;
//				w = (m.get(0, 2) - m.get(2, 0)) / S;
//
//			} else {
//				double S = Math.sqrt(1.0 + m.get(2, 2) - m.get(0, 0) - m.get(1, 1)) * 2;
//				x = (m.get(0, 2) + m.get(2, 0)) / S;
//				y = (m.get(1, 2) + m.get(2, 1)) / S;
//				z = 0.25 * S;
//				w = (m.get(1, 0) - m.get(0, 1)) / S; // correction
//			}
//		}
//		return new Quaternion(x, y, z, w);
//	}

	public void newEvent(Object obj, Object obj1) {
		newEvent(obj, (INSEvent) obj1);
	}
}