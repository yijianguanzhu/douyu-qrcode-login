/**
 * 
 */
package com.yijianguanzhu.douyu.qrcode.login.exception;

import lombok.Getter;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月16日
 *
 */
@SuppressWarnings("serial")
@Getter
public class BaseException extends RuntimeException {
	private final Integer code;

	private final String errMsg;

	public BaseException( final Integer code, final String errMsg ) {
		super( errMsg );
		this.code = code;
		this.errMsg = errMsg;
	}

	public BaseException( final Integer code ) {
		this.errMsg = null;
		this.code = code;
	}

	public BaseException( final String errMsg ) {
		super( errMsg );
		this.code = null;
		this.errMsg = errMsg;
	}

	public BaseException( final Throwable cause ) {
		super( cause );
		this.errMsg = null;
		this.code = null;
	}
}
