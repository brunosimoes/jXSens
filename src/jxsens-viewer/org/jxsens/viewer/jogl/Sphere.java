package org.jxsens.viewer.jogl;

import com.sun.opengl.util.GLUT;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

public class Sphere implements IGLDrawable {

	private float mTrans[] = { 0.0F, 0.0F, 0.0F };
	private float mRadius[];
	private float mColour[] = { 0.6F, 0.0F, 0.0F, 0.1F };

	public Sphere(float af[], float f, float af1[]) {
		mTrans = af;
		mRadius = (new float[] { f, f, f });
		mColour = af1;
	}

	public Sphere(float af[], float af1[], float af2[]) {
		mTrans = af;
		mRadius = (new float[] { af1[0], af1[1], af1[2] });
		mColour = af2;
	}

	public void display(GLAutoDrawable glautodrawable) {
		GL gl = glautodrawable.getGL();
		gl.glPushMatrix();
		gl.glTranslatef(mTrans[0], mTrans[1], mTrans[2]);
		gl.glScalef(mRadius[0], mRadius[1], mRadius[2]);
		gl.glColor4f(mColour[0], mColour[1], mColour[2], mColour[3]);
		GLUT glut = new GLUT();
		glut.glutWireSphere(1.0D, 100, 100);
		gl.glPopMatrix();
	}

	public void setColour(float af[]) {
		mColour = af;
	}

}