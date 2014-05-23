package org.jxsens;

import java.util.ArrayList;

public class MtxReporter {

	private ArrayList<MtxListener> mListeners;

	public MtxReporter() {
		mListeners = new ArrayList<MtxListener>();
	}

	public synchronized void notifyListeners(Object obj) {
		for (int i = 0; i < mListeners.size(); i++)
			mListeners.get(i).newEvent(this, obj);
	}

	public synchronized void addListener(MtxListener listener) {
		mListeners.add(listener);
	}

	public synchronized void removeListener(MtxListener genericlistener) {
		mListeners.remove(genericlistener);
	}

	public synchronized void clearListeners() {
		mListeners.clear();
	}
}