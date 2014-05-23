package org.jxsens.viewer.jogl;

import java.awt.event.*;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

public class SimpleCameraController implements CameraController, MouseWheelListener, KeyListener, MouseMotionListener {

	public static enum Projection {
		Perspective, Orthographic
	};

	private Projection mProjection;
	private float mDistance;
	private float mRotateAboutHorizontal;
	private float mRotateAboutVertical;
	private float mLookAt[] = { 0.0F, 0.0F, 0.0F };
	private GLU mGlu;
	private int mPrevX;
	private int mPrevY;

	public SimpleCameraController() {
		mProjection = Projection.Orthographic;
		mDistance = 50F;
		mRotateAboutHorizontal = 30F;
		mRotateAboutVertical = 30F;
		mGlu = new GLU();
	}

	public synchronized void doCameraTransformation(GLAutoDrawable drawable, int i, int j) {
		if (mProjection == Projection.Perspective)
			setPerspective(drawable, j, i);
		else
			setOrtho(drawable, j, i);
		GL gl = drawable.getGL();
		gl.glLoadIdentity();
		gl.glTranslatef(0.0F, 0.0F, -mDistance);
		gl.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
		gl.glRotatef(mRotateAboutHorizontal, 1.0F, 0.0F, 0.0F);
		gl.glRotatef(mRotateAboutVertical, 0.0F, 0.0F, 1.0F);
		gl.glTranslatef(-mLookAt[0], -mLookAt[1], -mLookAt[2]);
	}

	public void setProjection(Projection projection) {
		mProjection = projection;
	}

	private void setPerspective(GLAutoDrawable drawable, int i, int j) {
		drawable.getGL().glMatrixMode(5889);
		drawable.getGL().glLoadIdentity();
		mGlu.gluPerspective(30D, (double) j / (double) i, 0.10000000000000001D, 3.4028234663852886E+038D);
		drawable.getGL().glMatrixMode(5888);
	}

	private void setOrtho(GLAutoDrawable glautodrawable, int i, int j) {
		double d = (double) j / (double) i;
		glautodrawable.getGL().glMatrixMode(5889);
		glautodrawable.getGL().glLoadIdentity();
		glautodrawable.getGL().glOrtho((double) (-mDistance) * d, (double) mDistance * d, -mDistance, mDistance, -10000D, 10000D);
		glautodrawable.getGL().glMatrixMode(5888);
	}

	public synchronized void lookAt(float af[]) {
		mLookAt = af;
	}

	public synchronized void setCameraDistance(float f) {
		mDistance = f;
	}

	public synchronized void setElevation(float f) {
		mRotateAboutHorizontal = f;
	}

	public synchronized void setAzimuth(float f) {
		mRotateAboutVertical = f;
	}

	public synchronized void mouseWheelMoved(MouseWheelEvent mousewheelevent) {
		if (mousewheelevent.getWheelRotation() > 0)
			mDistance *= 1.1000000000000001D;
		else
			mDistance *= 0.90909F;
	}

	public synchronized void keyPressed(KeyEvent keyevent) {
		if (keyevent.getKeyCode() == 521)
			mDistance *= 1.1000000000000001D;
		else if (keyevent.getKeyCode() == 45)
			mDistance *= 0.90909F;
		else if (keyevent.getKeyCode() == 37)
			mRotateAboutVertical += 90F;
		else if (keyevent.getKeyCode() == 39)
			mRotateAboutVertical -= 90F;
		else if (keyevent.getKeyCode() == 38)
			mRotateAboutHorizontal -= 90F;
		else if (keyevent.getKeyCode() == 40)
			mRotateAboutHorizontal += 90F;
		else if (keyevent.getKeyCode() == 87) {
			mLookAt[0] += Math.sin((mRotateAboutVertical / 180F) * 3.141593F);
			mLookAt[1] += Math.cos((mRotateAboutVertical / 180F) * 3.141593F);
		} else if (keyevent.getKeyCode() == 83) {
			mLookAt[0] -= Math.sin((mRotateAboutVertical / 180F) * 3.141593F);
			mLookAt[1] -= Math.cos((mRotateAboutVertical / 180F) * 3.141593F);
		} else if (keyevent.getKeyCode() == 68) {
			mLookAt[1] -= Math.sin((mRotateAboutVertical / 180F) * 3.141593F);
			mLookAt[0] += Math.cos((mRotateAboutVertical / 180F) * 3.141593F);
		} else if (keyevent.getKeyCode() == 65) {
			mLookAt[1] += Math.sin((mRotateAboutVertical / 180F) * 3.141593F);
			mLookAt[0] -= Math.cos((mRotateAboutVertical / 180F) * 3.141593F);
		}
	}

	public void keyReleased(KeyEvent keyevent) {
	}

	public void keyTyped(KeyEvent keyevent) {
	}

	public void mousePressed(MouseEvent mouseevent) {
		mPrevX = mouseevent.getX();
		mPrevY = mouseevent.getY();
	}

	public void mouseDragged(MouseEvent mouseevent) {
		int i = mouseevent.getX() - mPrevX;
		int j = mouseevent.getY() - mPrevY;
		mRotateAboutVertical = mRotateAboutVertical + (float) i;
		mRotateAboutHorizontal = mRotateAboutHorizontal + (float) j;
		mPrevX = mouseevent.getX();
		mPrevY = mouseevent.getY();
	}

	public void mouseMoved(MouseEvent mouseevent) {
		mPrevX = mouseevent.getX();
		mPrevY = mouseevent.getY();
	}

}