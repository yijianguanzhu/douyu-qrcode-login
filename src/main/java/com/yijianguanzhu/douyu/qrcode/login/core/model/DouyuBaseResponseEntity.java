/**
 * 响应数据
 */
package com.yijianguanzhu.douyu.qrcode.login.core.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月17日
 *
 */
@Getter
@Setter
@ToString
public class DouyuBaseResponseEntity<T> {

	private int error;

	private String msg;

	private T data;
}
