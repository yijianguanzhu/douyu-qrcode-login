/**
 * 
 */
package com.yijianguanzhu.douyu.qrcode.login.utils;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月17日
 *
 */
public class Message2BeanUtil {

	private final static ObjectMapper JACKSONMAPPER = new ObjectMapper();
	static {
		JACKSONMAPPER.setSerializationInclusion( Include.NON_NULL );
		JACKSONMAPPER.configure( DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false );
		JACKSONMAPPER.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
		JACKSONMAPPER.configure( Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true );
	}

	public static <T> T bean( String jsonString, Class<T> clazz ) throws IOException {
		return JACKSONMAPPER.readValue( jsonString, clazz );
	}

	public static <T> T bean( String jsonString, TypeReference<T> valueTypeRef ) throws IOException {
		return JACKSONMAPPER.readValue( jsonString, valueTypeRef );
	}
}
