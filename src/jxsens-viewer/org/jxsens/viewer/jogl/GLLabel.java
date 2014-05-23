package org.jxsens.viewer.jogl;

import com.sun.opengl.util.GLUT;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

public class GLLabel implements IGLDrawable {

	private String mText;
	private float mColour[] = { 1.0F, 0.0F, 0.0F, 1.0F };
	private float mPosition[] = { 0.0F, 0.0F, 0.0F };
	private GLUT mGlut;
	
	public GLLabel(String s, float af[], float af1[]) {
		mGlut = new GLUT();
		mText = s;
		mColour = af1;
		mPosition = af;
	}

	public synchronized void display(GLAutoDrawable glautodrawable) {
		GL gl = glautodrawable.getGL();
		gl.glColor4f(mColour[0], mColour[1], mColour[2], mColour[3]);
		gl.glRasterPos3d(mPosition[0], mPosition[1], mPosition[2]);
		mGlut.glutBitmapString(7, mText);
	}

	public synchronized void setPosition(float af[]) {
		mPosition = af;
	}

	public synchronized void setColour(float af[]) {
		mColour = af;
	}

	public synchronized void setText(String s) {
		mText = s;
	}
}