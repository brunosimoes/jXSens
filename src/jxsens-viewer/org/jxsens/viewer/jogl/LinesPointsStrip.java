package org.jxsens.viewer.jogl;

import java.util.ArrayList;
import java.util.Iterator;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

public class LinesPointsStrip implements IGLDrawable {

	private ArrayList<float[]> mPoints;
	private boolean mDrawLines;
	private short mLineStipple;
	private float mLineThickness;
	private float mLineColour[] = { 1.0F, 0.0F, 0.0F, 1.0F };
	private boolean mDrawPoints;
	private float mPointSize;
	private float mPointColour[] = { 1.0F, 0.0F, 0.0F, 1.0F };

	public LinesPointsStrip() {
		mPoints = new ArrayList<float[]>();
		mDrawLines = true;
		mLineStipple = (short) Integer.parseInt("ffff", 16);
		mLineThickness = 1.0F;
		mDrawPoints = true;
		mPointSize = 5F;
	}

	public synchronized void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		if (getDrawLines()) {
			gl.glLineWidth(getLineThickness());
			gl.glColor4f(getLineColour()[0], getLineColour()[1], getLineColour()[2], getLineColour()[3]);
			gl.glLineStipple(1, getLineStipple());
			Iterator<float[]> iterator1 = iterator();
			gl.glBegin(3);
			float af[];
			for (; iterator1.hasNext(); gl.glVertex3f(af[0], af[1], af[2]))
				af = iterator1.next();

			gl.glEnd();
		}
		if (getDrawPoints()) {
			gl.glPointSize(getPointSize());
			gl.glColor4f(getPointColour()[0], getPointColour()[1], getPointColour()[2], getPointColour()[3]);
			Iterator<float[]> iterator2 = iterator();
			gl.glBegin(0);
			for (int i = 0; iterator2.hasNext(); i++) {
				float af1[] = iterator2.next();
				gl.glVertex3f(af1[0], af1[1], af1[2]);
			}

			gl.glEnd();
		}
	}

	public synchronized void addPoint(float af[]) {
		mPoints.add(af);
	}

	public synchronized void removePointAt(int i) {
		mPoints.remove(i);
	}

	public synchronized void clearPoints() {
		mPoints.clear();
	}

	public Iterator<float[]> iterator() {
		return mPoints.iterator();
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

	public synchronized void setLineStipple(short word0) {
		mLineStipple = word0;
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