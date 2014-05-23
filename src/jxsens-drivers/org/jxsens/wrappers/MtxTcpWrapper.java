package org.jxsens.wrappers;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MtxTcpWrapper extends MtxWrapper {

	private boolean mOpen;
	private int mPort;
	private Thread mReadThread;
	private Socket mSocket;
	private ServerSocket mSocketSvr;
	private InputStream mInStream;
	private OutputStream mOutStream;
	
	public MtxTcpWrapper(int i) throws Exception {
		mPort = i;
	}

	public void open() throws IOException {
		if (!mOpen)
			try {
				mSocketSvr = new ServerSocket(mPort);
				mSocket = mSocketSvr.accept();
				mInStream = mSocket.getInputStream();
				mOutStream = mSocket.getOutputStream();
				mReadThread = new Thread() {

					public void run() {
						while (mOpen)
							processNewData(mInStream);
					}
				};
				mOpen = true;
				mReadThread.start();
			} catch (IOException ioexception) {
				close();
				throw ioexception;
			}
	}

	public void close() {
		if (mOpen && mSocketSvr != null) {
			try {
				mOpen = false;
				if (mReadThread != null)
					mReadThread.join();
				mSocket.close();
				mSocketSvr.close();
			} catch (Exception exception) {
			}
			mSocketSvr = null;
		}
		mOpen = false;
	}

	public void send(byte data[]) throws IOException {
		super.send(mOutStream, data);
	}
}
