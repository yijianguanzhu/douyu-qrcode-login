/**
 * 
 */
package com.yijianguanzhu.douyu.qrcode.login.utils;

import java.util.Map;
import java.util.stream.Stream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;

import com.beust.jcommander.internal.Maps;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月18日
 *
 */
public final class CookieUtil {

	private final static String EQUAL_SIGN = "=";
	private final static String SEMICOLON = ";";
	private final static String SET_COOKIE = "Set-Cookie";

	// HttpResponse to Map
	public static Map<String, String> cookies( HttpResponse response ) {
		final Map<String, String> cookies = Maps.<String, String> newLinkedHashMap();
		final Header[] headers = response.getHeaders( SET_COOKIE );
		for ( final Header header : headers ) {
			final HeaderElement[] elements = header.getElements();
			for ( final HeaderElement headerElement : elements ) {
				cookies.put( headerElement.getName(), headerElement.getValue() );
				break;
			}
		}
		return cookies;
	}

	// Map to String
	public static String cookies( final Map<String, String> cookies ) {
		final StringBuilder builder = new StringBuilder();
		cookies.forEach( ( key, value ) -> builder.append( key ).append( EQUAL_SIGN ).append( value ).append( SEMICOLON ) );
		return builder.toString();
	}

	// String to Map
	public static Map<String, String> cookies( final String cookieString ) {
		final Map<String, String> cookies = Maps.<String, String> newLinkedHashMap();
		String[] keyValues = cookieString.split( SEMICOLON );
		Stream.of( keyValues ).filter( keyValue -> keyValue.contains( EQUAL_SIGN ) ).forEach( result -> {
			String[] keyValue = result.split( EQUAL_SIGN );
			cookies.put( keyValue[0], keyValue[1] );
		} );
		return cookies;
	}

	public static void fillCookies( final Map<String, String> oldCookies, final Map<String, String> newCookies ) {
		oldCookies.forEach( newCookies::putIfAbsent );
	}
}
