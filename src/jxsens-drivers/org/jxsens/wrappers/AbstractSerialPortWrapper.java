package org.jxsens.wrappers;

import java.io.*;
import java.util.*;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

import com.sun.comm.Win32Driver;

public abstract class AbstractSerialPortWrapper implements Runnable, SerialPortEventListener {

	private static CommPortIdentifier portId;
	private static Enumeration<CommPortIdentifier> portList;

	protected static OutputStream outputStream;
	protected static InputStream inputStream;
	
	private SerialPort serialPort;
	private Thread readThread;
	private boolean portOpen;
	private static String mPortName;
	private int mBaudRate;

	public AbstractSerialPortWrapper(String s, int i) throws Exception {
		mPortName = s;
		mBaudRate = i;
	}

	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}

	public void serialEvent(SerialPortEvent event) {

		switch (event.getEventType()) {
			case SerialPortEvent.BI:
				System.out.println("\n--- BREAK RECEIVED ---\n");
			case SerialPortEvent.OE:
			case SerialPortEvent.FE:
			case SerialPortEvent.PE:
			case SerialPortEvent.CD:
			case SerialPortEvent.CTS:
			case SerialPortEvent.DSR:
			case SerialPortEvent.RI:
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
				break;
			case SerialPortEvent.DATA_AVAILABLE:
				packetReader();

		}
	}

	public abstract void packetReader();
	
	public void read() {
		byte[] readBuffer = new byte[20];
		try {
			while (inputStream.available() > 0) {
				inputStream.read(readBuffer);
			}
			System.out.print(new String(readBuffer));
		} catch (IOException e) {
		}
	}

	public void close() {
		if (!portOpen) {
			return;
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			System.err.println(e);
		}
		serialPort.close();
	}

	@SuppressWarnings("unchecked")
	public void open() throws Exception {
		System.out.println("Starting....");
		portList = CommPortIdentifier.getPortIdentifiers();
		System.out.println("found portList = " + portList.toString());
		Win32Driver w32Driver = new Win32Driver();
		w32Driver.initialize();

		try {
			while (portList.hasMoreElements()) {
				portId = portList.nextElement();
				System.out.println("found portId = " + portId.getName());
				if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					if (portId.getName().equals(mPortName)) {
						System.out.println("Found Serial Port " + mPortName);

						System.out.println("Opening Serial......");

						try {
							serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
							portOpen = true;
						} catch (PortInUseException e) {
							System.out.println("!!!!!!!!! PortInUseException ");
						}
						try {
							outputStream = serialPort.getOutputStream();
						} catch (IOException e) {
						}

						try {
							System.out.println("Getting input stream....");
							inputStream = serialPort.getInputStream();
							System.out.println("serialPort.getName() " + serialPort.getName());

							// in = new BufferedInputStream(inputStream, 1024);
						} catch (IOException e) {
							serialPort.close();
							System.out.println("Error opening i/o streams");
						}
						try {
							serialPort.addEventListener(this);
						} catch (TooManyListenersException e) {
							System.out.println("!!!!!!!!! TooManyListenersException ");
						}
						serialPort.notifyOnDataAvailable(true);

						try {
							serialPort.setSerialPortParams(mBaudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
						} catch (UnsupportedCommOperationException e) {
							System.out.println("!!!!!!!!! UnsupportedCommOperationException ");
						}

						System.out.println("Parameters set");
						readThread = new Thread(this);
						readThread.start();
					}
				}
			}

		} catch (NullPointerException e) {
			System.out.println("ERROR: Device not found");
			System.exit(1);
		}
	}

	public void send(byte[] msg) throws IOException {
		outputStream.write(msg);
		outputStream.flush();
	}

	public void send(String messageString) throws IOException {
		send(messageString.getBytes());
	}
}
