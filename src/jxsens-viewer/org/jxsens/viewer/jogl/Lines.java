package org.jxsens.viewer.jogl;

import java.util.ArrayList;
import java.util.Iterator;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

public class Lines implements IGLDrawable {

	private ArrayList<float[]> mLines;
	private boolean mDrawLines;
	private short mLineStipple;
	private float mLineThickness;
	private float mLineColour[] = { 1.0F, 0.0F, 0.0F, 1.0F };
	private boolean mDrawPoints;
	private float mPointSize;
	private float mPointColour[] = { 1.0F, 0.0F, 0.0F, 1.0F };

	public Lines() {
		mLines = new ArrayList<float[]>();
		mDrawLines = true;
		mLineStipple = (short) Integer.parseInt("ffff", 16);
		mLineThickness = 1.0F;
		mDrawPoints = false;
		mPointSize = 5F;
	}

	public synchronized void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		if (getDrawLines()) {
			gl.glLineWidth(getLineThickness());
			gl.glColor4f(mLineColour[0], mLineColour[1], mLineColour[2], mLineColour[3]);
			gl.glLineStipple(1, getLineStipple());
			Iterator<float[]> iterator1 = iterator();
			gl.glBegin(1);
			float af[];
			for (; iterator1.hasNext(); gl.glVertex3f(af[3], af[4], af[5])) {
				af = iterator1.next();
				gl.glVertex3f(af[0], af[1], af[2]);
			}

			gl.glEnd();
		}
		if (getDrawPoints()) {
			gl.glPointSize(getPointSize());
			gl.glColor4f(mPointColour[0], mPointColour[1], mPointColour[2], mPointColour[3]);
			Iterator<float[]> iterator2 = iterator();
			gl.glBegin(0);
			float af1[];
			for (; iterator2.hasNext(); gl.glVertex3f(af1[3], af1[4], af1[5])) {
				af1 = iterator2.next();
				gl.glVertex3f(af1[0], af1[1], af1[2]);
			}
			gl.glEnd();
		}
	}

	public synchronized void addLine(float af[], float af1[]) {
		mLines.add(new float[] { af[0], af[1], af[2], af1[0], af1[1], af1[2] });
	}

	public synchronized void addLine(float af[]) {
		mLines.add(af);
	}

	public synchronized void removeLineAt(int i) {
		mLines.remove(i);
	}

	public Iterator<float[]> iterator() {
		return mLines.iterator();
	}

	public synchronized void setDrawLines(boolean flag) {
		mDrawLines = flag;
	}

	public boolean getDrawLines() {
		return mDrawLines;
	}

	public synchronized void setDrawPoints(boolean flag) {
		mDrawPoints = flag;
	}

	public boolean getDrawPoints() {
		return mDrawPoints;
	}

	public synchronized void setLineColour(float f, float f1, float f2, float f3) {
		mLineColour = (new float[] { f, f1, f2, f3 });
	}

	public float[] getLineColour() {
		return mLineColour;
	}

	public synchronized void setPointColour(float f, float f1, float f2, float f3) {
		mPointColour = (new float[] { f, f1, f2, f3 });
	}

	public float[] getPointColour() {
		return mPointColour;
	}

	public synchronized void setLineStipple(short stipple) {
		mLineStipple = stipple;
	}

	public short getLineStipple() {
		return mLineStipple;
	}

	public synchronized void setPointSize(float f) {
		mPointSize = f;
	}

	public float getPointSize() {
		return mPointSize;
	}

	public synchronized void setLineThickness(float f) {
		mLineThickness = f;
	}

	public float getLineThickness() {
		return mLineThickness;
	}

}