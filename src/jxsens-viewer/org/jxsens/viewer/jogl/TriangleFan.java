package org.jxsens.viewer.jogl;

import java.util.ArrayList;
import java.util.Iterator;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

public class TriangleFan implements IGLDrawable {

	private ArrayList<float[]> mLines;
	private float mColour[] = { 1.0F, 0.0F, 0.0F, 1.0F };

	public TriangleFan() {
		mLines = new ArrayList<float[]>();
	}

	public synchronized void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glColor4f(mColour[0], mColour[1], mColour[2], mColour[3]);
		Iterator<float[]> iterator1 = iterator();
		gl.glBegin(6);
		float af[];
		for (; iterator1.hasNext(); gl.glVertex3f(af[0], af[1], af[2]))
			af = iterator1.next();

		gl.glEnd();
	}

	public synchronized void addPoint(float af[]) {
		mLines.add(af);
	}

	public synchronized void removePointAt(int i) {
		mLines.remove(i);
	}

	public Iterator<float[]> iterator() {
		return mLines.iterator();
	}

	public synchronized void setColour(float f, float f1, float f2, float f3) {
		mColour = (new float[] { f, f1, f2, f3 });
	}

	public float[] getColour() {
		return mColour;
	}

}
