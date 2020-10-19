/**
 * 二维码的过期时间
 */
package com.yijianguanzhu.douyu.qrcode.login.core.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月17日
 *
 */
@Getter
@Setter
public class QRCode<T> {

	private long ttl;

	private String code;

	private T qrCode;
}
