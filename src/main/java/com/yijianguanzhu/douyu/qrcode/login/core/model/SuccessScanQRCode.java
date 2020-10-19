/**
 * 成功扫描二维码后，响应的数据
 */
package com.yijianguanzhu.douyu.qrcode.login.core.model;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月18日
 *
 */
@Getter
@Setter
public class SuccessScanQRCode {

	private String url;

	private Map<String, String> cookies;
}
