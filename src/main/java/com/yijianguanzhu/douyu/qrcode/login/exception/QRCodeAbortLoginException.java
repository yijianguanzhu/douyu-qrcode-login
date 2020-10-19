/**
 * 取消登录异常
 */
package com.yijianguanzhu.douyu.qrcode.login.exception;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月18日
 *
 */
@SuppressWarnings("serial")
public class QRCodeAbortLoginException extends BaseException {

	public QRCodeAbortLoginException( Integer code ) {
		super( code );
	}

	public QRCodeAbortLoginException( String errMsg ) {
		super( errMsg );
	}

	public QRCodeAbortLoginException( Integer code, String errMsg ) {
		super( code, errMsg );
	}
}
