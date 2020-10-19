/**
 * 
 */
package com.yijianguanzhu.douyu.qrcode.login.http;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yijianguanzhu.douyu.qrcode.login.enums.Code;
import com.yijianguanzhu.douyu.qrcode.login.enums.Mode;
import com.yijianguanzhu.douyu.qrcode.login.exception.HttpRequestException;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月16日
 *
 */
@Slf4j
@Contract(threading = ThreadingBehavior.SAFE)
public final class Http {

	private final static int CONNECTION_TIMEOUT = 2000;// 请求超时 (单位：ms)
	private final static int READ_TIMEOUT = 3000;// 传输超时 (单位：ms)
	private final static int CONNECTION_REQUEST_TIMEOUT = 1000; // 等待连接池的连接的超时时间(单位：ms)
	private final static ObjectMapper MAPPER = new ObjectMapper();

	public static <T> T getHttpResults( String url, Class<T> clazz, Mode requestType ) {
		return getHttpResults( url, null, clazz, null, requestType, null );
	}

	public static <T, R> T getHttpResults( @NonNull String url, Map<String, String> headers, @NonNull Class<T> clazz,
			R body, @NonNull Mode requestType, ContentType contentType ) {

		if ( log.isDebugEnabled() ) {
			log.debug( "Request Mode:{}, Request URL:{}", requestType, url );
		}
		try {
			return MAPPER.readValue( doFinal( url, body, headers, requestType, contentType ), clazz );
		}
		catch ( IOException e ) {
			throw new HttpRequestException( e );
		}
	}

	public static <T, R> T getHttpResults( String url, Map<String, String> headers, TypeReference<T> valueTypeRef,
			R body, Mode requestType, ContentType contentType ) {

		if ( log.isDebugEnabled() ) {
			log.debug( "Request Mode:{}, Request URL:{}", requestType, url );
		}
		try {
			return MAPPER.readValue( doFinal( url, body, headers, requestType, contentType ), valueTypeRef );
		}
		catch ( IOException e ) {
			throw new HttpRequestException( e );
		}
	}

	public static HttpResponse getHttpResponse( String url, Map<String, String> headers, Mode requestType ) {
		try {
			return getHttpResponse( url, null, headers, requestType, null );
		}
		catch ( IOException e ) {
			throw new HttpRequestException( e );
		}
	}

	public static <R> HttpResponse getHttpResponse( String url, R body,
			Map<String, String> headers, Mode requestType, ContentType contentType )
			throws JsonParseException, JsonMappingException, IOException {

		HttpClient client = HttpClientBuilder.init();
		HttpRequest request = requestType.getRequestMethod( url );
		if ( headers != null ) {
			headers.forEach( request::addHeader );
		}

		HttpEntity httpEntity = null;

		if ( request instanceof HttpEntityEnclosingRequestBase && body != null ) {

			if ( contentType == ContentType.APPLICATION_FORM_URLENCODED ) {
				httpEntity = post2Default( body );
			}
			else {
				httpEntity = new StringEntity( MAPPER.writeValueAsString( body ), StandardCharsets.UTF_8 );
			}

			( ( HttpEntityEnclosingRequestBase ) request )
					.setEntity( httpEntity );
		}

		HttpResponse response = null;

		try {
			response = client.execute( ( HttpUriRequest ) request );
			if ( response.getStatusLine().getStatusCode() != HttpStatus.SC_OK ) {
				throw new HttpRequestException( Code.ASK_FAILURE.getCode(),
						String.format( "调用 \"%s\" 失败 :%d", url, response.getStatusLine().getStatusCode() ) );
			}
			return response;
		}
		catch ( SocketTimeoutException e ) {
			throw new HttpRequestException( Code.ASK_TIMEOUT.getCode(),
					String.format( "调用 \"%s\" 失败 : %s :%s", url, "请求连接超时", e.toString() ) );
		}
	}

	private static <R> String doFinal( String url, R body,
			Map<String, String> headers, Mode requestType, ContentType contentType )
			throws ParseException, IOException {
		HttpResponse response = getHttpResponse( url, body, headers, requestType, contentType );
		try {
			return EntityUtils.toString( response.getEntity(), StandardCharsets.UTF_8 );
		}
		finally {
			EntityUtils.consumeQuietly( response.getEntity() );
		}
	}

	private static Map<String, Object> bean2Map( Object bean )
			throws JsonParseException, JsonMappingException, IOException {
		Objects.requireNonNull( bean, "bean" );

		String tmpStr = MAPPER.writeValueAsString( bean );
		return MAPPER.readValue( tmpStr, new TypeReference<Map<String, Object>>() {
		} );
	}

	@SuppressWarnings("unchecked")
	private static <T> UrlEncodedFormEntity post2Default( T body )
			throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> mapBody = null;
		if ( body instanceof Map ) {
			mapBody = ( Map<String, Object> ) body;
		}
		else {
			mapBody = bean2Map( body );
		}

		List<BasicNameValuePair> list = new LinkedList<BasicNameValuePair>();
		mapBody.forEach( ( key, value ) -> list.add( new BasicNameValuePair( key, String.valueOf( value ) ) ) );

		return new UrlEncodedFormEntity( list, StandardCharsets.UTF_8 );
	}

	static {
		/** 在反序列化时(从源字符串转成特定java对象的过程),忽略不能识别的字段 */
		MAPPER.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
	}

	/* 自定义管理httpClient连接池 */
	private static class HttpClientBuilder {

		private static PoolingHttpClientConnectionManager manager = null;
		private static HttpClient httpClient = null;

		private static HttpClient init() {
			return HttpClientBuilder.httpClient;
		}

		/** 初始化HttpClient */
		static {

			manager = new PoolingHttpClientConnectionManager();
			manager.setMaxTotal( 400 );// 设置整个连接池最大连接数
			manager.setDefaultMaxPerRoute( 50 ); // 表示将每个路由的默认最大连接数设置为max
			/** 在多少秒不活动后，验证连接是否断开 */
			manager.setValidateAfterInactivity( 60000 );

			httpClient = HttpClients.custom()
					.setConnectionManager( manager )
					.setDefaultRequestConfig( RequestConfig
							.custom()
							.setSocketTimeout( READ_TIMEOUT )
							.setConnectTimeout( CONNECTION_TIMEOUT )
							.setConnectionRequestTimeout( CONNECTION_REQUEST_TIMEOUT )
							.build() )
					/** 设置保活策略 */
					.setKeepAliveStrategy( ( response, context ) -> {
						Objects.requireNonNull( response, "HTTP response may not be null" );
						final HeaderElementIterator it = new BasicHeaderElementIterator(
								response.headerIterator( HTTP.CONN_KEEP_ALIVE ) );
						while ( it.hasNext() ) {
							final HeaderElement he = it.nextElement();
							final String param = he.getName();
							final String value = he.getValue();
							if ( value != null && param.equalsIgnoreCase( "timeout" ) ) {
								try {
									return Long.parseLong( value ) * 1000;
								}
								catch ( final NumberFormatException ignore ) {
								}
							}
						}
						return 1800 * 1000;
					} )
					/** 定时清理空闲连接和过期连接(单位:s) */
					.evictIdleConnections( 900, TimeUnit.SECONDS )
					.build();
			log.debug( "HTTP connection pool initialization completed." );
		}
	}
}
