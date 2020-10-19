/**
 * 
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
@Setter
@Getter
@ToString
public class DouyuData {

	private String code;

	private int expire;

	private String url;
}
