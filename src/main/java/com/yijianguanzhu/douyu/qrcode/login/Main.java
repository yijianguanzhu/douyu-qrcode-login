package com.yijianguanzhu.douyu.qrcode.login;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.yijianguanzhu.douyu.qrcode.login.core.DouyuLogin;
import com.yijianguanzhu.douyu.qrcode.login.core.model.QRCode;
import com.yijianguanzhu.douyu.qrcode.login.utils.CookieUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
	public static void main( String[] args ) {
		QRCode<String> qrCode = DouyuLogin.getBase64QRCode();
		writeQRCode( qrCode.getQrCode() );
		Map<String, String> cookie = DouyuLogin.getDouyuCookie( qrCode.getTtl(), qrCode.getCode() );
		System.out.println( CookieUtil.cookies( cookie ) );
	}

	public static void writeQRCode( String qrCode ) {
		qrCode = qrCode.substring( qrCode.indexOf( "," ) + 1 );
		try ( FileOutputStream outputStream = new FileOutputStream( "qrcode.png", false ) ) {
			outputStream.write( Base64.decodeBase64( qrCode ) );
		}
		catch ( IOException e ) {
			// ignore
			log.error( "保存二维码失败：{}", e.getMessage() );
		}
		finally {
			openQRCode();
		}
	}

	private static void openQRCode() {
		File file = new File( "qrcode.png" );
		try {
			Desktop.getDesktop().open( file );
		}
		catch ( IOException e ) {
			// ignore
			log.error( "打开二维码失败：{}", e.getMessage() );
		}
	}
}
