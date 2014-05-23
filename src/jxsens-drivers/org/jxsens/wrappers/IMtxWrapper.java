package org.jxsens.wrappers;

import java.io.IOException;

import org.jxsens.MtxListener;


public interface IMtxWrapper {

	public abstract void send(byte data[]) throws IOException;
	public abstract void close();
	public abstract void open() throws Exception;
	public abstract void addListener(MtxListener listener);
	
}
