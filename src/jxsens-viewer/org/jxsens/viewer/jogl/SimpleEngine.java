package org.jxsens.viewer.jogl;

import java.awt.Dimension;
import java.util.ArrayList;
import javax.media.opengl.*;

public class SimpleEngine implements GLEventListener {

	private CameraController camera;
	private ArrayList<IGLDrawable> shapes;
	private Dimension windowSize;

	public SimpleEngine() {
		camera = new SimpleCameraController();
		shapes = new ArrayList<IGLDrawable>();
		windowSize = new Dimension(100, 100);
	}

	public void setCameraController(CameraController ccontroller) {
		camera = ccontroller;
	}

	public synchronized void addShape(IGLDrawable drawable) {
		shapes.add(drawable);
	}

	public synchronized void removeShape(IGLDrawable drawable) {
		shapes.remove(drawable);
	}

	public synchronized void clearShapes() {
		shapes.clear();
	}

	public void reshape(GLAutoDrawable drawable, int i, int j, int k, int l) {
		windowSize = new Dimension(k, l);
	}

	public void displayChanged(GLAutoDrawable drawable, boolean flag, boolean flag1) {
	}

	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glEnable(3042);
		gl.glShadeModel(7425);
		gl.glBlendFunc(770, 771);
		gl.glEnable(2832);
		gl.glEnable(2848);
		gl.glDisable(2929);
		gl.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public synchronized void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glClear(16640);
		camera.doCameraTransformation(drawable, windowSize.width, windowSize.height);
		drawShapes(drawable);
	}

	private void drawShapes(GLAutoDrawable drawable) {
		for (int i = 0; i < shapes.size(); i++) {
			IGLDrawable igldrawable = shapes.get(i);
			igldrawable.display(drawable);
		}
	}

}