/**
 * 
 */
package com.yijianguanzhu.douyu.qrcode.login.exception;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月16日
 *
 */
@SuppressWarnings("serial")
public class HttpRequestException extends BaseException {

	public HttpRequestException( Integer code ) {
		super( code );
	}

	public HttpRequestException( String errMsg ) {
		super( errMsg );
	}

	public HttpRequestException( Integer code, String errMsg ) {
		super( code, errMsg );
	}

	public HttpRequestException( Throwable cause ) {
		super( cause );
	}
}
