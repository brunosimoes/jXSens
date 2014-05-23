package org.jxsens;

public class MtxCommException extends Exception {

	private static final long serialVersionUID = 0x61122773e91ea9b7L;
	public static final int XSENS_PERIOD_NOT_IN_RANGE = 3;
	public static final int XSENS_MESSAGE_SENT_IS_INVALID = 4;
	public static final int XSENS_BAUDRATE_NOT_IN_RANGE = 20;
	public static final int XSENS_PARAM_NOT_IN_RANGE = 21;
	public static final int XSENS_TIMER_OVERFLOW = 30;
	
	public static final int TIMEOUT = 0;
	public static final int IOERROR = 1;
	public static final int WRONG_MODE = 2;
	
	private int mErrorCode;
	private Exception mInnerException;

	public MtxCommException(int i) {
		mErrorCode = i;
	}

	public MtxCommException(Exception exception, int i) {
		mErrorCode = i;
		mInnerException = exception;
	}

	public int getErrorCode() {
		return mErrorCode;
	}

	public Exception getInnerException() {
		return mInnerException;
	}

}