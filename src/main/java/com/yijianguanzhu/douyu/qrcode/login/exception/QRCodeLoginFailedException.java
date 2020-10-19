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
public class QRCodeLoginFailedException extends BaseException {

	public QRCodeLoginFailedException( Integer code ) {
		super( code );
	}

	public QRCodeLoginFailedException( String errMsg ) {
		super( errMsg );
	}

	public QRCodeLoginFailedException( Integer code, String errMsg ) {
		super( code, errMsg );
	}
}
