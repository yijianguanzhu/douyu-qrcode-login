/**
 * cookie过期，刷新token
 */
package com.yijianguanzhu.douyu.qrcode.login.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import com.beust.jcommander.internal.Maps;
import com.yijianguanzhu.douyu.qrcode.login.core.config.DouyuHttpRequestConfig;
import com.yijianguanzhu.douyu.qrcode.login.enums.Mode;
import com.yijianguanzhu.douyu.qrcode.login.exception.CookieRefreshFailedException;
import com.yijianguanzhu.douyu.qrcode.login.http.Http;
import com.yijianguanzhu.douyu.qrcode.login.utils.CookieUtil;
import com.yijianguanzhu.douyu.qrcode.login.utils.Message2BeanUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月18日
 *
 */
@Slf4j
public final class RefreshCookie {
	private final static String COOKIE = "cookie";

	public static Map<String, String> refresh( final Map<String, String> oldCookie ) {

		log.info( "开始刷新cookie..." );
		final Map<String, String> headers = Maps.<String, String> newLinkedHashMap();
		headers.put( COOKIE, CookieUtil.map2CookieString( oldCookie ) );
		headers.putAll( DouyuHttpRequestConfig.REFRESH_COOKIE_URL_HEADERS );

		final HttpResponse response = Http.getHttpResponse( DouyuHttpRequestConfig.REFRESH_COOKIE_URL, headers, Mode.GET );
		Map<?, ?> data = null;
		try {
			String resp = EntityUtils.toString( response.getEntity(), StandardCharsets.UTF_8 );
			resp = resp.replace( "(", "" ).replace( ")", "" );
			data = Message2BeanUtil.bean( resp, Map.class );
		}
		catch ( ParseException | IOException e ) {
			log.warn( "cookie刷新失败" );
			log.error( "Un-Excepted Error occured. Cause By: \n", e );
			throw new CookieRefreshFailedException( e.getMessage() );
		}
		finally {
			EntityUtils.consumeQuietly( response.getEntity() );
		}

		log.debug( data.toString() );
		if ( ( int ) data.get( "error" ) != 0 ) {
			log.warn( "cookie刷新失败，{}", data.toString() );
			throw new CookieRefreshFailedException( ( String ) data.get( "msg" ) );
		}

		// 执行到这，说明cookie刷新成功
		/**
		 * 开始获取cookie
		 */
		log.info( "cookie刷新成功" );
		final Map<String, String> newCookies = CookieUtil.cookies( response );
		CookieUtil.fillCookies( oldCookie, newCookies );
		return newCookies;
	}
}
