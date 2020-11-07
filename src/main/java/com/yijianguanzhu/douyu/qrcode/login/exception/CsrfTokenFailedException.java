package com.yijianguanzhu.douyu.qrcode.login.exception;

@SuppressWarnings("serial")
public class CsrfTokenFailedException extends BaseException {

	public CsrfTokenFailedException( Integer code ) {
		super( code );
	}

	public CsrfTokenFailedException( String errMsg ) {
		super( errMsg );
	}

	public CsrfTokenFailedException( Integer code, String errMsg ) {
		super( code, errMsg );
	}
}
