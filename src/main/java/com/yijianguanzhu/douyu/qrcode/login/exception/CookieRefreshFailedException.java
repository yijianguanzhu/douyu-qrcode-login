/**
 * 
 */
package com.yijianguanzhu.douyu.qrcode.login.exception;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月18日
 *
 */
@SuppressWarnings("serial")
public class CookieRefreshFailedException extends BaseException {

	public CookieRefreshFailedException( Integer code ) {
		super( code );
	}

	public CookieRefreshFailedException( String errMsg ) {
		super( errMsg );
	}

	public CookieRefreshFailedException( Integer code, String errMsg ) {
		super( code, errMsg );
	}
}
