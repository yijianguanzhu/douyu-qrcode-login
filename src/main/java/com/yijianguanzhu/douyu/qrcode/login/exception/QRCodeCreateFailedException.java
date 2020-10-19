/**
 * 
 */
package com.yijianguanzhu.douyu.qrcode.login.exception;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月19日
 *
 */
@SuppressWarnings("serial")
public class QRCodeCreateFailedException extends BaseException {

	public QRCodeCreateFailedException( Integer code ) {
		super( code );
	}

	public QRCodeCreateFailedException( String errMsg ) {
		super( errMsg );
	}

	public QRCodeCreateFailedException( Integer code, String errMsg ) {
		super( code, errMsg );
	}

	public QRCodeCreateFailedException( Throwable cause ) {
		super( cause );
	}
}
