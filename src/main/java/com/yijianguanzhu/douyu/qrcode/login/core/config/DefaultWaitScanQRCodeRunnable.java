/**
 * 
 */
package com.yijianguanzhu.douyu.qrcode.login.core.config;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.yijianguanzhu.douyu.qrcode.login.core.model.SuccessScanQRCode;
import com.yijianguanzhu.douyu.qrcode.login.enums.Mode;
import com.yijianguanzhu.douyu.qrcode.login.exception.QRCodeAbortLoginException;
import com.yijianguanzhu.douyu.qrcode.login.exception.QRCodeTimeoutException;
import com.yijianguanzhu.douyu.qrcode.login.http.Http;
import com.yijianguanzhu.douyu.qrcode.login.utils.CookieUtil;
import com.yijianguanzhu.douyu.qrcode.login.utils.Message2BeanUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月17日
 *
 */
@Slf4j
public class DefaultWaitScanQRCodeRunnable implements Runnable {
	private CompletableFuture<SuccessScanQRCode> future;
	private ScheduledFuture<?> schedule;
	private long ttl;
	private String code;
	// 上层应用消费已扫码消息
	private Consumer<String> consumer;

	public DefaultWaitScanQRCodeRunnable( CompletableFuture<SuccessScanQRCode> future, long ttl, String code ) {
		this.future = future;
		this.ttl = ttl;
		this.code = code;
	}

	public DefaultWaitScanQRCodeRunnable( CompletableFuture<SuccessScanQRCode> future, long ttl, String code, Consumer<String> consumer ) {
		this.future = future;
		this.ttl = ttl;
		this.code = code;
		this.consumer = consumer;
	}

	@Override
	public void run() {
		long currentTimeMillis = System.currentTimeMillis();
		if ( currentTimeMillis > ttl ) {
			log.info( "二维码过期" );
			schedule.cancel( true );
			future.completeExceptionally( new QRCodeTimeoutException( "二维码过期" ) );
			return;
		}

		Map<String, Object> result = null;
		HttpResponse response = null;
		try {
			response = Http.getHttpResponse(
					String.format( DouyuHttpRequestConfig.QRCODE_IS_SCAN_URL, currentTimeMillis, code ),
					null, DouyuHttpRequestConfig.QRCODE_IS_SCAN_URL_HEADERS, Mode.GET, ContentType.APPLICATION_FORM_URLENCODED );
			final String resp = EntityUtils.toString( response.getEntity(), StandardCharsets.UTF_8 );
			result = Message2BeanUtil.bean( resp, new TypeReference<Map<String, Object>>() {
			} );
		}
		catch ( Exception e ) {
			log.error( "Un-Excepted Error occured. Cause By: \n", e );
			schedule.cancel( true );
			future.completeExceptionally( e );
			return;
		}
		finally {
			EntityUtils.consumeQuietly( response.getEntity() );
		}

		log.debug( result.toString() );
		final int error = ( int ) result.get( "error" );
		if ( error != 0 ) {
			log.info( ( String ) result.get( "data" ) );
			// 客户端已扫码
			if ( error == 1 && Objects.nonNull( consumer ) ) {
				consumer.accept( ( String ) result.get( "data" ) );
			}
			if ( error == -1 ) {
				// 客户端取消登录
				schedule.cancel( true );
				future.completeExceptionally( new QRCodeAbortLoginException( "客户端取消登录" ) );
			}
			return;
		}
		// 扫码完成
		log.info( "扫码成功" );
		final SuccessScanQRCode succ = new SuccessScanQRCode();
		String url = ( String ) ( ( Map<?, ?> ) result.get( "data" ) ).get( "url" );
		succ.setUrl( url );
		succ.setCookies( CookieUtil.cookies( response ) );

		future.complete( succ );
		schedule.cancel( true );
	}

	public void schedule() {
		final ScheduledExecutorService scheduled = DouyuHttpRequestConfig.scheduled();
		this.schedule = scheduled.scheduleAtFixedRate( this, 3, 3, TimeUnit.SECONDS );
		if ( future instanceof DefaultCompletableFuture ) {
			( ( DefaultCompletableFuture ) future ).setRunnable( this );
			( ( DefaultCompletableFuture ) future ).setSchedule( schedule );
		}
	}
}
