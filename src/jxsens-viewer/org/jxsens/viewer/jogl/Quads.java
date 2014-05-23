package org.jxsens.viewer.jogl;

import java.util.ArrayList;
import java.util.Iterator;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

public class Quads implements IGLDrawable {

	private ArrayList<float[]> mQuads;
	private float mFillColour[] = { 1.0F, 0.0F, 0.0F, 1.0F };

	public Quads() {
		mQuads = new ArrayList<float[]>();
	}

	public synchronized void display(GLAutoDrawable glautodrawable) {
		GL gl = glautodrawable.getGL();
		gl.glColor4f(mFillColour[0], mFillColour[1], mFillColour[2], mFillColour[3]);
		Iterator<float[]> iterator1 = iterator();
		gl.glBegin(7);
		float af[];
		for (; iterator1.hasNext(); gl.glVertex3f(af[9], af[10], af[11])) {
			af = iterator1.next();
			gl.glVertex3f(af[0], af[1], af[2]);
			gl.glVertex3f(af[3], af[4], af[5]);
			gl.glVertex3f(af[6], af[7], af[8]);
		}

		gl.glEnd();
	}

	public synchronized void addQuad(float af[], float af1[], float af2[], float af3[]) {
		mQuads.add(new float[] { 
					af[0], af[1], af[2], af1[0], af1[1], af1[2], 
					af2[0], af2[1], af2[2], af3[0], af3[1], af3[2] 
				});
	}

	public synchronized void removeQuadAt(int i) {
		mQuads.remove(i);
	}

	public Iterator<float[]> iterator() {
		return mQuads.iterator();
	}

	public synchronized void setFillColour(float f, float f1, float f2, float f3) {
		mFillColour = (new float[] { f, f1, f2, f3 });
	}

	public float[] getFillColour() {
		return mFillColour;
	}

	public void clear() {
		mQuads.clear();
	}

}