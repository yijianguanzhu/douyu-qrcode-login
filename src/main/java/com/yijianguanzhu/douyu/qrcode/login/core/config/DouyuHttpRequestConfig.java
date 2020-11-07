/**
 * 斗鱼请求配置
 */
package com.yijianguanzhu.douyu.qrcode.login.core.config;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

import org.apache.http.HttpHeaders;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月16日
 *
 */
public final class DouyuHttpRequestConfig {

	private DouyuHttpRequestConfig() {
	}

	private static volatile ScheduledExecutorService scheduled_thread_pool;

	public static ScheduledExecutorService scheduled() {
		if ( scheduled_thread_pool == null ) {
			synchronized ( DouyuHttpRequestConfig.class ) {
				if ( scheduled_thread_pool == null ) {
					scheduled_thread_pool = new ScheduledThreadPoolExecutor( 1, new DouyuThreadFactory( "Waiting-Scan-QRCode" ),
							new CallerRunsPolicy() );
				}
			}
		}
		return scheduled_thread_pool;
	}

	/**
	 * 获取二维码url
	 */
	public static final String QRCODE_URL = "https://passport.douyu.com/scan/generateCode";
	@SuppressWarnings("serial")
	public static final Map<String, String> QRCODE_URL_HEADERS = Collections
			.unmodifiableMap( new LinkedHashMap<String, String>() {
				{
					this.put( HttpHeaders.USER_AGENT,
							"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36" );
					this.put( HttpHeaders.REFERER,
							"https://passport.douyu.com/index/login?passport_reg_callback=PASSPORT_REG_SUCCESS_CALLBACK&passport_login_callback=PASSPORT_LOGIN_SUCCESS_CALLBACK&passport_close_callback=PASSPORT_CLOSE_CALLBACK&passport_dp_callback=PASSPORT_DP_CALLBACK&type=login&client_id=1&state=https%3A%2F%2Fwww.douyu.com%2Fdirectory&source=click_topnavi_login" );

				}
			} );
	@SuppressWarnings("serial")
	public static final Map<String, String> QRCODE_URL_DATA = Collections
			.unmodifiableMap( new LinkedHashMap<String, String>() {
				{
					this.put( "client_id", "1" );
				}
			} );

	/**
	 * 生成附着在二维码的url
	 */
	public static final String QRCODE_SCAN_URL = "https://passport.douyu.com/scan/checkLogin?scan_code=%s";

	/**
	 * 判断用户是否已扫描二维码url（等待扫码）
	 */
	public static final String QRCODE_IS_SCAN_URL = "https://passport.douyu.com/lapi/passport/qrcode/check?time=%s&code=%s";
	@SuppressWarnings("serial")
	public static final Map<String, String> QRCODE_IS_SCAN_URL_HEADERS = Collections
			.unmodifiableMap( new LinkedHashMap<String, String>() {
				{
					this.put( HttpHeaders.USER_AGENT,
							"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36" );
					this.put( HttpHeaders.REFERER,
							"https://passport.douyu.com/index/login?passport_reg_callback=PASSPORT_REG_SUCCESS_CALLBACK&passport_login_callback=PASSPORT_LOGIN_SUCCESS_CALLBACK&passport_close_callback=PASSPORT_CLOSE_CALLBACK&passport_dp_callback=PASSPORT_DP_CALLBACK&type=login&client_id=1&state=https%3A%2F%2Fwww.douyu.com%2F&source=click_topnavi_login" );
				}
			} );

	/**
	 * 执行登录，获取用户cookie
	 */
	public static final String LOGIN_URL = "https:%s&callback=appClient_json_callback&_=%s";
	@SuppressWarnings("serial")
	public static final Map<String, String> LOGIN_URL_HEADERS = Collections
			.unmodifiableMap( new LinkedHashMap<String, String>() {
				{
					this.put( HttpHeaders.USER_AGENT,
							"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36" );
					this.put( HttpHeaders.REFERER,
							"https://passport.douyu.com/index/login?passport_reg_callback=PASSPORT_REG_SUCCESS_CALLBACK&passport_login_callback=PASSPORT_LOGIN_SUCCESS_CALLBACK&passport_close_callback=PASSPORT_CLOSE_CALLBACK&passport_dp_callback=PASSPORT_DP_CALLBACK&type=login&client_id=1&state=https%3A%2F%2Fwww.douyu.com%2F&source=click_topnavi_login" );
				}
			} );

	/**
	 * 刷新cookie
	 */
	public static final String REFRESH_COOKIE_URL = "https://passport.douyu.com/lapi/passport/iframe/safeAuth?client_id=1";
	@SuppressWarnings("serial")
	public static final Map<String, String> REFRESH_COOKIE_URL_HEADERS = Collections
			.unmodifiableMap( new LinkedHashMap<String, String>() {
				{
					this.put( HttpHeaders.USER_AGENT,
							"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36" );
					this.put( HttpHeaders.REFERER, "https://www.douyu.com/directory/myFollow" );
					this.put( "X-Requested-With", "XMLHttpRequest" );
				}
			} );

	/**
	 * 验证&&获取csrf
	 */
	public static final String CSRF_TOKEN_URL = "https://www.douyu.com/curl/csrfApi/getCsrfCookie";
	@SuppressWarnings("serial")
	public static final Map<String, String> CSRF_TOKEN_URL_HEADERS = Collections
			.unmodifiableMap( new LinkedHashMap<String, String>() {
				{
					this.put( HttpHeaders.USER_AGENT,
							"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36" );
				}
			} );
}
