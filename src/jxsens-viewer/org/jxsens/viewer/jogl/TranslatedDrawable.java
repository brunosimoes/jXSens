package org.jxsens.viewer.jogl;

import Jama.Matrix;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

public class TranslatedDrawable implements IGLDrawable {

	private IGLDrawable mChild;
	private Matrix mTransMatrix;
	private float mYaw;
	private float mPitch;
	private float mRoll;

	public TranslatedDrawable(IGLDrawable drawable) {
		mChild = drawable;
		setTrans(Matrix.identity(4, 4));
	}

	public TranslatedDrawable(IGLDrawable drawable, Matrix matrix) {
		mChild = drawable;
		setTrans(matrix);
	}

	public TranslatedDrawable(IGLDrawable drawable, float af[]) {
		mChild = drawable;
		setTrans(Matrix.identity(4, 4));
		mTransMatrix.set(0, 3, af[0]);
		mTransMatrix.set(1, 3, af[1]);
		mTransMatrix.set(2, 3, af[2]);
	}

	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glPushMatrix();
		gl.glMultMatrixd(mTransMatrix.getColumnPackedCopy(), 0);
		mChild.display(drawable);
		gl.glPopMatrix();
	}

	public void setTrans(Matrix matrix) {
		mTransMatrix = matrix;
		mYaw = (float) Math.atan(mTransMatrix.get(1, 0) / mTransMatrix.get(0, 0));
		mPitch = (float) Math.atan(-mTransMatrix.get(2, 0) / Math.sqrt(mTransMatrix.get(2, 1) * mTransMatrix.get(2, 1) + mTransMatrix.get(2, 2) * mTransMatrix.get(2, 2)));
		mRoll = (float) Math.atan(mTransMatrix.get(2, 1) / mTransMatrix.get(2, 2));
	}

	public float getYaw() {
		return mYaw;
	}

	public void setYaw(float f) {
		mYaw = f;
		updateAttitudeMatrix();
	}

	public float getPitch() {
		return mPitch;
	}

	public void setPitch(float f) {
		mPitch = f;
		updateAttitudeMatrix();
	}

	public float getRoll() {
		return mRoll;
	}

	public void setRoll(float f) {
		mRoll = f;
		updateAttitudeMatrix();
	}

	private void updateAttitudeMatrix() {
		mTransMatrix.set(0, 0, Math.cos(mYaw) * Math.cos(mPitch));
		mTransMatrix.set(1, 0, Math.sin(mYaw) * Math.cos(mPitch));
		mTransMatrix.set(2, 0, -Math.sin(mPitch));
		mTransMatrix.set(0, 1, Math.cos(mYaw) * Math.sin(mPitch) * Math.sin(mRoll) - Math.sin(mYaw) * Math.cos(mRoll));
		mTransMatrix.set(1, 1, Math.cos(mYaw) * Math.sin(mPitch) * Math.sin(mRoll) + Math.cos(mYaw) * Math.cos(mRoll));
		mTransMatrix.set(2, 1, Math.cos(mPitch) * Math.sin(mRoll));
		mTransMatrix.set(0, 2, Math.cos(mYaw) * Math.sin(mPitch) * Math.cos(mRoll) + Math.sin(mYaw) * Math.sin(mRoll));
		mTransMatrix.set(1, 2, Math.cos(mYaw) * Math.sin(mPitch) * Math.cos(mRoll) - Math.sin(mYaw) * Math.sin(mRoll));
		mTransMatrix.set(2, 2, Math.cos(mPitch) * Math.cos(mRoll));
	}
}