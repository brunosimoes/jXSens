package org.jxsens;

public class Lock {

	private boolean mLocked;

	public Lock() {
		mLocked = false;
	}

	public synchronized void acquireLock() {
		while (mLocked)
			try {
				wait();
			} catch (InterruptedException interruptedexception) {
				interruptedexception.printStackTrace();
				System.exit(1);
			}
		mLocked = true;
	}

	public synchronized void releaseLock() {
		mLocked = false;
		notifyAll();
	}
}