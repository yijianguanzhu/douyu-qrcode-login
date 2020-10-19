/**
 * 
 */
package com.yijianguanzhu.douyu.qrcode.login.enums;

import lombok.AllArgsConstructor;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月16日
 *
 */
@AllArgsConstructor
public enum Code implements BaseCodeEnum {

	ASK_ERROR(9999),

	ASK_UNREALIZED(9997),

	ASK_TIMEOUT(9995),

	ASK_FAILURE(9993);

	private int code;

	@Override
	public int getCode() {
		return this.code;
	}
}
