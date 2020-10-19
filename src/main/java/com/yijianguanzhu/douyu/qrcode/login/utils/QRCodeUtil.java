/**
 * 二维码工具类
 */
package com.yijianguanzhu.douyu.qrcode.login.utils;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.yijianguanzhu.douyu.qrcode.login.exception.QRCodeCreateFailedException;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月17日
 *
 */
public final class QRCodeUtil {
	// 编码
	private static final String CHARSET = "utf-8";
	// 文件格式
	private static final String FORMAT_NAME = "PNG";
	// 二维码尺寸
	private static final int QRCODE_SIZE = 300;
	// Base64
	private static final String BASE64_STRING = "data:image/png;base64,%s";

	@SuppressWarnings("serial")
	private static final Map<EncodeHintType, Object> HINTS = new LinkedHashMap<EncodeHintType, Object>() {
		{
			this.put( EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H );
			this.put( EncodeHintType.CHARACTER_SET, CHARSET );
			this.put( EncodeHintType.MARGIN, 1 );
		}
	};

	private static final QRCodeWriter QRCODE_WRITER = new QRCodeWriter();

	public static byte[] getQRCode( final String url ) {
		BitMatrix matrix = null;
		try {
			matrix = QRCODE_WRITER.encode( url, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, HINTS );
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream( matrix, FORMAT_NAME, out );
			return out.toByteArray();
		}
		catch ( Exception e ) {
			throw new QRCodeCreateFailedException( e );
		}
	}

	public static String getBase64QRCode( final String url ) {
		final String base64String = Base64.encodeBase64String( getQRCode( url ) );
		return String.format( BASE64_STRING, base64String );
	}
}
