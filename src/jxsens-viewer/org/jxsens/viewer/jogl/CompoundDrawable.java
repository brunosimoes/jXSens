package org.jxsens.viewer.jogl;

import java.util.ArrayList;
import javax.media.opengl.GLAutoDrawable;

public class CompoundDrawable implements IGLDrawable {

	private ArrayList<IGLDrawable> mChildren;

	public CompoundDrawable() {
		mChildren = new ArrayList<IGLDrawable>();
	}

	public synchronized void addDrawable(IGLDrawable igldrawable) {
		mChildren.add(igldrawable);
	}

	public synchronized void clearMeasurements() {
		mChildren.clear();
	}

	public synchronized void removeMeasurement(IGLDrawable igldrawable) {
		mChildren.remove(igldrawable);
	}

	public synchronized void display(GLAutoDrawable glautodrawable) {
		for (int i = 0; i < mChildren.size(); i++)
			mChildren.get(i).display(glautodrawable);
	}
}