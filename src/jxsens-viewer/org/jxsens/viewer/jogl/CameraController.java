package org.jxsens.viewer.jogl;

import javax.media.opengl.GLAutoDrawable;

public interface CameraController {

	public abstract void doCameraTransformation(GLAutoDrawable glautodrawable, int i, int j);

}