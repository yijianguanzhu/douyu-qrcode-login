/**
 * 扫码登录
 */
package com.yijianguanzhu.douyu.qrcode.login.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.yijianguanzhu.douyu.qrcode.login.core.config.DefaultWaitScanQRCodeRunnable;
import com.yijianguanzhu.douyu.qrcode.login.core.config.DouyuHttpRequestConfig;
import com.yijianguanzhu.douyu.qrcode.login.core.model.DouyuBaseResponseEntity;
import com.yijianguanzhu.douyu.qrcode.login.core.model.DouyuData;
import com.yijianguanzhu.douyu.qrcode.login.core.model.QRCode;
import com.yijianguanzhu.douyu.qrcode.login.core.model.SuccessScanQRCode;
import com.yijianguanzhu.douyu.qrcode.login.enums.Mode;
import com.yijianguanzhu.douyu.qrcode.login.exception.QRCodeLoginFailedException;
import com.yijianguanzhu.douyu.qrcode.login.http.Http;
import com.yijianguanzhu.douyu.qrcode.login.utils.CookieUtil;
import com.yijianguanzhu.douyu.qrcode.login.utils.Message2BeanUtil;
import com.yijianguanzhu.douyu.qrcode.login.utils.QRCodeUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月16日
 *
 */
@Slf4j
public final class DouyuLogin {

	private static DouyuData getQRCodeUrl() {

		final DouyuBaseResponseEntity<DouyuData> resp = Http.getHttpResults( DouyuHttpRequestConfig.QRCODE_URL,
				DouyuHttpRequestConfig.QRCODE_URL_HEADERS,
				new TypeReference<DouyuBaseResponseEntity<DouyuData>>() {
				}, DouyuHttpRequestConfig.QRCODE_URL_DATA, Mode.POST, ContentType.APPLICATION_FORM_URLENCODED );

		log.debug( resp.toString() );
		return resp.getData();
	}

	private static QRCode<String> getQRCodeScanUrl() {
		final DouyuData data = getQRCodeUrl();
		// 二维码过期时间
		long ttl = System.currentTimeMillis() + data.getExpire() * 1000;
		QRCode<String> qrCode = new QRCode<>();
		qrCode.setTtl( ttl );
		qrCode.setCode( data.getCode() );
		qrCode.setQrCode( String.format( DouyuHttpRequestConfig.QRCODE_SCAN_URL, data.getCode() ) );
		return qrCode;
	}

	// 获取登录二维码(byte[])
	public static QRCode<byte[]> getQRCode() {
		final QRCode<String> codeScanUrl = getQRCodeScanUrl();
		final QRCode<byte[]> qrCode = new QRCode<>();
		qrCode.setTtl( codeScanUrl.getTtl() );
		qrCode.setCode( codeScanUrl.getCode() );
		qrCode.setQrCode( QRCodeUtil.getQRCode( codeScanUrl.getQrCode() ) );
		return qrCode;
	}

	// 获取登录二维码(base64)
	public static QRCode<String> getBase64QRCode() {
		final QRCode<String> qrCode = getQRCodeScanUrl();
		qrCode.setQrCode( QRCodeUtil.getBase64QRCode( qrCode.getQrCode() ) );
		return qrCode;
	}

	// 获取登录url(异步回调)
	public static CompletableFuture<SuccessScanQRCode> getLoginUrl( final long ttl, final String code ) {
		final CompletableFuture<SuccessScanQRCode> future = new CompletableFuture<>();
		final DefaultWaitScanQRCodeRunnable runnable = new DefaultWaitScanQRCodeRunnable( future, ttl, code );
		runnable.schedule();
		return future;
	}

	public static Map<String, String> getDouyuCookie( final long ttl, final String code ) {
		final CompletableFuture<SuccessScanQRCode> future = getLoginUrl( ttl, code );
		SuccessScanQRCode succ = null;
		try {
			succ = future.get();
		}
		catch ( InterruptedException | ExecutionException e ) {
			log.error( "Un-Excepted Error occured. Cause By: \n", e );
			throw new RuntimeException( e );
		}
		return getDouyuCookie( succ );
	}

	public static Map<String, String> getDouyuCookie( SuccessScanQRCode succ ) {
		String url = String.format( DouyuHttpRequestConfig.LOGIN_URL, succ.getUrl(), System.currentTimeMillis() );
		HttpResponse response = Http.getHttpResponse( url, DouyuHttpRequestConfig.LOGIN_URL_HEADERS, Mode.GET );
		Map<?, ?> data = null;
		try {
			String resp = EntityUtils.toString( response.getEntity(), StandardCharsets.UTF_8 );
			resp = resp.replace( "appClient_json_callback(", "" ).replace( ")", "" );
			data = Message2BeanUtil.bean( resp, Map.class );
		}
		catch ( ParseException | IOException e ) {
			log.warn( "登录失败" );
			log.error( "Un-Excepted Error occured. Cause By: \n", e );
			throw new QRCodeLoginFailedException( e.getMessage() );
		}
		finally {
			EntityUtils.consumeQuietly( response.getEntity() );
		}

		log.debug( data.toString() );
		if ( ( int ) data.get( "error" ) != 0 ) {
			log.warn( "登录失败，{}", data.toString() );
			throw new QRCodeLoginFailedException( ( String ) data.get( "msg" ) );
		}

		// 执行到这，说明登录成功
		/**
		 * 开始获取cookie
		 */
		log.info( "登录成功" );
		final Map<String, String> cookies = CookieUtil.cookies( response );
		cookies.putAll( succ.getCookies() );
		return cookies;
	}
}
