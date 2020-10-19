/**
 * 
 */
package com.yijianguanzhu.douyu.qrcode.login.exception;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月17日
 *
 */
@SuppressWarnings("serial")
public class QRCodeTimeoutException extends BaseException {

	public QRCodeTimeoutException( Integer code ) {
		super( code );
	}

	public QRCodeTimeoutException( String errMsg ) {
		super( errMsg );
	}

	public QRCodeTimeoutException( Integer code, String errMsg ) {
		super( code, errMsg );
	}
}
